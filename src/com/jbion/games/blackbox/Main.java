package com.jbion.games.blackbox;

import com.jbion.games.blackbox.ai.ComputerSolver;
import com.jbion.games.blackbox.model.Grid;
import com.jbion.games.blackbox.model.GuessableGrid;

public class Main {

    public static void main(String[] args) {
        IOHelper io = new IOHelper();
        System.out.print("Before anything, please set the size of the grid:");
        Grid.size = io.getIntegerBetween(2, 10);
        System.out.println("What would you like to do?");
        System.out.println("  0 - Play a game");
        System.out.println("  1 - Get help for an external game");
        System.out.println("  2 - Watch you solve a random grid");
        System.out.print("Your choice: ");
        int choice = io.getIntegerBetween(0, 2);
        if (choice == 0) {
            Game game = new Game(io);
            while (game.play()) {
            }
        } else if (choice == 1) {
            HumanAssistant ha = new HumanAssistant(io);
            while (ha.helpMe()) {
            }
        } else {
            int nBalls = io.getNumberOfBalls();
            Grid grid = new GuessableGrid(nBalls); // no balls displayed when printing
            new ComputerSolver(grid).solve();
        }
    }
}
