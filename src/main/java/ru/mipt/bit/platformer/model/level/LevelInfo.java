package ru.mipt.bit.platformer.model.level;

import com.badlogic.gdx.math.GridPoint2;
import java.util.List;

public record LevelInfo(
        GridPoint2 playerStartPosition,
        List<GridPoint2> enemyPositions,
        List<GridPoint2> treePositions,
        int levelWidth,
        int levelHeight
) {}
