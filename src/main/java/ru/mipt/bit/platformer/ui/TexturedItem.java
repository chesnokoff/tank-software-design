package ru.mipt.bit.platformer.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class TexturedItem implements Disposable {
    /** Texture. */
    protected final Texture texture;

    /** Region. */
    final protected TextureRegion region;

    /** Rectangle. */
    protected final Rectangle rectangle;

    /** Coordinate. */
    GridPoint2 coordinate;

    /** Rotation. */
    protected float rotation;


    /**
     * @param src Source.
     * @param curCoordinate Current coordinate.
     */
    public TexturedItem(String src, GridPoint2 curCoordinate) {
        texture = new Texture(src);
        this.coordinate = curCoordinate;
        region = new TextureRegion(texture);
        rectangle = createBoundingRectangle(region);
        rotation = 0;
    }

    /**
     * @param rotation Rotation.
     */
    public TexturedItem rotate(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public void draw(Batch batch) {
        draw(batch, region, rectangle, rotation);
    }

    protected static void draw(Batch batch, TextureRegion region, Rectangle rectangle, float rotation) {
        int regionWidth = region.getRegionWidth();
        int regionHeight = region.getRegionHeight();
        float regionOriginX = regionWidth / 2f;
        float regionOriginY = regionHeight / 2f;
        batch.draw(region, rectangle.x, rectangle.y, regionOriginX, regionOriginY, regionWidth, regionHeight, 1f, 1f, rotation);
    }

    public boolean hasCommonBorderOnRightWith(TexturedItem other) {
        return coordinate.x + 1 == other.coordinate.x && coordinate.y == other.coordinate.y;
    }

    public boolean hasCommonBorderOnLeftWith(TexturedItem other) {
        return coordinate.x - 1 == other.coordinate.x && coordinate.y == other.coordinate.y;
    }

    public boolean hasCommonBorderOnTopWith(TexturedItem other) {
        return coordinate.x == other.coordinate.x && coordinate.y + 1 == other.coordinate.y;
    }

    public boolean hasCommonBorderOnBottomWith(TexturedItem other) {
        return coordinate.x == other.coordinate.x && coordinate.y - 1 == other.coordinate.y;
    }

    private static Rectangle createBoundingRectangle(TextureRegion region) {
        return new Rectangle()
                .setWidth(region.getRegionWidth())
                .setHeight(region.getRegionHeight());
    }

    void setCenter(Vector2 tileCenter) {
        rectangle.setCenter(tileCenter);
    }

    void setCoordinates(GridPoint2 point) {
        coordinate.set(point);
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        texture.dispose();
    }
}
