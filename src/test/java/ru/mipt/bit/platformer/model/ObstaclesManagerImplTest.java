package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.InternalContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObstaclesManagerImplTest {
    @Test
    void testFreePosition() {
        InternalContext context = new InternalContext();
        ObstaclesManager manager = new ObstaclesManagerImpl(3, 3, context);

        assertFalse(manager.isPositionFree(new GridPoint2(-1, 0)));
        assertFalse(manager.isPositionFree(new GridPoint2(3, 1)));
    }

    @Test
    void testMovingEntity() {
        InternalContext context = new InternalContext();
        ObstaclesManagerImpl manager = new ObstaclesManagerImpl(4, 4, context);
        Entity entity = new Entity(new GridPoint2(1, 1));
        manager.addObstacle(entity);

        assertTrue(manager.isPositionFree(new GridPoint2(2, 1)));

        entity.move(Direction.RIGHT, manager);

        assertFalse(manager.isPositionFree(new GridPoint2(1, 1)));
        assertFalse(manager.isPositionFree(new GridPoint2(2, 1)));
    }
}
