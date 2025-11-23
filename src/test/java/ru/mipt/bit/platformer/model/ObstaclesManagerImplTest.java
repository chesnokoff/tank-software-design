package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObstaclesManagerImplTest {
    @Test
    void testFreePosition() {
        ObstaclesManager manager = new ObstaclesManagerImpl(3, 3);

        assertFalse(manager.isPositionFree(new GridPoint2(-1, 0)));
        assertFalse(manager.isPositionFree(new GridPoint2(3, 1)));
    }

    @Test
    void testMovingEntity() {
        ObstaclesManagerImpl manager = new ObstaclesManagerImpl(4, 4);
        Entity entity = new Entity(new GridPoint2(1, 1));
        manager.addObstacle(entity);

        assertTrue(manager.isPositionFree(new GridPoint2(2, 1)));

        entity.move(Direction.RIGHT, manager);

        assertFalse(manager.isPositionFree(new GridPoint2(1, 1)));
        assertFalse(manager.isPositionFree(new GridPoint2(2, 1)));
    }
}
