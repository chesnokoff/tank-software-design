package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.mipt.bit.platformer.log.GameLogger;
import ru.mipt.bit.platformer.model.Entity;

import com.badlogic.gdx.math.Interpolation;

/** Responsible for animation of an entity. */
public class AnimatedEntityView implements Drawble {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(AnimatedEntityView.class);

    /** Entity. */
    private final Entity entity;

    /** Region. */
    private final TextureRegion region;

    /** Rectangle. */
    private final Rectangle rect;

    /** Interpolation of movement. */
    private final Interpolation interpolation = Interpolation.smooth;

    /** Speed. */
    private final float speed;

    /** Progress of the animation. */
    private float progress = 0f;

    /** */
    public AnimatedEntityView(Entity entity, Texture texture, float speed) {
        this.entity = entity;
        this.region = new TextureRegion(texture);
        this.rect = new Rectangle(0, 0, region.getRegionWidth(), region.getRegionHeight());
        this.speed = speed;
    }

    /**
     * @param entity Entity.
     * @param texturePath Texture path.
     * @param speed Speed.
     */
    public AnimatedEntityView(Entity entity, String texturePath, float speed) {
        this(entity, new Texture(texturePath), speed);
    }

    /**
     * Updates the view.
     *
     * @param deltaTime Delta time.
     * @param level Level.
     */
    public void update(float deltaTime, TiledLevel level) {
        logger.debug("Update called - moving: {}, progress: {}", entity.isMoving(), progress);

        if (!entity.isMoving()) {
            if (!rect.getCenter(new Vector2()).equals(entity.getPosition())) {
                rect.setCenter(level.calculateTileCenter(entity.getPosition()));
            }
            return;
        }

        float previousProgress = progress;
        progress = Math.min(1f, progress + deltaTime / speed);

        logger.debug("Movement update - direction: {}, progress: {} -> {}, delta: {}, speed: {}",
                entity.getDirection(), previousProgress, progress, deltaTime, speed);

        Vector2 from = level.calculateTileCenter(entity.getPosition());
        Vector2 to = level.calculateTileCenter(entity.getDestination());
        Vector2 current = new Vector2(
                interpolation.apply(from.x, to.x, progress),
                interpolation.apply(from.y, to.y, progress)
        );

        rect.setCenter(current);

        if (progress >= 1f) {
            logger.info("Movement completed for {} from {} to {}",
                    entity.getClass().getSimpleName(), entity.getPosition(), entity.getDestination());
            entity.finishMove();
            progress = 0f;
            logger.debug("Progress reset to 0");
        }
    }

    /** */
    public Rectangle rect() {
        return rect;
    }

    /** {@inheritDoc} */
    @Override public void draw(Batch batch) {
        float rotation = entity.getDirection().rotation;
        batch.draw(region, rect.x, rect.y,
                rect.width / 2f, rect.height / 2f,
                rect.width, rect.height,
                1f, 1f, rotation);
    }
}
