package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AnimationProperties {
    @Value("${game.animation.tank-speed}")
    private float tankSpeed;

    @Value("${game.animation.bullet-speed}")
    private float bulletSpeed;

    @Value("${game.animation.tree-speed}")
    private float treeSpeed;

    public float getTankSpeed() {
        return tankSpeed;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public float getTreeSpeed() {
        return treeSpeed;
    }
}
