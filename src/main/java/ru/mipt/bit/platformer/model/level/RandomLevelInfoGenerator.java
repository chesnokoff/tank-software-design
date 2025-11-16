package ru.mipt.bit.platformer.model.level;

import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;

/**
 * Random level generator
 */
public class RandomLevelInfoGenerator implements LevelInfoGenerator {
    /** Enemy tanks count. */
    public static final int ENEMY_TANKS_COUNT = 2;

    /** Level width. */
    private final int levelWidth;

    /** Level height. */
    private final int levelHeight;

    /** Random. */
    private final Random random = new Random();

    /**
     * @param levelWidth Level width.
     * @param levelHeight Level height.
     */
    public RandomLevelInfoGenerator(int levelWidth, int levelHeight) {
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }

    /** {@inheritDoc} */
    @Override
    public LevelInfo generate() {
        Set<GridPoint2> treePositions = new HashSet<>();

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                if (random.nextFloat() < 0.2f) {
                    treePositions.add(new GridPoint2(x, y));
                }
            }
        }

        List<GridPoint2> freePositions = collectFreePositions(treePositions);

        GridPoint2 playerPosition = freePositions.remove(random.nextInt(freePositions.size()));

        List<GridPoint2> enemyTankPositions = new ArrayList<>();

        for (int i = 0; i < ENEMY_TANKS_COUNT && !freePositions.isEmpty(); i++)
            enemyTankPositions.add(freePositions.remove(random.nextInt(freePositions.size())));

        return new LevelInfo(playerPosition, enemyTankPositions, treePositions.stream().toList(), levelWidth, levelHeight);
    }

    /** */
    private List<GridPoint2> collectFreePositions(Set<GridPoint2> ponts) {
        List<GridPoint2> freePositions = new ArrayList<>();

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                GridPoint2 position = new GridPoint2(x, y);

                if (ponts.contains(position))
                    continue;

                freePositions.add(position);
            }
        }

        return freePositions;
    }
}
