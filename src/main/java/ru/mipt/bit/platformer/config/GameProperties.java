package ru.mipt.bit.platformer.config;

import org.springframework.stereotype.Component;

@Component
public class GameProperties {
    private final WindowProperties window;
    private final AnimationProperties animation;
    private final ControllerProperties controller;
    private final TankProperties tank;
    private final BulletProperties bullet;

    public GameProperties(
        WindowProperties window,
        AnimationProperties animation,
        ControllerProperties controller,
        TankProperties tank,
        BulletProperties bullet
    ) {
        this.window = window;
        this.animation = animation;
        this.controller = controller;
        this.tank = tank;
        this.bullet = bullet;
    }

    public WindowProperties getWindow() {
        return window;
    }

    public AnimationProperties getAnimation() {
        return animation;
    }

    public ControllerProperties getController() {
        return controller;
    }

    public TankProperties getTank() {
        return tank;
    }

    public BulletProperties getBullet() {
        return bullet;
    }
}
