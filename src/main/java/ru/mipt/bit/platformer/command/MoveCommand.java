package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;

/** */
public class MoveCommand implements Command {
    /** Entity. */
    private final Entity entity;

    /** Direction. */
    private final Direction direction;

    /** Context. */
    private final InternalContext context;

    /** */
    public MoveCommand(Entity entity, Direction direction, InternalContext context) {
        this.entity = entity;
        this.direction = direction;
        this.context = context;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        entity.move(direction, context.get(ObstaclesManager.class));
    }
}
