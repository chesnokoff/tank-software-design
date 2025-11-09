package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Tank. */
public class Tank extends Entity implements Healtable {
    /** */
    private static final int DEFAULT_MAX_HEALTH = 100;

    /** */
    private final int maxHealth;

    /** */
    private int health;

    /** */
    public Tank(GridPoint2 pos) {
        this(pos, DEFAULT_MAX_HEALTH);
    }


    /** */
    public Tank(GridPoint2 pos, int maxHealth) {
        super(pos);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    /** {@inheritDoc} */
    @Override
    public int getHealth() {
        return health;
    }

    /** {@inheritDoc} */
    @Override
    public int getMaxHealth() {
        return maxHealth;
    }
}
