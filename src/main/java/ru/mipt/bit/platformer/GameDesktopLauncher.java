package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.controller.InputController;
import ru.mipt.bit.platformer.controller.KeyboardController;
import ru.mipt.bit.platformer.controller.RandomController;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.model.level.FileLevelInfoGenerator;
import ru.mipt.bit.platformer.model.level.GameLevel;
import ru.mipt.bit.platformer.model.level.LevelInfo;
import ru.mipt.bit.platformer.model.level.LevelInfoGenerator;
import ru.mipt.bit.platformer.model.level.LevelObserver;
import ru.mipt.bit.platformer.model.level.RandomLevelInfoGenerator;
import ru.mipt.bit.platformer.model.Healtable;
import ru.mipt.bit.platformer.view.AnimatedEntityView;
import ru.mipt.bit.platformer.view.Drawble;
import ru.mipt.bit.platformer.view.HealthBarDecorator;
import ru.mipt.bit.platformer.view.HealthBarManager;
import ru.mipt.bit.platformer.view.TiledLevel;

/** */
public class GameDesktopLauncher implements ApplicationListener, LevelObserver {
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

    /** */
    private GameLevel gameLevel;

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
    private final Map<Entity, AnimatedEntityView> entityViews = new HashMap<>();

    /** */
    private final Map<Entity, Drawble> entityDrawables = new HashMap<>();

    /** */
    private HealthBarManager healthBarManager;

    /** */
    private Texture bulletTexture;

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

        bulletTexture = registerDisposable(this::createBulletTexture);

        gameLevel = new GameLevel(internalContext);
        gameLevel.addObserver(this);

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
        tankEntity = gameLevel.addEntity(new Tank(levelInfo.playerStartPosition()));

        enemyTanks = new ArrayList<>();
        levelInfo.enemyPositions().stream()
            .map(position -> gameLevel.addEntity(new Tank(position)))
            .forEach(enemyTanks::add);

        levelInfo.treePositions().forEach(treePos -> gameLevel.addEntity(new Tree(treePos)));
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

        if (tankEntity != null) {
            keyboardController.update(tankEntity);
        }

        enemyTanks.forEach(tank -> aiController.update(tank));

        internalContext.get(CommandManager.class).executeAll();

        gameLevel.update(deltaTime);

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
     * @param animatedView Disposable to register.
     */
    public <T extends AnimatedEntityView> T registerAnimatedView(Supplier<T> animatedView) {
        T a = animatedView.get();
        animatedViews.add(a);
        drawableViews.add(a);
        return a;
    }

    /** {@inheritDoc} */
    @Override
    public void onEntityAdded(Entity entity) {
        AnimatedEntityView view;

        if (entity instanceof Tank tank) {
            view = registerAnimatedView(() -> new AnimatedEntityView(tank, "images/tank_blue.png", 0.4f));
            Drawble decorated = decorateWithHealthBar(view, tank);
            entityDrawables.put(entity, decorated);
        } else if (entity instanceof Tree tree) {
            view = registerAnimatedView(() -> new AnimatedEntityView(tree, "images/greenTree.png", 0f));
            entityDrawables.put(entity, view);
        } else if (entity instanceof Bullet bullet) {
            view = registerAnimatedView(() -> new AnimatedEntityView(bullet, bulletTexture, 0.1f));
            entityDrawables.put(entity, view);
        } else {
            return;
        }

        entityViews.put(entity, view);
    }

    /** {@inheritDoc} */
    @Override
    public void onEntityRemoved(Entity entity) {
        Drawble drawable = entityDrawables.remove(entity);
        if (drawable != null) {
            drawableViews.remove(drawable);
        }

        AnimatedEntityView view = entityViews.remove(entity);
        if (view != null) {
            animatedViews.remove(view);
        }

        if (entity instanceof Tank tank) {
            if (tank == tankEntity) {
                tankEntity = null;
            } else {
                enemyTanks.remove(tank);
            }
        }
    }

    /** */
    private Drawble decorateWithHealthBar(AnimatedEntityView view, Healtable healtable) {
        Drawble decoratedView = new HealthBarDecorator(view, healtable, healthBarManager);
        int index = drawableViews.indexOf(view);

        if (index >= 0)
            drawableViews.set(index, decoratedView);
        else
            drawableViews.add(decoratedView);

        return decoratedView;
    }

    /** */
    private Texture createBulletTexture() {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1f, 0.8f, 0f, 1f));
        pixmap.fillCircle(8, 8, 7);
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.drawCircle(8, 8, 6);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
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
