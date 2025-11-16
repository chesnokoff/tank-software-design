package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.level.GameLevel;

/** */
public class FireCommand implements Command {
    /** */
    private final Tank shooter;

    /** */
    private final InternalContext context;

    /**
     * @param shooter Shooter.
     * @param context Context.
     */
    public FireCommand(Tank shooter, InternalContext context) {
        this.shooter = shooter;
        this.context = context;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        GameLevel level = context.get(GameLevel.class);
        if (level == null) {
            return;
        }

        level.fire(shooter);
    }
}
