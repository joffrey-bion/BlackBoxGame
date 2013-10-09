package com.joffrey_bion.games.blackbox;

import java.util.Scanner;

import com.joffrey_bion.games.blackbox.model.Grid;
import com.joffrey_bion.games.blackbox.model.GuessableGrid;
import com.joffrey_bion.games.blackbox.model.Side;

public class Game {

    private final int size;
    private final Scanner sc;
    private int nBalls;
    private GuessableGrid grid;
    
    public Game(int size) {
        this.size = size;
        this.sc = new Scanner(System.in);
    }
    
    public static void main(String[] args) {
        int size = 8;
        System.out.println("Starting " + size + "-sized game...");
        new Game(size).play();
    }
    
    private void play() {
        nBalls = getNumberOfBalls();
        grid = new GuessableGrid(size, nBalls);
        while (true) {
            System.out.println(grid);
            System.out.println("What would you like to do?");
            System.out.println("  0 - shoot a ray");
            System.out.println("  1 - guess a position");
            System.out.println("  2 - validate the guesses");
            System.out.print("Your choice: ");
            int choice = getIntegerBetween(0, 2);
            if (choice == 0) {
                shootOne();
            } else if (choice == 1) {
                guessOne();
            } else {
                tryGuesses();
                break;
            }
            System.out.println();
        }
    }

    private void someShots(Grid grid) {
        grid.shoot(Side.TOP, 2);
        System.out.println(grid);
        grid.shoot(Side.TOP, 5);
        System.out.println(grid);
        grid.shoot(Side.LEFT, 0);
        System.out.println(grid);
        grid.shoot(Side.RIGHT, 2);
        System.out.println(grid);
        grid.shoot(Side.RIGHT, 5);
        System.out.println(grid);
        grid.shoot(Side.BOTTOM, 3);
        System.out.println(grid);
    }

    private int getNumberOfBalls() {
        System.out.print("Enter number of balls to find: ");
        return getIntegerBetween(1, size);
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
        int i = getIntegerBetween(0, size - 1);
        System.out.print("col: ");
        int j = getIntegerBetween(0, size - 1);
        grid.placeGuess(i, j);
    }

    private void shootOne() {
        Side side = getSide();
        int index = getIndex(side);
        grid.shoot(side, index);
    }

    private Side getSide() {
        System.out.print("Enter the side you want to shoot from (TOP, BOTTOM, LEFT or RIGHT): ");
        Side side = null;
        while (sc.hasNext()) {
            String sideStr = sc.next();
            try {
                side = Side.valueOf(sideStr);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("TOP, BOTTOM, LEFT or RIGHT expected.");
            }
        }
        return side;
    }

    private int getIndex(Side side) {
        System.out.print("Enter the index you want to shoot from on side '" + side + "' (0 to "
                + (size - 1) + "): ");
        return getIntegerBetween(0, size - 1);
    }

    private int getIntegerBetween(int min, int max) {
        int i = -1;
        while (sc.hasNext()) {
            if (!sc.hasNextInt()) {
                System.err.println("Please enter an integer.");
                sc.next();
            } else {
                i = sc.nextInt();
                if (i >= min && i <= max) {
                    break;
                } else {
                    System.err.println("Please enter a value between " + min + " and " + max + ".");
                }
            }
        }
        return i;
    }
}
