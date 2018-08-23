package org.hildan.games.blackbox;

import org.hildan.games.blackbox.ai.SolvingAssistant;
import org.hildan.games.blackbox.model.Grid;
import org.hildan.games.blackbox.model.Side;
import org.hildan.games.blackbox.model.ports.Ports;
import org.hildan.games.blackbox.model.ports.State;

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
        System.out.print("What is the result (HIT, REFLECT, DETOUR)? ");
        State state = io.getState();
        switch (state) {
        case HIT:
            helper.setHit(port);
            break;
        case REFLECT:
            helper.setReflect(port);
            break;
        case DETOUR:
            int twin = getTwin();
            helper.setDetour(port, twin);
            break;
        case UNKNOWN:
        default:
            throw new RuntimeException(
                    "INTERNAL ERROR: The state of the port must be known at this point.");
        }
        System.out.println();
    }

    private int getTwin() {
        System.out.println("Please enter the port where the detour ended:");
        System.out.print("Side (TOP, BOTTOM, LEFT, RIGHT): ");
        Side side = io.getSide();
        System.out.print("Index (0-" + (Grid.size - 1) + "): ");
        int index = io.getIndex();
        return Ports.getPortNum(side, index);
    }
}
