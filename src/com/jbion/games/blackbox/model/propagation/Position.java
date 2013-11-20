package com.jbion.games.blackbox.model.propagation;

import com.jbion.games.blackbox.model.Grid;
import com.jbion.games.blackbox.model.Side;

class Position {

    public int row;
    public int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(Position pos) {
        this(pos.row, pos.col);
    }
    
    public static Position get(Side side, int index) {
        switch (side) {
        case TOP:
            return new Position(-1, index);
        case BOTTOM:
            return new Position(Grid.size, index);
        case LEFT:
            return new Position(index, -1);
        case RIGHT:
        default:
            return new Position(index, Grid.size);
        }
    }

    public void move(Direction dir) {
        switch (dir) {
        case UP:
            row--;
            break;
        case DOWN:
            row++;
            break;
        case LEFT:
            col--;
            break;
        case RIGHT:
            col++;
            break;
        }
    }
}
