package ru.mipt.bit.platformer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mipt.bit.platformer.model.level.LevelInfo;

/** */
@Configuration
public class RuntimeLevelConfiguration {
    private final LevelInfo levelInfo;
    private final LevelDimensions levelDimensions;

    public RuntimeLevelConfiguration(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
        this.levelDimensions = new LevelDimensions(levelInfo.levelWidth(), levelInfo.levelHeight());
    }

    @Bean
    public LevelInfo levelInfo() {
        return levelInfo;
    }

    @Bean
    public LevelDimensions levelDimensions() {
        return levelDimensions;
    }
}
