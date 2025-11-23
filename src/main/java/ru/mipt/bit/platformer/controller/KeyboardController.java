package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.Map;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.command.FireCommand;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ToggleHealthBarsCommand;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.view.HealthBarManager;
import ru.mipt.bit.platformer.model.level.GameLevel;

/** */
public class KeyboardController implements InputController {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(KeyboardController.class);

    /** Key map. */
    private final Map<Integer, Direction> keyMap;

    /** */
    private final CommandManager commandManager;

    /** */
    private final HealthBarManager healthBarManager;

    /** */
    private final GameLevel gameLevel;

    /** */
    private final ObstaclesManager obstaclesManager;

    /**
     * @param keyMap Key map.
     * @param commandManager Command manager.
     * @param healthBarManager Health bar manager.
     * @param gameLevel Game level.
     * @param obstaclesManager Obstacles manager.
     */
    public KeyboardController(
        Map<Integer, Direction> keyMap,
        CommandManager commandManager,
        HealthBarManager healthBarManager,
        GameLevel gameLevel,
        ObstaclesManager obstaclesManager
    ) {
        if (keyMap == null) {
            throw new IllegalArgumentException("keyMap");
        }

        this.keyMap = keyMap;
        this.commandManager = commandManager;
        this.healthBarManager = healthBarManager;
        this.gameLevel = gameLevel;
        this.obstaclesManager = obstaclesManager;
    }

    /**
     * @param commandManager Command manager.
     * @param healthBarManager Health bar manager.
     * @param gameLevel Game level.
     * @param obstaclesManager Obstacles manager.
     */
    public KeyboardController(
        CommandManager commandManager,
        HealthBarManager healthBarManager,
        GameLevel gameLevel,
        ObstaclesManager obstaclesManager
    ) {
        this(
            Map.of(
                Input.Keys.W, Direction.UP,
                Input.Keys.UP, Direction.UP,
                Input.Keys.S, Direction.DOWN,
                Input.Keys.DOWN, Direction.DOWN,
                Input.Keys.A, Direction.LEFT,
                Input.Keys.LEFT, Direction.LEFT,
                Input.Keys.D, Direction.RIGHT,
                Input.Keys.RIGHT, Direction.RIGHT
            ),
            commandManager,
            healthBarManager,
            gameLevel,
            obstaclesManager
        );
    }

    /** {@inheritDoc} */
    @Override
    public void update(Entity entity) {
        if (entity == null) {
            return;
        }

        boolean anyKeyPressed = false;

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            logger.debug("Toggle health bars key pressed");
            commandManager.submit(new ToggleHealthBarsCommand(healthBarManager));
        }

        if (entity instanceof Tank tank && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            logger.debug("Fire key pressed");
            commandManager.submit(new FireCommand(tank, gameLevel));
        }

        for (Map.Entry<Integer, Direction> entry : keyMap.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                logger.debug("Key {} pressed, direction: {}", entry.getKey(), entry.getValue());
                commandManager.submit(new MoveCommand(entity, entry.getValue(), obstaclesManager));
                anyKeyPressed = true;
            }
        }

        if (!anyKeyPressed) {
            logger.debug("No movement keys pressed");
        }
    }
}
