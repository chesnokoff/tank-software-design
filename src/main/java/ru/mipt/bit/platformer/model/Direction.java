package ru.mipt.bit.platformer.model;

/** */
public enum Direction {
    /** Up. */
    UP(0, 1, 90f),

    /** Down. */
    DOWN(0, -1, -90f),

    /** Left. */
    LEFT(-1, 0, 180f),

    /** Right. */
    RIGHT(1, 0, 0f);

    /** Dx. */
    public final int dx;

    /** Dy. */
    public final int dy;

    /** Rotation. */
    public final float rotation;

    /**
     * @param dx Dx.
     * @param dy Dy.
     * @param rotation Rotation.
     */
    Direction(int dx, int dy, float rotation) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
    }
}
