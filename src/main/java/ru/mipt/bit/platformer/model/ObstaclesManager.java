package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface ObstaclesManager {
    void addObstacle(Obstacle obstacle);

    boolean isPositionFree(GridPoint2 position);
}
