package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.log.GameLogger;

public class ObstaclesManager {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(ObstaclesManager.class);

    /** Obstacles. */
    private final List<Entity> obstacles = new ArrayList<>();

    /**
     * @param obstacle Obstacle to add.
     */
    public void addObstacle(Entity obstacle) {
        if (obstacle == null) {
            throw new IllegalArgumentException("Obstacle can't be null");
        }

        obstacles.add(obstacle);
    }

    /** Checks if position is free. */
    public boolean isPositionFree(GridPoint2 position) {
        return obstacles.stream().noneMatch(obstacle -> obstacle.getPosition().equals(position));
    }
}
