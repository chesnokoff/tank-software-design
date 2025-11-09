package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Healtable;

/** */
public class HealthBarDecorator implements Drawble {
    /** Bar height. */
    private static final float BAR_HEIGHT = 10f;

    /** Bar offset. */
    private static final float BAR_OFFSET = 5f;

    /** Pixel texture. */
    private static final Texture PIXEL_TEXTURE = createPixelTexture();

    /** Decorated view. */
    private final AnimatedEntityView decoratedView;

    /** Health holder. */
    private final Healtable healthHolder;

    /** Health bar manager. */
    private final HealthBarManager healthBarManager;

    /**
     * @param decoratedView Decorated view.
     * @param healthHolder Health holder.
     * @param healthBarManager Health bar manager.
     */
    public HealthBarDecorator(
            AnimatedEntityView decoratedView,
            Healtable healthHolder,
            HealthBarManager healthBarManager
    ) {
        this.decoratedView = decoratedView;
        this.healthHolder = healthHolder;
        this.healthBarManager = healthBarManager;
    }

    /** {@inheritDoc} */
    @Override
    public void draw(Batch batch) {
        decoratedView.draw(batch);

        if (!healthBarManager.isVisible()) {
            return;
        }

        int maxHealth = healthHolder.getMaxHealth();
        if (maxHealth <= 0) {
            return;
        }

        float healthRatio = Math.min(1f, Math.max(0f, healthHolder.getHealth() / (float) maxHealth));

        Rectangle bounds = decoratedView.rect();
        float barWidth = bounds.width;
        float barX = bounds.x;
        float barY = bounds.y + bounds.height + BAR_OFFSET;

        Color previousColor = new Color(batch.getColor());

        batch.setColor(Color.DARK_GRAY);
        batch.draw(PIXEL_TEXTURE, barX, barY, barWidth, BAR_HEIGHT);

        batch.setColor(Color.GREEN);
        batch.draw(PIXEL_TEXTURE, barX, barY, barWidth * healthRatio, BAR_HEIGHT);

        batch.setColor(previousColor);
    }

    /** */
    private static Texture createPixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
