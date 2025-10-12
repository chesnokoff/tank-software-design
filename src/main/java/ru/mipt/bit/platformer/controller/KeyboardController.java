package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.Direction;

import java.util.Map;

import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;

/** */
public class KeyboardController implements InputController {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(KeyboardController.class);

    /** Key map. */
    private static final Map<Integer, Direction> keyMap = Map.of(
            Input.Keys.W, Direction.UP,
            Input.Keys.UP, Direction.UP,
            Input.Keys.S, Direction.DOWN,
            Input.Keys.DOWN, Direction.DOWN,
            Input.Keys.A, Direction.LEFT,
            Input.Keys.LEFT, Direction.LEFT,
            Input.Keys.D, Direction.RIGHT,
            Input.Keys.RIGHT, Direction.RIGHT
    );

    /** {@inheritDoc} */
    @Override
    public void update(Entity entity, ObstaclesManager obstaclesManager) {
        boolean anyKeyPressed = false;

        for (Map.Entry<Integer, Direction> entry : keyMap.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                logger.debug("Key {} pressed, direction: {}", entry.getKey(), entry.getValue());
                entity.move(entry.getValue(), obstaclesManager);
                anyKeyPressed = true;
            }
        }

        if (!anyKeyPressed) {
            logger.debug("No movement keys pressed");
        }
    }
}
