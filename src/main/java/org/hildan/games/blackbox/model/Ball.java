package org.hildan.games.blackbox.model;

/**
 * A little wrapper class for 2 coordinates of a ball.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
public class Ball {

    /**
     * Row of this {@code Ball}.
     */
    public int i;
    /**
     * Column of this {@code Ball}.
     */
    public int j;

    /**
     * Creates a {@code Ball} with the specified coordinates.
     * 
     * @param i
     *            The row.
     * @param j
     *            The column.
     */
    public Ball(int i, int j) {
        this.i = i;
        this.j = j;
    }
}