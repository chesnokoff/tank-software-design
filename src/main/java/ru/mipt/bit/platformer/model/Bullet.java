package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.level.GameLevel;

/** */
public class Bullet extends Entity {
    /** */
    public static final int DEFAULT_DAMAGE = 25;

    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(Bullet.class);

    /** */
    private final int damage;

    /** */
    private final GameLevel level;

    /** */
    private final ObstaclesManager obstaclesManager;

    /**
     * @param position starting position
     * @param direction movement direction
     * @param level logical level
     * @param obstaclesManager obstacles manager
     */
    public Bullet(
            GridPoint2 position,
            Direction direction,
            GameLevel level,
            ObstaclesManager obstaclesManager
    ) {
        this(position, direction, DEFAULT_DAMAGE, level, obstaclesManager);
    }

    /**
     * @param position starting position
     * @param direction movement direction
     * @param damage damage value
     * @param level logical level
     * @param obstaclesManager obstacles manager
     */
    public Bullet(
            GridPoint2 position,
            Direction direction,
            int damage,
            GameLevel level,
            ObstaclesManager obstaclesManager
    ) {
        super(position);
        this.damage = damage;
        this.level = level;
        this.obstaclesManager = obstaclesManager;
        setDirection(direction);
    }

    /** {@inheritDoc} */
    @Override
    public void update(float tickDuration) {
        if (isMoving()) {
            return;
        }

        Direction direction = getDirection();
        GridPoint2 nextCell = getPosition().add(direction.dx, direction.dy);

        if (!obstaclesManager.isPositionFree(nextCell)) {
            handleCollision(nextCell);
            return;
        }

        boolean startedMoving = move(direction, obstaclesManager);
        if (!startedMoving) {
            logger.debug("Bullet can't move to {} and will be removed", nextCell);
            level.removeEntity(this);
        }
    }

    /**
     * @param nextCell Next cell.
     */
    private void handleCollision(GridPoint2 nextCell) {
        Obstacle value = obstaclesManager.findObstacle(nextCell);

        if (value != null) {
            if (value instanceof Tank tank) {
                level.damageTank(tank, damage);
            } else if (value instanceof Bullet otherBullet && otherBullet != this) {
                level.removeEntity(otherBullet);
            }
        }

        level.removeEntity(this);
    }
}
