package com.joffrey_bion.games.blackbox.model;

public enum Side {
    TOP(Direction.DOWN),
    BOTTOM(Direction.UP),
    LEFT(Direction.RIGHT),
    RIGHT(Direction.LEFT);
    
    private Direction direction;
    
    private Side(Direction d) {
        direction = d;
    }
    
    public Direction getDirection() {
        return direction;
    }
}
