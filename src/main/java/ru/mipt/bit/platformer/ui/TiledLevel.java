package ru.mipt.bit.platformer.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.NoSuchElementException;

/** */
public class TiledLevel implements Disposable {
    /**
     * Level.
     */
    private final TiledMap level;
    /**
     * Level renderer.
     */
    private final MapRenderer levelRenderer;

    /**
     * @param batch Batch.
     * @param src Source.
     */
    public TiledLevel(Batch batch, String src) {
        level = new TmxMapLoader().load(src);
        levelRenderer = createSingleLayerMapRenderer(batch);
    }

    /** Render each tile of the level */
    public void render() {
        levelRenderer.render();
    }

    public Vector2 calculateTileCenter(GridPoint2 tileCoordinates) {
        TiledMapTileLayer layer = getSingleLayer();
        int tileWidth = layer.getTileWidth();
        int tileHeight = layer.getTileHeight();
        int tileBottomLeftCornerX = tileCoordinates.x * tileWidth;
        int tileBottomLeftCornerY = tileCoordinates.y * tileHeight;

        return new Rectangle()
                .setX(tileBottomLeftCornerX)
                .setY(tileBottomLeftCornerY)
                .setWidth(tileWidth)
                .setHeight(tileHeight)
                .getCenter(new Vector2());
    }

    @Override
    public void dispose() {
        level.dispose();
    }

    public int getTileWidth() {
        return getSingleLayer().getTileWidth();
    }

    public int getTileHeight() {
        return getSingleLayer().getTileHeight();
    }

    /**
     * @return Single layer.
     */
    private TiledMapTileLayer getSingleLayer() {
        MapLayers layers = level.getLayers();
        switch (layers.size()) {
            case 0:
                throw new NoSuchElementException("Map has no layers");
            case 1:
                @SuppressWarnings("unchecked")
                TiledMapTileLayer layer = (TiledMapTileLayer) layers.iterator().next();
                return layer;
            default:
                throw new IllegalArgumentException("Map has more than one layer");
        }
    }

    private MapRenderer createSingleLayerMapRenderer(Batch batch) {
        TiledMapTileLayer tileLayer = getSingleLayer();
        float viewWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        float viewHeight = tileLayer.getHeight() * tileLayer.getTileHeight();

        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(level, batch);
        mapRenderer.getViewBounds().set(0f, 0f, viewWidth, viewHeight);

        return mapRenderer;
    }
}
