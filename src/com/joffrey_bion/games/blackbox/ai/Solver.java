package com.joffrey_bion.games.blackbox.ai;

import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;

import com.joffrey_bion.games.blackbox.model.Ball;
import com.joffrey_bion.games.blackbox.model.BallList;
import com.joffrey_bion.games.blackbox.model.EntryPort;
import com.joffrey_bion.games.blackbox.model.Grid;
import com.joffrey_bion.games.blackbox.model.GuessableGrid;
import com.joffrey_bion.games.blackbox.model.PortState;
import com.joffrey_bion.games.blackbox.model.Side;

public class Solver {

    private static final Side[] SIDES = Side.values();
    private static final boolean RANDOM_EP = false;
    private GuessableGrid gridToSolve;
    private int size;
    private int nBalls;
    private LinkedList<Grid> grids;

    public Solver(GuessableGrid gridToSolve) {
        this.gridToSolve = gridToSolve;
        this.size = gridToSolve.getSize();
        this.nBalls = gridToSolve.getNBalls();
        System.out.println("Creating balls list...");
        BallList allBalls = new BallList();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                allBalls.add(i, j);
            }
        }
        System.out.print("Generating " + nBalls + "-long balls lists...");
        LinkedList<BallList> lists = generateBallsLists(allBalls, new BallList(), nBalls);
        grids = new LinkedList<>();
        System.out.println("There are " + lists.size() + " possible grids.");
        System.out.println("Generating all possible grids...");
        for (BallList list : lists) {
            Grid grid = new Grid(size, list);
            grids.add(grid);
            grid.shootAll();
        }

    }

    private static LinkedList<BallList> generateBallsLists(BallList set, BallList exceptions,
            int size) {
        LinkedList<BallList> results = new LinkedList<>();
        if (size == 0) {
            results.add(new BallList());
            return results;
        }
        //System.out.println("generateBallsLists level " + size);
        BallList subexceptions = new BallList();
        subexceptions.addAll(exceptions);
        for (Ball b : set) {
            if (exceptions.contains(b)) {
                continue;
            }
            subexceptions.add(b);
            LinkedList<BallList> sublists = generateBallsLists(set, subexceptions, size - 1);
            for (BallList sublist : sublists) {
                sublist.addFirst(b);
                results.add(sublist);
            }
        }
        return results;
    }

    public void solve() {
        System.out.println("Starting with " + grids.size() + " possibilities");
        while (grids.size() > 1) {
            EntryPort ep;
            do {
                ep = chooseEntryPort();
                Side side = ep.getSide();
                int index = ep.getIndex();
                System.out.println("Shooting from " + side + "-" + index);
            } while (!gridToSolve.shoot(ep.getSide(), ep.getIndex()));

            System.out.println(gridToSolve);
            if (!removeInconsistentGrids(gridToSolve.getEntryPort(ep.getSide(), ep.getIndex()))) {
                // break;
            }
        }
    }

    private EntryPort chooseEntryPort() {
        if (RANDOM_EP) {
            Random rand = new Random();
            Side side = Side.values()[rand.nextInt(4)];
            int index = rand.nextInt(size);
            return new EntryPort(side, index);
        }
        TreeMap<Double, EntryPort> eps = new TreeMap<>();
        for (Side side : SIDES) {
            for (int index = 0; index < size; index++) {
                if (gridToSolve.getEntryPort(side, index).getState() != PortState.UNKNOWN) {
                    continue;
                }
                EntryPort ep = new EntryPort(side, index);
                eps.put(getAvgRemainingGrids(ep), ep);
            }
        }
        return eps.get(eps.firstKey());
    }

    private Double getAvgRemainingGrids(EntryPort ep) {
        LinkedList<Double> nRemainingGrids = new LinkedList<>();
        ep.setState(PortState.HIT);
        nRemainingGrids.add((double) getConsistentGrids(ep).size());
        ep.setState(PortState.REFLECT);
        nRemainingGrids.add((double) getConsistentGrids(ep).size());
        ep.setState(PortState.DETOUR);
        Side epSide = ep.getSide();
        int epIndex = ep.getIndex();
        for (Side side : SIDES) {
            for (int index = 0; index < size; index++) {
                if (side == epSide && index == epIndex) {
                    continue;
                }
                EntryPort possibleTwin = gridToSolve.getEntryPort(side, index);
                ep.setTwin(possibleTwin);
                nRemainingGrids.add((double) getConsistentGrids(ep).size());
            }
        }
        double sum = 0;
        double sumSq = 0;
        for (double nv : nRemainingGrids) {
            sum += nv;
            sumSq += nv * nv;
        }
        System.out.println(ep.positionToString() + " avg rem = " + sumSq / sum);
        return sumSq / sum;
    }

    public static double squareSum(int... nvs) {
        double sum = 0;
        double sumSq = 0;
        for (int nv : nvs) {
            sum += nv;
            sumSq += nv * nv;
        }
        return sumSq / sum;
    }

    private boolean removeInconsistentGrids(EntryPort newEP) {
        LinkedList<Grid> toKeep = getConsistentGrids(newEP);
        grids.retainAll(toKeep);
        int nGrids = grids.size();
        System.out.println(nGrids + " possibilit" + (nGrids > 1 ? "ies" : "y") + " left");
        return toKeep.size() < grids.size();
    }

    private LinkedList<Grid> getConsistentGrids(EntryPort newEP) {
        LinkedList<Grid> consistent = new LinkedList<>();
        for (Grid grid : grids) {
            EntryPort ep = grid.getEntryPort(newEP.getSide(), newEP.getIndex());
            if (ep.equals(newEP)) {
                consistent.add(grid);
            }
        }
        return consistent;
    }
}
