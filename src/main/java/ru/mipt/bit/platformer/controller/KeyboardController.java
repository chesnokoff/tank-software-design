package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.Map;
import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.command.FireCommand;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ToggleHealthBarsCommand;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.view.HealthBarManager;

/** */
public class KeyboardController implements InputController {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(KeyboardController.class);

    /** Key map. */
    private final Map<Integer, Direction> keyMap;

    /** Context. */
    private final InternalContext context;

    /**
     * @param keyMap Key map.
     * @param context Context.
     */
    public KeyboardController(Map<Integer, Direction> keyMap, InternalContext context) {
        if (keyMap == null) {
            throw new IllegalArgumentException("keyMap");
        }

        if (context == null) {
            throw new IllegalArgumentException("context");
        }

        this.keyMap = keyMap;
        this.context = context;
    }

    /**
     * @param context Context.
     */
    public KeyboardController(InternalContext context) {
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
            context
        );
    }

    /** {@inheritDoc} */
    @Override
    public void update(Entity entity) {
        if (entity == null) {
            return;
        }

        boolean anyKeyPressed = false;

        CommandManager commandManager = context.get(CommandManager.class);

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            logger.debug("Toggle health bars key pressed");
            commandManager.submit(new ToggleHealthBarsCommand(context.get(HealthBarManager.class)));
        }

        if (entity instanceof Tank tank && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            logger.debug("Fire key pressed");
            commandManager.submit(new FireCommand(tank, context));
        }

        for (Map.Entry<Integer, Direction> entry : keyMap.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                logger.debug("Key {} pressed, direction: {}", entry.getKey(), entry.getValue());
                commandManager.submit(new MoveCommand(entity, entry.getValue(), context));
                anyKeyPressed = true;
            }
        }

        if (!anyKeyPressed) {
            logger.debug("No movement keys pressed");
        }
    }
}
