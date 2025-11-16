package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomControllerTest {
    private InternalContext context;
    private RandomController controller;
    private Entity entity;
    private ObstaclesManager obstaclesManager;
    private CommandManager commandManager;

    @BeforeEach
    void setUp() {
        context = new InternalContext();
        commandManager = new CommandManager(context);
        obstaclesManager = new ObstaclesManagerImpl(4, 4, context);
        entity = new Entity(new GridPoint2(1, 1));
        obstaclesManager.addObstacle(entity);
        controller = new RandomController(context);
    }

    @Test
    void testMoveCommand() {
        controller.update(entity);
        commandManager.executeAll();

        assertTrue(entity.isMoving());
    }
}
