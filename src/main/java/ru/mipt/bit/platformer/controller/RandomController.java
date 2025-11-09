package ru.mipt.bit.platformer.controller;

import java.util.Random;
import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;

/** */
public class RandomController implements InputController {
    /** */
    private final Random random;

    /** */
    private final InternalContext context;

    /** */
    public RandomController(InternalContext context) {
        this(new Random(), context);
    }

    /** */
    private RandomController(Random random, InternalContext context) {
        this.random = random;
        this.context = context;
    }

    /** {@inheritDoc} */
    @Override public void update(Entity entity) {
        if (entity.isMoving())
            return;

        Direction direction = getRandomDirection();
        context.get(CommandManager.class).submit(new MoveCommand(entity, direction, context));
    }

    private Direction getRandomDirection() {
        Direction[] values = Direction.values();
        return values[random.nextInt(values.length)];
    }
}
