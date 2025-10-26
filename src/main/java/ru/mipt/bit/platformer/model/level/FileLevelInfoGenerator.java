package ru.mipt.bit.platformer.model.level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.log.GameLogger;

/**
 * File-based level loader that parses level files directly
 */
public class FileLevelInfoGenerator implements LevelInfoGenerator {
    /** Logger. */
    private static final GameLogger logger = GameLogger.getLogger(FileLevelInfoGenerator.class);

    /** File path. */
    private final String configPath;

    /**
     * @param configPath File path.
     */
    public FileLevelInfoGenerator(String configPath) {
        this.configPath = configPath;
    }

    /** {@inheritDoc} */
    @Override
    public LevelInfo generate() {
        logger.info("Loading level from file: {}", configPath);

        List<String> validLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            validLines = reader.lines().collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Level file not found or cannot be read: " + configPath, e);
        }

        if (validLines.isEmpty()) {
            throw new RuntimeException("Level file is empty: " + configPath);
        }

        LevelInfo info = parseConfig(validLines);

        logger.info("Loading level from file completed");

        return info;
    }

    /** Parses level data from text lines. */
    private static LevelInfo parseConfig(List<String> lines) {
        List<GridPoint2> treePositions = new ArrayList<>();

        GridPoint2 playerPosition = null;

        int levelHeight = lines.size();
        int levelWidth = lines.getFirst().length();

        for (int y = 0; y < levelHeight; y++) {
            String line = lines.get(levelHeight - 1 - y);

            for (int x = 0; x < levelWidth; x++) {
                char cell = line.charAt(x);

                if (cell == 'T') {
                    treePositions.add(new GridPoint2(x, y));
                    continue;
                }

                if (cell == 'X') {
                    playerPosition = new GridPoint2(x, y);
                    continue;
                }

                if (cell == '_')
                    continue;

                throw new RuntimeException("Invalid level config: unexpected character '" + cell + "' at " + x + ", " + y);
            }
        }

        if (playerPosition == null)
            throw new RuntimeException("Invalid level config: player position not found");

        return new LevelInfo(playerPosition, treePositions);
    }
}