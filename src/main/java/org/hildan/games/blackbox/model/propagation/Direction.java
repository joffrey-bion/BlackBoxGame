package org.hildan.games.blackbox.model.propagation;

/**
 * An enum representing a direction followed by a ray.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
public enum Direction {

    UP,
    RIGHT,
    DOWN,
    LEFT;

    private static final Direction[] VALUES = values();

    /**
     * Returns the resulting {@code Direction} after turning right when following
     * this {@code Direction}.
     * 
     * @return the new {@code Direction} after the turn.
     */
    public Direction turnRight() {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }

    /**
     * Returns the resulting {@code Direction} after turning left when following this
     * {@code Direction}.
     * 
     * @return the new {@code Direction} after the turn.
     */
    public Direction turnLeft() {
        return VALUES[(ordinal() - 1 + VALUES.length) % VALUES.length];
    }

    /**
     * Returns the resulting {@code Direction} after a U-turn from this
     * {@code Direction}.
     * 
     * @return the new {@code Direction} after the U-turn.
     */
    public Direction opposite() {
        return VALUES[(ordinal() + 2) % VALUES.length];
    }
}
