package ru.mipt.bit.platformer.model.level;

import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import ru.mipt.bit.platformer.InternalContext;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.ObstaclesManager;
import ru.mipt.bit.platformer.model.Tank;

/** Logical representation of the level. */
public class GameLevel {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(GameLevel.class);

    /** */
    private final List<Entity> entities = new ArrayList<>();

    /** */
    private final List<LevelObserver> observers = new ArrayList<>();

    /** */
    private final ObstaclesManager obstaclesManager;

    public GameLevel(InternalContext context) {
        Objects.requireNonNull(context, "context");
        this.obstaclesManager = context.get(ObstaclesManager.class);
        context.register(GameLevel.class, this);
    }

    /** */
    public <T extends Entity> T addEntity(T entity) {
        entities.add(entity);
        obstaclesManager.addObstacle(entity);
        notifyAdded(entity);
        return entity;
    }

    /** */
    public void removeEntity(Entity entity) {
        if (entity == null) {
            return;
        }

        if (!entities.remove(entity)) {
            return;
        }

        obstaclesManager.removeObstacle(entity);
        notifyRemoved(entity);
    }

    /** */
    public void addObserver(LevelObserver observer) {
        observers.add(observer);
    }

    /** */
    public void update(float tickDuration) {
        List<Entity> snapshot = new ArrayList<>(entities);
        snapshot.forEach(entity -> entity.update(tickDuration));
    }

    /** */
    public void fire(Tank shooter) {
        if (shooter == null || !entities.contains(shooter)) {
            return;
        }

        GridPoint2 spawnPoint = shooter.getPosition().add(shooter.getDirection().dx, shooter.getDirection().dy);
        if (!obstaclesManager.isPositionFree(spawnPoint)) {
            handleImmediateCollision(spawnPoint);
            return;
        }

        Bullet bullet = new Bullet(spawnPoint, shooter.getDirection(), this, obstaclesManager);
        addEntity(bullet);
    }

    /** Applies damage to the tank and removes it when health drops to zero. */
    public void damageTank(Tank target, int damage) {
        if (target == null || !entities.contains(target)) {
            return;
        }

        target.takeDamage(damage);
        logger.debug("Tank {} took {} damage, health {}", target, damage, target.getHealth());
        if (!target.isAlive()) {
            removeEntity(target);
        }
    }

    /**
     * @param position Position.
     */
    private void handleImmediateCollision(GridPoint2 position) {
        Obstacle value = obstaclesManager.findObstacle(position);
        if (value == null) {
            return;
        }
        if (value instanceof Tank tank) {
            damageTank(tank, Bullet.DEFAULT_DAMAGE);
        } else if (value instanceof Bullet bullet) {
            removeEntity(bullet);
        }
    }

    /**
     * @param entity Entity.
     */
    private void notifyAdded(Entity entity) {
        for (LevelObserver observer : observers) {
            observer.onEntityAdded(entity);
        }
    }

    private void notifyRemoved(Entity entity) {
        for (LevelObserver observer : new ArrayList<>(observers)) {
            observer.onEntityRemoved(entity);
        }
    }

    /**
     * @return unmodifiable snapshot of entities mainly for debugging
     */
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(new ArrayList<>(entities));
    }
}
