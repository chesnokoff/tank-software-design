package ru.mipt.bit.platformer.view;

import java.util.NoSuchElementException;
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
import ru.mipt.bit.platformer.log.GameLogger;

/** */
public class TiledLevel implements Disposable {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(TiledLevel.class);

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
        logger.info("Loading Tiled level from: {}", src);
        level = new TmxMapLoader().load(src);
        levelRenderer = createSingleLayerMapRenderer(batch);
    }

    /** Render each tile of the level */
    public void render() {
        levelRenderer.render();
    }

    /**
     * @return Level width in tiles.
     */
    public int getWidthInTiles() {
        return getSingleLayer().getWidth();
    }

    /**
     * @return Level height in tiles.
     */
    public int getHeightInTiles() {
        return getSingleLayer().getHeight();
    }

    /**
     * Generate center of tile with coordinates of left bottom corner.
     *
     * @param tileCoordinates Coordinates of left bottom corner.
     * @return Center of tile.
     */
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

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        level.dispose();
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
                TiledMapTileLayer layer = (TiledMapTileLayer) layers.iterator().next();
                return layer;
            default:
                throw new IllegalArgumentException("Map has more than one layer");
        }
    }

    /**
     * Create map renderer.
     *
     * @param batch Batch.
     */
    private MapRenderer createSingleLayerMapRenderer(Batch batch) {
        TiledMapTileLayer tileLayer = getSingleLayer();
        float viewWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        float viewHeight = tileLayer.getHeight() * tileLayer.getTileHeight();

        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(level, batch);
        mapRenderer.getViewBounds().set(0f, 0f, viewWidth, viewHeight);

        return mapRenderer;
    }
}
