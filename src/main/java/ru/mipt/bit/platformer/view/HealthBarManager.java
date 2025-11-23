package ru.mipt.bit.platformer.view;

/** Manages visibility of health bars. */
public class HealthBarManager {
    /** */
    private boolean visible;

    /** */
    public void toggle() {
        visible = !visible;
    }

    /** */
    public boolean isVisible() {
        return visible;
    }
}
