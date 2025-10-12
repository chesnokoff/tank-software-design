package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.log.GameLogger;

/**
 * Class representing an entity in the game.
 * Holds information about position, direction, and movement state.
 */
public class Entity {
    /** Logger for debugging. */
    private static final GameLogger logger = GameLogger.getLogger(Entity.class);

    /** Current position of the entity. */
    private final GridPoint2 position;

    /** Destination point of the entity. Equals to position if not moving. */
    private final GridPoint2 destination;

    /** Direction the entity is facing or moving towards. */
    private Direction direction;

    /** Flag indicating whether the entity is currently moving. */
    private boolean moving;

    /**
     * Constructor for the Entity class.
     *
     * @param position initial position of the entity
     */
    public Entity(GridPoint2 position) {
        this.position = new GridPoint2(position);
        this.destination = new GridPoint2(position);
        this.direction = Direction.UP;
        this.moving = false;
    }

    /**
     * Starts moving the entity in the specified direction.
     *
     * @param dir direction to move
     * @return true if the movement was started successfully, false otherwise
     */
    public boolean move(Direction dir, ObstaclesManager collisionManager) {
        if (!canMove(dir, collisionManager)) {
            return false;
        }

        this.direction = dir;
        this.destination.add(dir.dx, dir.dy);
        this.moving = true;

        logger.debug("Movement started - direction: {}, from: {} to {}", dir, position, destination);
        return true;
    }

    /**
     * Finishes the movement, updating the current position to the destination.
     */
    public void finishMove() {
        logger.debug("Finishing move - from: {} to {}", position, destination);

        this.position.set(destination);

        this.moving = false;

        logger.debug("Movement finished - new position: {}", position);
    }

    /**
     * Checks if the entity is currently moving.
     *
     * @return true if the entity is moving, false otherwise
     */
    public boolean isMoving() {
        return moving;
    }

    /** */
    public boolean canMove(Direction dir, ObstaclesManager collisionManager) {
        if (moving) {
            return false;
        }

        GridPoint2 newDestination = new GridPoint2(position).add(dir.dx, dir.dy);

        if (!collisionManager.isPositionFree(newDestination)) {
            logger.debug("Movement blocked by obstacle at {}", newDestination);
            return false;
        }

        return true;
    }

    /**
     * Returns the current position of the entity.
     *
     * @return current position
     */
    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }

    /**
     * Returns the destination position of the entity.
     *
     * @return destination position
     */
    public GridPoint2 getDestination() {
        return new GridPoint2(destination);
    }

    /**
     * Returns the current direction of the entity.
     *
     * @return current direction
     */
    public Direction getDirection() {
        return direction;
    }
}
