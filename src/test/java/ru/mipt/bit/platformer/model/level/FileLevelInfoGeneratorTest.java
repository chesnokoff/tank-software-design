package ru.mipt.bit.platformer.model.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** */
class FileLevelInfoGeneratorTest {
    /** Tests reading from config */
    @Test
    void testConfig() {
        FileLevelInfoGenerator generator = new FileLevelInfoGenerator("src/test/resources/level.txt");

        LevelInfo levelInfo = generator.generate();

        assertNotNull(levelInfo);

        assertEquals(new GridPoint2(5, 1), levelInfo.playerStartPosition());

        assertEquals(10, levelInfo.treePositions().size());

        for (int i = 0; i < 10; i++)
            assertTrue(levelInfo.treePositions().contains(new GridPoint2(i, 2)));
    }
}
