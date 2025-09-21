package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.ui.MovableTexturedItem;
import ru.mipt.bit.platformer.ui.TexturedItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardListener {
    private final Map<Integer, Action> keyMap = new HashMap<>();
    public KeyboardListener() {
        keyMap.put(Input.Keys.UP, new UpAction());
        keyMap.put(Input.Keys.W, new UpAction());

        keyMap.put(Input.Keys.DOWN, new DownAction());
        keyMap.put(Input.Keys.S, new DownAction());

        keyMap.put(Input.Keys.LEFT, new LeftAction());
        keyMap.put(Input.Keys.A, new LeftAction());

        keyMap.put(Input.Keys.RIGHT, new RightAction());
        keyMap.put(Input.Keys.D, new RightAction());
    }

    public void processKey(MovableTexturedItem player, List<TexturedItem> obstacles) {
        keyMap.entrySet().stream().filter(entry -> Gdx.input.isKeyPressed(entry.getKey()))
                .forEach(entry -> entry.getValue().accept(player, obstacles));
    }
}
