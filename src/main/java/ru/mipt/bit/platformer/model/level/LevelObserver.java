package ru.mipt.bit.platformer.model.level;

import ru.mipt.bit.platformer.model.Entity;

/** */
public interface LevelObserver {
    /** */
    void onEntityAdded(Entity entity);

    /** */
    void onEntityRemoved(Entity entity);
}
