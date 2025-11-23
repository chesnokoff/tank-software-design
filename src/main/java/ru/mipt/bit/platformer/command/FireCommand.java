package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.level.GameLevel;

/** */
public class FireCommand implements Command {
    /** */
    private final Tank shooter;

    /** */
    private final GameLevel gameLevel;

    /**
     * @param shooter Shooter.
     * @param gameLevel Level.
     */
    public FireCommand(Tank shooter, GameLevel gameLevel) {
        this.shooter = shooter;
        this.gameLevel = gameLevel;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        gameLevel.fire(shooter);
    }
}
