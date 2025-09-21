package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.ui.MovableTexturedItem;
import ru.mipt.bit.platformer.ui.TexturedItem;

import java.util.List;
import java.util.function.BiConsumer;

public interface Action extends BiConsumer<MovableTexturedItem, List<TexturedItem>> {}
