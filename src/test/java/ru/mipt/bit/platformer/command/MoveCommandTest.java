package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveCommandTest {
    private Entity entity;
    private ObstaclesManager obstaclesManager;
    private InternalContext context;

    @BeforeEach
    void setUp() {
        context = new InternalContext();
        entity = new Entity(new GridPoint2(2, 2));
        obstaclesManager = new ObstaclesManagerImpl(5, 5, context);
        obstaclesManager.addObstacle(entity);
    }

    @Test
    void executeStartsMovement() {
        Command command = new MoveCommand(entity, Direction.LEFT, context);

        command.execute();

        assertTrue(entity.isMoving());
        assertEquals(Direction.LEFT, entity.getDirection());
        assertEquals(new GridPoint2(1, 2), entity.getDestination());
    }
}
