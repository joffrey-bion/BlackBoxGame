package com.joffrey_bion.games.blackbox.model;

public class GuessableGrid extends Grid {

    private BallList guesses;

    public GuessableGrid(int size, int nBalls) {
        super(size, nBalls);
        guesses = new BallList();
    }

    public void placeGuess(int i, int j) {
        guesses.add(i, j);
    }

    public void removeGuess(int i, int j) {
        guesses.remove(i, j);
    }

    public boolean amIRight() {
        for (Ball b : guesses) {
            if (!balls.contains(b)) {
                return false;
            }
        }
        for (Ball b : balls) {
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
