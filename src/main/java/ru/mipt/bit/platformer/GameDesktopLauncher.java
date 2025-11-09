package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.controller.InputController;
import ru.mipt.bit.platformer.controller.KeyboardController;
import ru.mipt.bit.platformer.controller.RandomController;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.model.level.FileLevelInfoGenerator;
import ru.mipt.bit.platformer.model.level.LevelInfo;
import ru.mipt.bit.platformer.model.level.LevelInfoGenerator;
import ru.mipt.bit.platformer.model.level.RandomLevelInfoGenerator;
import ru.mipt.bit.platformer.model.Healtable;
import ru.mipt.bit.platformer.view.AnimatedEntityView;
import ru.mipt.bit.platformer.view.Drawble;
import ru.mipt.bit.platformer.view.HealthBarDecorator;
import ru.mipt.bit.platformer.view.HealthBarManager;
import ru.mipt.bit.platformer.view.TiledLevel;

/** */
public class GameDesktopLauncher implements ApplicationListener {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(GameDesktopLauncher.class);

    /** Window width. */
    private static final int WINDOW_WIDTH = 1280;

    /** Window height. */
    private static final int WINDOW_HEIGHT = 1024;

    /** System environment variable for level file path. */
    private static final String LEVEL_CONFIG_KEY_NAME = "USER.LEVEL";

    /** Internal context. */
    private final InternalContext internalContext = new InternalContext();

    /** Batch. */
    private Batch batch;

    /** Tiled level. */
    private TiledLevel tiledLevel;

    /** Tank entity. */
    private Tank tankEntity;

    /** Keyboard handler. */
    private InputController keyboardController;

    /** Keyboard handler. */
    private InputController aiController;

    /** Enemy tanks. */
    private List<Tank> enemyTanks = new ArrayList<>();

    /** Disposables. */
    private final List<Disposable> disposables = new ArrayList<>();

    /** Animated views. */
    private final List<AnimatedEntityView> animatedViews = new ArrayList<>();

    /** */
    private final List<Drawble> drawableViews = new ArrayList<>();

    /** */
    private HealthBarManager healthBarManager;

    /** {@inheritDoc} */
    @Override
    public void create() {
        logger.info("Start initializing game.");

        batch = registerDisposable(SpriteBatch::new);

        tiledLevel = registerDisposable(() -> new TiledLevel(batch, "level.tmx"));

        int levelWidth = tiledLevel.getWidthInTiles();
        int levelHeight = tiledLevel.getHeightInTiles();

        LevelInfo levelInfo = levelGenerator(levelWidth, levelHeight).generate();

        new CommandManager(internalContext);

        new ObstaclesManagerImpl(levelInfo.levelWidth(), levelInfo.levelHeight(), internalContext);

        healthBarManager = new HealthBarManager(internalContext);

        initiateEntities(levelInfo);

        logger.info("Views initialized");

        keyboardController = new KeyboardController(internalContext);
        aiController = new RandomController(internalContext);

        logger.info("Game initialization completed successfully");
    }

    /**
     * @param levelInfo Level info.
     */
    private void initiateEntities(LevelInfo levelInfo) {
        tankEntity = registerEntity(() -> new Tank(levelInfo.playerStartPosition()));

        AnimatedEntityView playerView =
                registerAnimatedView(() -> new AnimatedEntityView(tankEntity, "images/tank_blue.png", 0.4f));
        decorateWithHealthBar(playerView, tankEntity);

        enemyTanks = levelInfo.enemyPositions().stream()
            .map(position -> registerEntity(() -> new Tank(position))).toList();

        enemyTanks.forEach(enemyTank -> {
            AnimatedEntityView enemyView =
                    registerAnimatedView(() -> new AnimatedEntityView(enemyTank, "images/tank_blue.png", 0.4f));
            decorateWithHealthBar(enemyView, enemyTank);
        });

        levelInfo.treePositions().stream().map(treePos -> registerEntity(() -> new Tree(treePos)))
            .forEach(treeEntity -> registerAnimatedView(() -> new AnimatedEntityView(treeEntity, "images/greenTree.png", 0f)));
    }

    /** */
    private static LevelInfoGenerator levelGenerator(int levelWidth, int levelHeight) {
        String levelConfigPath = System.getenv(LEVEL_CONFIG_KEY_NAME);

        if (levelConfigPath != null) {
            logger.info("Using file level loader from USER.LEVEL environment variable: {}", levelConfigPath);

            return new FileLevelInfoGenerator(levelConfigPath);
        }

        logger.info("USER.LEVEL environment variable not set or empty. Using random level generator");

        return new RandomLevelInfoGenerator(levelWidth, levelHeight);
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        clearScreen();

        float deltaTime = Gdx.graphics.getDeltaTime();
        logger.debug("Delta time: {}", deltaTime);

        keyboardController.update(tankEntity);

        enemyTanks.forEach(tank -> aiController.update(tank));

        internalContext.get(CommandManager.class).executeAll();

        animatedViews.forEach(view -> view.update(deltaTime, tiledLevel));

        tiledLevel.render();

        batch.begin();
        drawableViews.forEach(view -> view.draw(batch));
        batch.end();
    }

    /**
     * @param disposable Disposable to register.
     */
    public <T extends Disposable> T registerDisposable(Supplier<T> disposable) {
        T d = disposable.get();
        disposables.add(d);
        return d;
    }

    /**
     * @param entity Entity to register.
     */
    public <T extends Entity> T registerEntity(Supplier<T> entity) {
        T e = entity.get();
        internalContext.get(ObstaclesManager.class).addObstacle(e);
        return e;
    }

    /**
     * @param animatedView Disposable to register.
     */
    public <T extends AnimatedEntityView> T registerAnimatedView(Supplier<T> animatedView) {
        T a = animatedView.get();
        animatedViews.add(a);
        drawableViews.add(a);
        return a;
    }

    /** */
    private void decorateWithHealthBar(AnimatedEntityView view, Healtable healtable) {
        Drawble decoratedView = new HealthBarDecorator(view, healtable, healthBarManager);
        int index = drawableViews.indexOf(view);

        if (index >= 0)
            drawableViews.set(index, decoratedView);
        else
            drawableViews.add(decoratedView);
    }

    /** Clear screen. */
    private static void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        logger.info("Disposing game resources");
        disposables.forEach(Disposable::dispose);
    }

    /** {@inheritDoc} */
    @Override
    public void resize(int width, int height) {}

    /** {@inheritDoc} */
    @Override
    public void pause() {}

    /** {@inheritDoc} */
    @Override
    public void resume() {}

    /**
     * @param args Arguments.
     */
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);

        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
