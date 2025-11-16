package ru.mipt.bit.platformer.model;

/** */
public interface Healtable {
    /** */
    int getHealth();

    /** */
    int getMaxHealth();

    /** */
    void setHealth(int health);

    /** */
    default void takeDamage(int damage) {
        if (damage <= 0) {
            return;
        }

        setHealth(Math.max(0, getHealth() - damage));
    }

    /** */
    default boolean isAlive() {
        return getHealth() > 0;
    }
}
