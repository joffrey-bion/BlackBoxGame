package org.hildan.games.blackbox;

import org.hildan.games.blackbox.model.Grid;
import org.hildan.games.blackbox.model.GuessableGrid;
import org.hildan.games.blackbox.model.Side;

public class Game {

    private IOHelper io;
    private int nBalls;
    private GuessableGrid grid;

    public Game(IOHelper io) {
        this.io = io;
    }

    /**
     * Asks the user for parameters and starts a game with a random grid.
     * 
     * @return {@code true} if the user wants to play another game.
     */
    public boolean play() {
        nBalls = io.getNumberOfBalls();
        grid = new GuessableGrid(nBalls);
        while (true) {
            System.out.println("Current state of the entry ports and guesses:");
            System.out.println(grid);
            System.out.println("What would you like to do?");
            System.out.println("  0 - shoot a ray");
            System.out.println("  1 - guess a position");
            System.out.println("  2 - validate the guesses");
            System.out.println("  3 - exit game");
            System.out.print("Your choice: ");
            int choice = io.getIntegerBetween(0, 3);
            if (choice == 0) {
                shootOne();
            } else if (choice == 1) {
                guessOne();
            } else if (choice == 2){
                tryGuesses();
                break;
            } else {
                break;
            }
            System.out.println();
        }
        System.out.println("Play again? (Y/N)");
        return io.getYesNo();
    }

    private void tryGuesses() {
        boolean victory = grid.amIRight();
        if (victory) {
            System.out.println("Well done!");
        } else {
            System.out.println("Too bad, another time maybe!");
        }
    }

    private void guessOne() {
        System.out.println("Enter the position where to place a guess:");
        System.out.print("row: ");
        int i = io.getIntegerBetween(0, Grid.size - 1);
        System.out.print("col: ");
        int j = io.getIntegerBetween(0, Grid.size - 1);
        grid.placeGuess(i, j);
    }

    private void shootOne() {
        System.out.print("Enter the side you want to shoot from (TOP, BOTTOM, LEFT or RIGHT): ");
        Side side = io.getSide();
        System.out.print("Enter the index you want to shoot from on side '" + side + "' (0 to "
                + (Grid.size - 1) + "): ");
        int index = io.getIndex(side);
        grid.shoot(side, index);
    }
}
