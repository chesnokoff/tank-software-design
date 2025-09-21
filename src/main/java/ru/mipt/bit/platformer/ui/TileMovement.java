package ru.mipt.bit.platformer.ui;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TileMovement {

    private final TiledLevel level;
    private final Interpolation interpolation;

    /**
     * @param level Level.
     * @param interpolation Interpolation.
     */
    public TileMovement(TiledLevel level, Interpolation interpolation) {
        this.level = level;
        this.interpolation = interpolation;
    }

    /**
     * @param item Item.
     */
    public void moveItemBetweenTileCenters(MovableTexturedItem item) {
        Vector2 tileCenter1 = level.calculateTileCenter(item.coordinate);
        item.rectangle.setCenter(tileCenter1);
        float fromTileBottomLeftX = item.rectangle.x;
        float fromTileBottomLeftY = item.rectangle.y;

        Vector2 tileCenter = level.calculateTileCenter(item.destination);
        item.rectangle.setCenter(tileCenter);
        float toTileBottomLeftX = item.rectangle.x;
        float toTileBottomLeftY = item.rectangle.y;

        item.rectangle.setCenter(tileCenter1);

        float intermediateBottomLeftX = interpolation.apply(fromTileBottomLeftX, toTileBottomLeftX, item.getProgress());
        float intermediateBottomLeftY = interpolation.apply(fromTileBottomLeftY, toTileBottomLeftY, item.getProgress());

        item.setCoordinatesToDraw(intermediateBottomLeftX, intermediateBottomLeftY);
    }

    /**
     * @param item Item.
     * @param point Point.
     */
    public void locateItemAtTileCenterImmediately(TexturedItem item, GridPoint2 point) {
        int tileWidth = level.getTileWidth();
        int tileHeight = level.getTileHeight();

        int tileBottomLeftCornerX = point.x * tileWidth;
        int tileBottomLeftCornerY = point.y * tileHeight;

        Vector2 tileCenter = new Rectangle()
                .setX(tileBottomLeftCornerX)
                .setY(tileBottomLeftCornerY)
                .setWidth(tileWidth)
                .setHeight(tileHeight)
                .getCenter(new Vector2());

        item.setCenter(tileCenter);
        item.setCoordinates(point);
    }
}
