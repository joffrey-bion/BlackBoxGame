package com.jbion.games.blackbox.model;

import com.jbion.games.blackbox.model.propagation.Direction;

/**
 * An enum representing the 4 sides of the grid. Each side has a corresponding
 * direction, which is the one followed by any ray shot from that side.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
public enum Side {

    TOP(Direction.DOWN),
    BOTTOM(Direction.UP),
    LEFT(Direction.RIGHT),
    RIGHT(Direction.LEFT);

    private Direction direction;

    private Side(Direction d) {
        direction = d;
    }

    /**
     * Returns the {@link Direction} of the ray shot from this {@code Side}.
     * 
     * @return the {@link Direction} of the ray shot from this {@code Side}.
     */
    public Direction getDirection() {
        return direction;
    }
}
