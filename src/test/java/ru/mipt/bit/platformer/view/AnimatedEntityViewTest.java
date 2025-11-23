package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnimatedEntityViewTest {

    /** Entity. */
    private Entity entity;

    /** Level. */
    private TiledLevel level;

    /** View. */
    private AnimatedEntityView view;

    @BeforeAll
    static void enableByteBuddyExperimental() {
        System.setProperty("net.bytebuddy.experimental", "true");
    }

    @BeforeEach
    void setUp() {
        entity = new Entity(new GridPoint2(0, 0));
        ObstaclesManagerImpl manager = new ObstaclesManagerImpl(5, 5);
        manager.addObstacle(entity);
        entity.move(Direction.RIGHT, manager);

        level = mock(TiledLevel.class);
        when(level.calculateTileCenter(any(GridPoint2.class))).thenAnswer(invocation -> {
            GridPoint2 point = invocation.getArgument(0, GridPoint2.class);
            return new Vector2(point.x * 128, point.y * 128);
        });

        Texture texture = mock(Texture.class);
        when(texture.getWidth()).thenReturn(128);
        when(texture.getHeight()).thenReturn(128);

        view = new AnimatedEntityView(entity, texture, 0.1f);
    }

    @Test
    void testUpdate() {
        view.update(0.05f, level);

        assertTrue(entity.isMoving());

        view.update(5f, level);

        assertFalse(entity.isMoving());
    }
}
