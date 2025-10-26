package ru.mipt.bit.platformer.model.level;

import com.badlogic.gdx.math.GridPoint2;
import java.util.List;

/** Info about level: player and trees position. */
public record LevelInfo(GridPoint2 playerStartPosition, List<GridPoint2> treePositions) {}
