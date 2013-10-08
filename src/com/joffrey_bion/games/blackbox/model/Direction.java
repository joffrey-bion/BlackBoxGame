package com.joffrey_bion.games.blackbox.model;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;
    
    private static final Direction[] VALUES = values();

    public Direction turnRight() {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }

    public Direction turnLeft() {
        return VALUES[(ordinal() - 1 + VALUES.length) % VALUES.length];
    }

    public Direction opposite() {
        return VALUES[(ordinal() + 2) % VALUES.length];
    }
}
