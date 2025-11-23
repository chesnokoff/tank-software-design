package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.level.GameLevel;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomControllerTest {
    private RandomController controller;
    private Entity entity;
    private ObstaclesManager obstaclesManager;
    private CommandManager commandManager;
    private GameLevel gameLevel;

    @BeforeEach
    void setUp() {
        commandManager = new CommandManager();
        obstaclesManager = new ObstaclesManagerImpl(4, 4);
        gameLevel = new GameLevel(obstaclesManager);
        entity = new Tank(new GridPoint2(1, 1));
        obstaclesManager.addObstacle(entity);
        controller = new RandomController(new Random(0), commandManager, gameLevel, obstaclesManager);
    }

    @Test
    void testMoveCommand() {
        controller.update(entity);
        commandManager.executeAll();

        assertTrue(entity.isMoving());
    }
}
