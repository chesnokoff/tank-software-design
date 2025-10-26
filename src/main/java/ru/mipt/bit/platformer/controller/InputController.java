package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.ObstaclesManager;

public interface InputController {
    /**
     * @param entity           Entity.
     * @param obstaclesManager ObstaclesManager.
     */
    void update(Entity entity, ObstaclesManager obstaclesManager);
}
