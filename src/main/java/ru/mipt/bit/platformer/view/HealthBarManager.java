package ru.mipt.bit.platformer.view;

import ru.mipt.bit.platformer.InternalContext;

/** Manages visibility of health bars. */
public class HealthBarManager {
    /** */
    private boolean visible;

    /**
     * @param context Context.
     */
    public HealthBarManager(InternalContext context) {
        context.register(HealthBarManager.class, this);
    }

    /** */
    public void toggle() {
        visible = !visible;
    }

    /** */
    public boolean isVisible() {
        return visible;
    }
}
