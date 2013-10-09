package com.joffrey_bion.games.blackbox.model;

public class GuessableGrid extends Grid {

    private boolean[][] guess;

    public GuessableGrid(int size, int nBalls) {
        super(size, nBalls);
        guess = new boolean[SIZE][SIZE];
    }

    public void placeGuess(int i, int j) {
        guess[i][j] = true;
    }

    public void removeGuess(int i, int j) {
        guess[i][j] = false;
    }

    public boolean amIRight() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (guess[i][j] != isBall(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return toString(guess);
    }
}
