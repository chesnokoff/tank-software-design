package ru.mipt.bit.platformer.controller;

import java.util.Random;
import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.command.FireCommand;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.Tank;

/** */
public class RandomController implements InputController {
    /** Probability to shoot instead of moving. */
    private static final float FIRE_PROBABILITY = 0.15f;

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
        if (!(entity instanceof Tank tank)) {
            return;
        }

        if (tank.isMoving())
            return;

        CommandManager commandManager = context.get(CommandManager.class);

        if (random.nextFloat() < FIRE_PROBABILITY) {
            commandManager.submit(new FireCommand(tank, context));
            return;
        }

        Direction direction = getRandomDirection();
        commandManager.submit(new MoveCommand(tank, direction, context));
    }

    private Direction getRandomDirection() {
        Direction[] values = Direction.values();
        return values[random.nextInt(values.length)];
    }
}
