package org.hildan.games.blackbox.ai;

import org.hildan.games.blackbox.model.Grid;
import org.hildan.games.blackbox.model.ports.State;

public class ComputerSolver {

    private Grid gridToSolve;
    private SolvingAssistant helper;

    /**
     * Creates a new solver for the specified {@code GuessableGrid}.
     * 
     * @param gridToSolve
     *            The grid to solve.
     */
    public ComputerSolver(Grid gridToSolve) {
        this.gridToSolve = gridToSolve;
        helper = new SolvingAssistant(gridToSolve.getNBalls());
    }

    public void solve() {
        System.out.println(gridToSolve);
        int port;
        while (!helper.isSolutionFound()
                && (port = helper.chooseEntryPort()) >= 0) {
            System.out.println(helper.getNbPossibilities() + " possibilities left.");
            System.out.println("Shooting from " + port);
            Integer twin = gridToSolve.shoot(port);
            State state = gridToSolve.getState(port);
            System.out.println("Discovered state: " + state);
            System.out.println(gridToSolve);
            switch (state) {
            case HIT:
                helper.setHit(port);
                break;
            case REFLECT:
                helper.setReflect(port);
                break;
            case DETOUR:
                helper.setDetour(port, twin);
                break;
            case UNKNOWN:
            default:
                throw new RuntimeException(
                        "INTERNAL ERROR: The state of the port must be known at this point.");
            }
        }
        System.out.println("Solution found:");
        System.out.println(helper.getSolution());
    }
}
