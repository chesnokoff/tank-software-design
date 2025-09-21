package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.ui.MovableTexturedItem;
import ru.mipt.bit.platformer.ui.TexturedItem;

import java.util.List;

public class LeftAction implements Action {
    @Override
    public void accept(MovableTexturedItem player, List<TexturedItem> obstacles) {
        if (!player.progressCompleted()) {
            return;
        }

        player.rotate(-180f);

        if (obstacles.stream().anyMatch(item -> player.hasCommonBorderOnLeftWith(item))) {
            return;
        }

        player.moveLeft();
        player.resetProgress();

    }
}
