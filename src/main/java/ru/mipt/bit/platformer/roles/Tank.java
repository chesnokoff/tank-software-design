package ru.mipt.bit.platformer.roles;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.ui.MovableTexturedItem;

public class Tank extends MovableTexturedItem {
    /** Movement speed. */
    private static final float MOVEMENT_SPEED = 0.4f;

    public Tank() {
        this(new GridPoint2(1, 1));
    }

    public Tank(GridPoint2 position) {
        super("images/tank_blue.png", position, MOVEMENT_SPEED);
    }
}
