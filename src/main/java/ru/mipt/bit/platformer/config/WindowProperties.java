package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WindowProperties {
    @Value("${game.window.width}")
    private int width;

    @Value("${game.window.height}")
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
