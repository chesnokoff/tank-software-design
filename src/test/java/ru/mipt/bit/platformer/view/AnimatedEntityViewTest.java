package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AnimatedEntityViewTest {

    /** Entity. */
    private Entity entity;

    /** Level. */
    private TiledLevel level;

    /** View. */
    private AnimatedEntityView view;

    @BeforeEach
    void setUp() {
        entity = new Entity(new GridPoint2(0, 0));
        entity.move(Direction.RIGHT, new ObstaclesManager());
        level = mock(TiledLevel.class);

        when(level.calculateTileCenter(new GridPoint2(0, 0))).thenReturn(new Vector2(0, 0));
        when(level.calculateTileCenter(new GridPoint2(1, 0))).thenReturn(new Vector2(128, 0));

        view = new AnimatedEntityView(entity, "/Users/aleksandr/IdeaProjects/tank-software-design/src/test/resources/images/tank_blue.png", 0.1f);
    }

    @Test
    void testUpdate() {
        view.update(0.05f, level);

        assertTrue(entity.isMoving());

        view.update(5f, level);

        assertFalse(entity.isMoving());
    }
}
