package com.jbion.games.blackbox.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.jbion.games.blackbox.model.Grid;
import com.jbion.games.blackbox.model.ports.Ports;
import com.jbion.games.blackbox.model.ports.State;
import com.jbion.utils.console.drawing.progress.OneTimeProgressBar;

/**
 * A helper that contains useful methods to keep track of the state of a BlackBox
 * game and computes the best decisions to solve it.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
public class SolvingAssistant {

    private PossibleGridList possibleGrids;
    private Ports testPorts;

    /**
     * Creates a new {@code SolvingHelper} to find the position of {@code nBalls}
     * balls in a grid of the current size.
     * 
     * @param nBalls
     *            The number of hidden balls to find.
     */
    public SolvingAssistant(int nBalls) {
        testPorts = new Ports();
        possibleGrids = new PossibleGridList(nBalls);
    }

    /**
     * Returns whether a solution has been found.
     * 
     * @return {@code false} if more than 1 possible grid are left, {@code true}
     *         otherwise.
     */
    public boolean isSolutionFound() {
        return possibleGrids.size() <= 1;
    }

    /**
     * Returns the number of possible {@link Grid}s.
     * 
     * @return the number of possible {@link Grid}s.
     */
    public int getNbPossibilities() {
        return possibleGrids.size();
    }

    /**
     * Returns the only possible {@link Grid}. If more than one {@code Grid} are
     * left, or none at all, this method throws an exception.
     * 
     * @return the solution Grid.
     */
    public Grid getSolution() {
        if (possibleGrids.size() == 0) {
            throw new RuntimeException("No possible solution!");
        }
        if (possibleGrids.size() > 1) {
            throw new RuntimeException("Not a unique solution");
        }
        return possibleGrids.getFirst();
    }

    /**
     * Indicates to this {@link SolvingAssistant} that the tested port is a
     * {@link State#HIT}.
     * 
     * @param port
     *            The tested port.
     */
    public void setHit(int port) {
        testPorts.setHit(port);
        possibleGrids.removeInconsistentGrids(port, testPorts);
    }

    /**
     * Indicates to this {@link SolvingAssistant} that the tested port is a
     * {@link State#REFLECT}.
     * 
     * @param port
     *            The tested port.
     */
    public void setReflect(int port) {
        testPorts.setReflect(port);
        possibleGrids.removeInconsistentGrids(port, testPorts);
    }

    /**
     * Indicates to this {@link SolvingAssistant} that the tested port is a
     * {@link State#DETOUR}.
     * 
     * @param port
     *            The tested port.
     * @param twin
     *            The port where the ray arrived after the detour.
     */
    public void setDetour(int port, int twin) {
        testPorts.setDetour(port, twin);
        possibleGrids.removeInconsistentGrids(port, testPorts);
    }

    /**
     * Chooses the best entry port to try in the current situation. This is based on
     * the average information gain given by knowing the state of a port. The chosen
     * port is the one with the maximum average information gain.
     * 
     * @return the best port to try in the current situation.
     */
    public int chooseEntryPort() {
        System.out.println("Looking for the best port to test...");
        TreeMap<Double, Integer> eps = new TreeMap<>();
        OneTimeProgressBar progress = new OneTimeProgressBar(Ports.size(), 50);
        progress.begin();
        for (int port = 0; port < Ports.size(); port++) {
            if (testPorts.getState(port) != State.UNKNOWN) {
                continue;
            }
            eps.put(getAvgRemainingGrids(port), port);
            progress.printProgress(port + 1);
        }
        progress.end();
        if (eps.size() > 0) {
            return eps.get(eps.firstKey());
        } else {
            return -1;
        }
    }

    /**
     * Returns the average number of grids left when the state of the specified port
     * is known.
     * 
     * @param port
     *            The port to test.
     * @return the average number of grids left when the state of the specified port
     *         is known.
     */
    private double getAvgRemainingGrids(int port) {
        LinkedList<Double> nRemainingGrids = new LinkedList<>();
        double nbGrids;
        testPorts.setHit(port);
        nbGrids = possibleGrids.getConsistentGrids(port, testPorts).size();
        nRemainingGrids.add(nbGrids);
        testPorts.setReflect(port);
        nbGrids = possibleGrids.getConsistentGrids(port, testPorts).size();
        nRemainingGrids.add(nbGrids);
        for (int twin = 0; twin < Ports.size(); twin++) {
            if (twin == port || testPorts.isKnown(twin)) {
                continue;
            }
            testPorts.setDetour(port, twin);
            nbGrids = possibleGrids.getConsistentGrids(port, testPorts).size();
            nRemainingGrids.add(nbGrids);
            testPorts.resetState(port);
        }
        return weigthedAverage(nRemainingGrids);
    }

    /**
     * Returns the weighted average of the specified numbers of grids. The weight is
     * the probability for a subset of grids to remain after testing a port, which is
     * exactly the number of grids left (for each state) divided by the total number
     * of grids.
     * 
     * @param nGrids
     *            A list containing the sizes of the remaining grids subsets to get
     *            the average from.
     * @return the weighted average of the specified numbers of grids.
     */
    private static double weigthedAverage(List<Double> nGrids) {
        double totalGrids = 0;
        double weightedSum = 0;
        for (double n : nGrids) {
            totalGrids += n;
            weightedSum += n * n;
        }
        return weightedSum / totalGrids;
    }
}
