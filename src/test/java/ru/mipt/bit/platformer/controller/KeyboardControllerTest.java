package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeyboardControllerTest {
    /** Keyboard controller. */
    private KeyboardController keyboardController;

    /** Test entity. */
    private Entity entity;

    /** Obstacles manager. */
    private ObstaclesManager obstaclesManager;

    private CommandManager commandManager;
    private InternalContext context;

    @BeforeEach
    void setUp() {
        context = new InternalContext();
        commandManager = new CommandManager(context);
        obstaclesManager = new ObstaclesManagerImpl(5, 5, context);
        entity = new Entity(new GridPoint2(0, 0));
        obstaclesManager.addObstacle(entity);
        obstaclesManager.addObstacle(new Entity(new GridPoint2(1, 0)));
        keyboardController = new KeyboardController(context);

        com.badlogic.gdx.Input inputMock = mock(com.badlogic.gdx.Input.class);
        Gdx.input = inputMock;
    }

    @Test
    void testMove() {
        when(Gdx.input.isKeyPressed(Input.Keys.W)).thenReturn(true);

        keyboardController.update(entity);
        commandManager.executeAll();

        assertTrue(entity.isMoving());
        assertEquals(Direction.UP, entity.getDirection());
        assertEquals(new GridPoint2(0, 1), entity.getDestination());
    }

    @Test
    void testMoveToObstacle() {
        when(Gdx.input.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

        keyboardController.update(entity);
        commandManager.executeAll();

        assertFalse(entity.isMoving());
        assertEquals(new GridPoint2(0, 0), entity.getDestination());
    }
}
