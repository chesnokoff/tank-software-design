package ru.mipt.bit.platformer.roles;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.ui.TexturedItem;

public class Tree extends TexturedItem {
    public Tree() {
        this(new GridPoint2(1, 1));
    }

    public Tree(GridPoint2 position) {
        super("images/greenTree.png", position);
    }
}
