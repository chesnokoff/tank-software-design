package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RandomControllerProperties {
    @Value("${game.controller.random.fire-probability}")
    private float fireProbability;

    public float getFireProbability() {
        return fireProbability;
    }
}
