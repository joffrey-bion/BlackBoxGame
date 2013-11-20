package com.jbion.games.blackbox;

import com.jbion.games.blackbox.ai.SolvingAssistant;
import com.jbion.games.blackbox.model.Grid;
import com.jbion.games.blackbox.model.Side;
import com.jbion.games.blackbox.model.ports.Ports;

public class HumanAssistant {

    private IOHelper io;
    private SolvingAssistant helper;

    public HumanAssistant(IOHelper io) {
        this.io = io;
    }

    /**
     * Asks the user for parameters and starts an assistant.
     * 
     * @return {@code true} if the user wants help for another game.
     */
    public boolean helpMe() {
        int nBalls = io.getNumberOfBalls();
        helper = new SolvingAssistant(nBalls);
        while (!helper.isSolutionFound()) {
            System.out.println(helper.getNbPossibilities() + " possibilities left.");
            int port = helper.chooseEntryPort();
            Side side = Ports.getSide(port);
            int index = Ports.getIndex(port);
            System.out.println("Try to shoot from the " + side + " side, position " + index + ".");
            updateState(port);
        }
        System.out.println("Solution found! Try this:");
        System.out.println(helper.getSolution());
        System.out.println();
        System.out.println("Play again? (Y/N)");
        return io.getYesNo();
    }

    private void updateState(int port) {
        System.out.println("What is the result?");
        System.out.println("  0 - HIT");
        System.out.println("  1 - REFLECT");
        System.out.println("  2 - DETOUR");
        System.out.print("Result: ");
        int choice = io.getIntegerBetween(0, 2);
        if (choice == 0) {
            helper.setHit(port);
        } else if (choice == 1) {
            helper.setReflect(port);
        } else {
            int twin = getTwin();
            helper.setDetour(port, twin);
        }
        System.out.println();
    }

    private int getTwin() {
        System.out.println("Please enter the port where the detour ended:");
        System.out.print("Side (TOP, BOTTOM, LEFT, RIGHT): ");
        Side side = io.getSide();
        System.out.print("Index (0-" + Grid.size + ": ");
        int index = io.getIndex(side);
        return Ports.getPortNum(side, index);
    }
}
