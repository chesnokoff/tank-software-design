package ru.mipt.bit.platformer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mipt.bit.platformer.model.level.LevelInfo;

/** */
public final class GameContextFactory {
    private GameContextFactory() {}

    /** */
    public static AnnotationConfigApplicationContext createApplicationContext(LevelInfo levelInfo) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(RuntimeLevelConfiguration.class, () -> new RuntimeLevelConfiguration(levelInfo));
        context.register(GameConfiguration.class);
        context.refresh();
        return context;
    }
}

