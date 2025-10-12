package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** */
class EntityTest {
    /** Entity. */
    private Entity entity;

    /** Obstacles manager. */
    private ObstaclesManager obstaclesManager;

    /** Initial position. */
    private GridPoint2 initialPosition;

    /** */
    @BeforeEach
    void setUp() {
        initialPosition = new GridPoint2(2, 2);
        entity = new Entity(initialPosition);
        obstaclesManager = new ObstaclesManager();
    }

    /** */
    @Test
    void testInit() {
        assertEquals(initialPosition, entity.getPosition());
        assertEquals(initialPosition, entity.getDestination());
        assertEquals(Direction.UP, entity.getDirection());
        assertFalse(entity.isMoving());
    }

    /** */
    @Test
    void testMove() {
        boolean moved = entity.move(Direction.RIGHT, obstaclesManager);

        assertTrue(moved);
        assertTrue(entity.isMoving());

        assertEquals(new GridPoint2(3, 2), entity.getDestination());
        assertEquals(Direction.RIGHT, entity.getDirection());

        assertFalse(entity.move(Direction.RIGHT, obstaclesManager));
        assertEquals(new GridPoint2(3, 2), entity.getDestination());
    }

    /** */
    @Test
    void getPositionShouldReturnCopy() {
        GridPoint2 position = entity.getPosition();
        position.set(5, 5);

        assertEquals(initialPosition, entity.getPosition());
    }

    /** */
    @Test
    void getDestinationShouldReturnCopy() {
        GridPoint2 destination = entity.getDestination();
        destination.set(5, 5);

        assertEquals(initialPosition, entity.getDestination());
    }
}