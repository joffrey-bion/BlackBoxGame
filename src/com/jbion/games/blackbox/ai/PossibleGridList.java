package com.jbion.games.blackbox.ai;

import java.util.LinkedList;

import com.jbion.games.blackbox.model.Ball;
import com.jbion.games.blackbox.model.BallList;
import com.jbion.games.blackbox.model.Grid;
import com.jbion.games.blackbox.model.ports.Ports;
import com.jbion.utils.strings.BoxChars;
import com.jbion.utils.strings.GridDrawer;

/**
 * A list that contains all possible grids. This class provides operations to filter
 * those grids according to one port's state, which is helpful for solving the
 * BlackBox.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
class PossibleGridList extends LinkedList<Grid> {

    private static final boolean FULL_PROGRESS_BAR = false;

    /**
     * Creates a new {@code PossibleGridList} containing all possible grids for the
     * specified number of balls.
     * 
     * @param nBalls
     *            The number of hidden {@link Ball}s in the {@link Grid}s.
     */
    PossibleGridList(int nBalls) {
        super();
        System.out.print("Creating all possible balls...");
        BallList allBalls = BallList.getAllBalls();
        System.out.println(" Done.");

        // create all possible sets of nBalls balls
        System.out.print("Generating " + nBalls + "-long balls lists...");
        LinkedList<BallList> lists = generateBallsLists(allBalls, new BallList(), nBalls);
        final int total = lists.size();
        System.out.println(" Done.");
        System.out.println("There are " + lists.size() + " possible grids.");

        System.out.println("Generating all possible grids...");
        if (!FULL_PROGRESS_BAR) {
            System.out.println(BoxChars.BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT
                    + GridDrawer.repeat(BoxChars.BOX_DRAWINGS_LIGHT_HORIZONTAL, 50)
                    + BoxChars.BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT);
            System.out.print(BoxChars.BOX_DRAWINGS_VERTICAL_SINGLE_AND_RIGHT_DOUBLE);
        }
        for (BallList list : lists) {
            Grid grid = new Grid(list);
            add(grid);
            grid.shootAll();
            displayProgress(size(), total);
        }
        System.out.println();
    }

    /**
     * Generates all possible lists of {@code size} {@link Ball}s. The balls are
     * chosen among the specified {@code set}, but can't be chosen if they are in the
     * {@code exceptions} set.
     * 
     * @param set
     *            The set of {@link Ball}s to choose from.
     * @param exceptions
     *            The {@code Ball}s that can't be chosen from {@code set}.
     * @param nb
     *            The number of {@code Ball}s in each list.
     * @return A list of all possible lists of {@code Ball}s with the specified
     *         parameters.
     */
    private static LinkedList<BallList> generateBallsLists(BallList set, BallList exceptions, int nb) {
        LinkedList<BallList> results = new LinkedList<>();
        // adds an empty list if the size is 0
        if (nb == 0) {
            results.add(new BallList());
            return results;
        }
        BallList subexceptions = new BallList();
        subexceptions.addAll(exceptions);
        // chooses the first ball
        for (Ball b : set) {
            // skip exceptions
            if (exceptions.contains(b)) {
                continue;
            }
            // not to choose b in the rest of the list
            subexceptions.add(b);
            // recursive call to build the rest of the list
            LinkedList<BallList> sublists = generateBallsLists(set, subexceptions, nb - 1);
            for (BallList sublist : sublists) {
                sublist.addFirst(b);
                results.add(sublist);
            }
            // we don't put b back here because
            // all lists containing it have already been counted
        }
        return results;
    }

    /**
     * Removes all the grids that are inconsistent with the specified port's state.
     * 
     * @param port
     *            The port to check the state of.
     * @param compared
     *            The {@link Ports} list to get {@code port}'s state from.
     */
    void removeInconsistentGrids(int port, Ports compared) {
        System.out.print("Removing inconsistent grids...");
        LinkedList<Grid> toKeep = getConsistentGrids(port, compared);
        retainAll(toKeep);
        System.out.println("Done.");
    }

    /**
     * Returns a {@link LinkedList} containing all the possible grids that are
     * consistent with the state of the specified port.
     * 
     * @param port
     *            The port to check the state of.
     * @param compared
     *            The {@link Ports} list to get {@code port}'s state from.
     * @return a new {@link LinkedList} containing all the possible grids that are
     *         consistent with the state of the specified port.
     */
    LinkedList<Grid> getConsistentGrids(int port, Ports compared) {
        LinkedList<Grid> consistent = new LinkedList<>();
        for (Grid grid : this) {
            if (grid.getPorts().isConsistent(port, compared)) {
                consistent.add(grid);
            }
        }
        return consistent;
    }

    private static void displayProgress(int count, int total) {
        if (count == total || count % (total / 50) == 0) {
            if (FULL_PROGRESS_BAR) {
                int percent = count * 50 / total;
                System.out.print(String.format("% 4d", percent * 2) + "% ");
                System.out.print(BoxChars.BOX_DRAWINGS_VERTICAL_SINGLE_AND_RIGHT_DOUBLE);
                for (int i = 0; i < 50; i++) {
                    if (i < percent) {
                        System.out.print(BoxChars.BOX_DRAWINGS_DOUBLE_HORIZONTAL);
                    } else if (i == percent) {
                        System.out.print(">");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.println("]");
            } else {
                if (count == total) {
                    System.out.print(BoxChars.BOX_DRAWINGS_VERTICAL_SINGLE_AND_LEFT_DOUBLE);
                } else {
                    System.out.print(BoxChars.BOX_DRAWINGS_DOUBLE_HORIZONTAL);
                }
            }
        }
    }
}
