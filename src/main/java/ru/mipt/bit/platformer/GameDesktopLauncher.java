package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.input.KeyboardListener;
import ru.mipt.bit.platformer.roles.Tank;
import ru.mipt.bit.platformer.roles.Tree;
import ru.mipt.bit.platformer.ui.MovableTexturedItem;
import ru.mipt.bit.platformer.ui.TiledLevel;
import ru.mipt.bit.platformer.ui.TexturedItem;
import ru.mipt.bit.platformer.ui.TileMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/** Launcher for the game. */
public class GameDesktopLauncher implements ApplicationListener {
    /** Level width: 10 tiles x 128px. */
    private static final int WINDOW_WIDTH = 1280;

    /** Level height: 8 tiles x 128px. */
    private static final int WINDOW_HEIGHT = 1024;

    /** Tiled level. */
    private TiledLevel tiledLevel;

    /** Batch. */
    private Batch batch;

    /** Tile movement. */
    private TileMovement tileMovement;

    /** Textured items. */
    private final List<TexturedItem> texturedItems = new ArrayList<>();

    /** Disposables. */
    private final List<Disposable> disposables = new ArrayList<>();

    /** Tree obstacle. */
    private TexturedItem treeObstacle;

    /** Player. */
    private MovableTexturedItem player;

    /** Keyboard listener. */
    private final KeyboardListener keyboardListener = new KeyboardListener();

    @Override
    public void create() {
        batch = registerDisposable(SpriteBatch::new);

        // load level tiles
        tiledLevel = registerDisposable(() -> new TiledLevel(batch, "level.tmx"));

        tileMovement = new TileMovement(tiledLevel, Interpolation.smooth);

        player = registerTexturedItem(Tank::new);

        treeObstacle = registerTexturedItem(Tree::new);

        tileMovement.locateItemAtTileCenterImmediately(treeObstacle, new GridPoint2(1, 3));
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        keyboardListener.processKey(player, List.of(treeObstacle));

        // calculate interpolated player screen coordinates
        tileMovement.moveItemBetweenTileCenters(player);

        player.continueProgress(deltaTime);

        tiledLevel.render();

        recordDrawCommands();
    }

    private void recordDrawCommands() {
        batch.begin();
        texturedItems.forEach(item -> item.draw(batch));
        batch.end();
    }

    private <T extends TexturedItem> T registerTexturedItem(Supplier<T> itemSupplier) {
        T item = itemSupplier.get();
        texturedItems.add(item);

        registerDisposable(() -> item);

        return item;
    }

    private <T extends Disposable> T registerDisposable(Supplier<T> disposableSupplier) {
        T disposable = disposableSupplier.get();
        disposables.add(disposable);
        return disposable;
    }



    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        texturedItems.forEach(Disposable::dispose);
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
