package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TankProperties {
    @Value("${game.tank.max-health}")
    private int maxHealth;

    public int getMaxHealth() {
        return maxHealth;
    }
}
