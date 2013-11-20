package com.jbion.games.blackbox.model;

/**
 * An extended {@link Grid} that allows to place guesses and check for victory.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
public class GuessableGrid extends Grid {

    private BallList guesses;

    /**
     * Creates a new {@code GuessableGrid}.
     * 
     * @param nBalls
     *            The number of balls to guess.
     */
    public GuessableGrid(int nBalls) {
        super(nBalls);
        guesses = new BallList();
    }

    /**
     * Places a guess at the specified position.
     * 
     * @param i
     *            The row of the guess.
     * @param j
     *            The column of the guess.
     */
    public void placeGuess(int i, int j) {
        guesses.add(i, j);
    }

    /**
     * Removes the guess at the specified position. Throws an exception if there is
     * no guess at the specified position.
     * 
     * @param i
     *            The row of the guess.
     * @param j
     *            The column of the guess.
     */
    public void removeGuess(int i, int j) {
        guesses.remove(i, j);
    }

    /**
     * Returns whether the guesses are correct.
     * 
     * @return {@code true} if all guesses are correct and all {@link Ball}s are
     *         guessed, {@code false} otherwise.
     */
    public boolean amIRight() {
        for (Ball b : guesses) {
            if (!propagator.balls.contains(b)) {
                return false;
            }
        }
        for (Ball b : propagator.balls) {
            if (!guesses.contains(b)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return toString(guesses);
    }
}
