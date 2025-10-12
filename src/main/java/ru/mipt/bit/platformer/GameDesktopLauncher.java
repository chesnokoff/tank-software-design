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
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.controller.InputController;
import ru.mipt.bit.platformer.controller.KeyboardController;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.view.AnimatedEntityView;
import ru.mipt.bit.platformer.view.TiledLevel;

/** */
public class GameDesktopLauncher implements ApplicationListener {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(GameDesktopLauncher.class);

    /** Window width. */
    private static final int WINDOW_WIDTH = 1280;

    /** Window height. */
    private static final int WINDOW_HEIGHT = 1024;

    /** Batch. */
    private Batch batch;

    /** Tiled level. */
    private TiledLevel tiledLevel;

    /** Tank entity. */
    private Tank tankEntity;

    /** Keyboard handler. */
    private InputController keyboardController;

    /** Disposables. */
    private final List<Disposable> disposables = new ArrayList<>();

    /** Animated views. */
    private final List<AnimatedEntityView> animatedViews = new ArrayList<>();

    /** Obstacles manager. */
    private ObstaclesManager obstaclesManager;

    /** {@inheritDoc} */
    @Override
    public void create() {
        logger.info("Start initializing game.");

        batch = registerDisposable(SpriteBatch::new);

        obstaclesManager = new ObstaclesManagerImpl();

        tiledLevel = registerDisposable(() -> new TiledLevel(batch, "level.tmx"));

        tankEntity = registerEntity(() -> new Tank(new GridPoint2(1, 1)));

        Entity treeEntity = registerEntity(() -> new Tree(new GridPoint2(1, 3)));

        registerAnimatedView(() -> new AnimatedEntityView(tankEntity, "images/tank_blue.png", 0.4f));

        registerAnimatedView(() -> new AnimatedEntityView(treeEntity, "images/greenTree.png", 0f));

        logger.info("Views initialized");

        keyboardController = new KeyboardController();

        logger.info("Game initialization completed successfully");
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        clearScreen();

        float deltaTime = Gdx.graphics.getDeltaTime();
        logger.debug("Delta time: {}", deltaTime);

        keyboardController.update(tankEntity, obstaclesManager);

        animatedViews.forEach(view -> view.update(deltaTime, tiledLevel));

        tiledLevel.render();

        batch.begin();
        animatedViews.forEach(view -> view.draw(batch));
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
        obstaclesManager.addObstacle(e);
        return e;
    }

    /**
     * @param animatedView Disposable to register.
     */
    public <T extends AnimatedEntityView> T registerAnimatedView(Supplier<T> animatedView) {
        T a = animatedView.get();
        animatedViews.add(a);
        return a;
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
