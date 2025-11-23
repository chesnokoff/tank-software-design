package ru.mipt.bit.platformer;

import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.mipt.bit.platformer.command.CommandManager;
import ru.mipt.bit.platformer.controller.KeyboardController;
import ru.mipt.bit.platformer.controller.RandomController;
import ru.mipt.bit.platformer.config.GameProperties;
import ru.mipt.bit.platformer.config.PropertiesConfiguration;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.ObstaclesManagerImpl;
import ru.mipt.bit.platformer.model.level.GameLevel;
import ru.mipt.bit.platformer.view.HealthBarManager;

/** */
@Configuration
@Import(PropertiesConfiguration.class)
public class GameConfiguration {
    @Bean
    public CommandManager commandManager() {
        return new CommandManager();
    }

    @Bean
    public HealthBarManager healthBarManager() {
        return new HealthBarManager();
    }

    @Bean
    public ObstaclesManager obstaclesManager(LevelDimensions levelDimensions) {
        return new ObstaclesManagerImpl(levelDimensions.levelWidth(), levelDimensions.levelHeight());
    }

    @Bean
    public GameLevel gameLevel(ObstaclesManager obstaclesManager, GameProperties properties) {
        return new GameLevel(obstaclesManager, properties.getBullet().getDamage());
    }

    @Bean
    public KeyboardController keyboardController(
        CommandManager commandManager,
        HealthBarManager healthBarManager,
        GameLevel gameLevel,
        ObstaclesManager obstaclesManager
    ) {
        return new KeyboardController(commandManager, healthBarManager, gameLevel, obstaclesManager);
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public RandomController randomController(
        Random random,
        CommandManager commandManager,
        GameLevel gameLevel,
        ObstaclesManager obstaclesManager,
        GameProperties properties
    ) {
        return new RandomController(
            random,
            properties.getController().getRandom().getFireProbability(),
            commandManager,
            gameLevel,
            obstaclesManager
        );
    }
}
