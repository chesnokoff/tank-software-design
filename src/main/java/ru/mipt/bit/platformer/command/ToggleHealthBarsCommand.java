package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.view.HealthBarManager;

/** */
public class ToggleHealthBarsCommand implements Command {
    /** */
    private final HealthBarManager healthBarManager;

    /**
     * @param healthBarManager Health bar manager.
     */
    public ToggleHealthBarsCommand(HealthBarManager healthBarManager) {
        this.healthBarManager = healthBarManager;
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        healthBarManager.toggle();
    }
}
