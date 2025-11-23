package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BulletProperties {
    @Value("${game.bullet.damage}")
    private int damage;

    public int getDamage() {
        return damage;
    }
}
