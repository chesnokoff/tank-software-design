package ru.mipt.bit.platformer.config;

import org.springframework.stereotype.Component;

@Component
public class ControllerProperties {
    private final RandomControllerProperties random;

    public ControllerProperties(RandomControllerProperties random) {
        this.random = random;
    }

    public RandomControllerProperties getRandom() {
        return random;
    }
}
