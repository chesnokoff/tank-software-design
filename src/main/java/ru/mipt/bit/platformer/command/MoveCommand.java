package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;

/** */
public class MoveCommand implements Command {
    /** Entity. */
    private final Entity entity;

    /** Direction. */
    private final Direction direction;

    /** */
    private final ObstaclesManager obstaclesManager;

    /** */
    public MoveCommand(Entity entity, Direction direction, ObstaclesManager obstaclesManager) {
        this.entity = entity;
        this.direction = direction;
        this.obstaclesManager = obstaclesManager;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        entity.move(direction, obstaclesManager);
    }
}
