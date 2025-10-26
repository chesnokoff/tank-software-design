package ru.mipt.bit.platformer.model.level;

import com.badlogic.gdx.math.GridPoint2;
import java.util.HashSet;
import java.util.Set;

/**
 * Random level generator
 */
public class RandomLevelInfoGenerator implements LevelInfoGenerator {
    /** Level width. */
    private final int levelWidth;

    /** Level height. */
    private final int levelHeight;

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
        GridPoint2 playerPosition = null;

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                if (Math.random() < 0.2) {
                    treePositions.add(new GridPoint2(x, y));
                }
            }
        }

        while (playerPosition == null) {
            GridPoint2 position = new GridPoint2(getRandomNumber(levelWidth), getRandomNumber(levelHeight));

            if (treePositions.contains(position))
                continue;

            playerPosition = position;
        }

        return new LevelInfo(playerPosition, treePositions.stream().toList());
    }

    /**
     * @param max Max value.
     */
    private static int getRandomNumber(int max) {
        return (int) (Math.random() * max);
    }
}
