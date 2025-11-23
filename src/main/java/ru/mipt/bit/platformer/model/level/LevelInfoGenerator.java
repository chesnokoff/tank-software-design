package ru.mipt.bit.platformer.model.level;

/**
 * Interface for level loading strategies
 */
public interface LevelInfoGenerator {
    /** Generates level info and returns {@link LevelInfo} object */
    LevelInfo generate();
}
