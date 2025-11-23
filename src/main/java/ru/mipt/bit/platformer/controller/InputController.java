package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Entity;

public interface InputController {
    /**
     * @param entity Entity.
     */
    void update(Entity entity);
}
