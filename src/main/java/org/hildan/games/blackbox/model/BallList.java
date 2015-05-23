package org.hildan.games.blackbox.model;

import java.util.LinkedList;

/**
 * An extended list that allows to add and remove {@link Ball}s by their coordinates
 * directly.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
public class BallList extends LinkedList<Ball> {

    /**
     * Creates a new list.
     */
    public BallList() {
        super();
    }

    /**
     * Adds a new {@link Ball} to this list with the specified coordinates.
     * 
     * @param i
     *            The row of the {@code Ball} to create.
     * @param j
     *            The column of the {@code Ball} to create.
     */
    public void add(int i, int j) {
        add(new Ball(i, j));
    }

    /**
     * Removes the {@link Ball} (with the specified coordinates) from this list.
     * 
     * @param i
     *            The row of the {@code Ball} to remove.
     * @param j
     *            The column of the {@code Ball} to remove.
     */
    public void remove(int i, int j) {
        for (Ball b : this) {
            if (b.i == i && b.j == j) {
                remove(b);
                return;
            }
        }
        throw new RuntimeException("The specified ball is not in the list.");
    }

    /**
     * Returns whether the specified {@link Ball} is in this list.
     * 
     * @param i
     *            The row of the ball to check.
     * @param j
     *            The column of the ball to check.
     * @return {@code true} if the ball at the specified coordinates is in this list,
     *         {@code false} otherwise.
     */
    public boolean contains(int i, int j) {
        for (Ball b : this) {
            if (b.i == i && b.j == j) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a new {@code BallList} containing all possible {@link Ball}s for the
     * current grid size.
     * 
     * @return a new {@code BallList} containing all possible {@link Ball}s.
     */
    public static BallList getAllBalls() {
        BallList allBalls = new BallList();
        for (int i = 0; i < Grid.size; i++) {
            for (int j = 0; j < Grid.size; j++) {
                allBalls.add(i, j);
            }
        }
        return allBalls;
    }
}
