package ru.mipt.bit.platformer.controller;

import java.util.Random;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.command.FireCommand;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.level.GameLevel;

/** */
public class RandomController implements InputController {
    /** */
    private final Random random;

    /** */
    private final CommandManager commandManager;

    /** */
    private final GameLevel gameLevel;

    /** */
    private final ObstaclesManager obstaclesManager;

    /** */
    private final float fireProbability;

    public RandomController(Random random, CommandManager commandManager, GameLevel gameLevel, ObstaclesManager obstaclesManager) {
        this(random, 0.15f, commandManager, gameLevel, obstaclesManager);
    }

    /** */
    public RandomController(
        Random random,
        float fireProbability,
        CommandManager commandManager,
        GameLevel gameLevel,
        ObstaclesManager obstaclesManager
    ) {
        this.random = random;
        this.fireProbability = fireProbability;
        this.commandManager = commandManager;
        this.gameLevel = gameLevel;
        this.obstaclesManager = obstaclesManager;
    }

    /** {@inheritDoc} */
    @Override public void update(Entity entity) {
        if (!(entity instanceof Tank tank)) {
            return;
        }

        if (tank.isMoving())
            return;

        if (random.nextFloat() < fireProbability) {
            commandManager.submit(new FireCommand(tank, gameLevel));
            return;
        }

        Direction direction = getRandomDirection();
        commandManager.submit(new MoveCommand(tank, direction, obstaclesManager));
    }

    private Direction getRandomDirection() {
        Direction[] values = Direction.values();
        return values[random.nextInt(values.length)];
    }
}
