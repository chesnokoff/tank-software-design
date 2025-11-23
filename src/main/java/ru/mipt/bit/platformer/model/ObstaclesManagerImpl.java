package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.log.GameLogger;

/** */
public class ObstaclesManagerImpl implements ObstaclesManager {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(ObstaclesManagerImpl.class);

    /** Obstacles. */
    private final List<Obstacle> obstacles = new ArrayList<>();

    /** Level width. */
    private final int levelWidth;

    /** Level height. */
    private final int levelHeight;

    /**
     * @param levelWidth Level width.
     * @param levelHeight Level height.
     */
    public ObstaclesManagerImpl(int levelWidth, int levelHeight) {
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }

    /**
     * @param obstacle Obstacle to add.
     */
    @Override public void addObstacle(Obstacle obstacle) {
        if (obstacle == null) {
            throw new IllegalArgumentException("Obstacle can't be null");
        }

        obstacles.add(obstacle);
    }

    /** Checks if position is free. */
    @Override public boolean isPositionFree(GridPoint2 position) {
        if (!isInsideBounds(position))
            return false;

        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(position))
                return false;

            if (obstacle instanceof Entity entity && entity.isMoving() && entity.getDestination().equals(position))
                return false;
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public void removeObstacle(Obstacle obstacle) {
        obstacles.remove(obstacle);
    }

    /** {@inheritDoc} */
    @Override public Obstacle findObstacle(GridPoint2 position) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(position)) {
                return obstacle;
            }

            if (obstacle instanceof Entity entity && entity.isMoving() && entity.getDestination().equals(position)) {
                return obstacle;
            }
        }

        return null;
    }

    /**
     * @param position Position.
     */
    private boolean isInsideBounds(GridPoint2 position) {
        return position.x >= 0 && position.x < levelWidth && position.y >= 0 && position.y < levelHeight;
    }
}
