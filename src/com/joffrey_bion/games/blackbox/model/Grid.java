package com.joffrey_bion.games.blackbox.model;

import java.util.HashMap;
import java.util.Random;

import com.joffrey_bion.utils.strings.BoxDrawing;

public class Grid {

    protected final int SIZE;
    protected BallList balls;
    protected HashMap<Side, EntryPort[]> entryPorts;
    private int detourCount = 0;
    
    /**
     * Creates a new empty {@code Grid}.
     * 
     * @param size
     *            The size of the grid (length of one side).
     */
    private Grid(int size) {
        SIZE = size;
        balls = new BallList();
        entryPorts = new HashMap<>();
        for (Side s : Side.values()) {
            EntryPort[] ports = new EntryPort[SIZE];
            for (int i = 0; i < SIZE; i++) {
                ports[i] = new EntryPort(s, i);
            }
            entryPorts.put(s, ports);
        }
    }

    /**
     * Create a grid with the specified balls.
     * 
     * @param size
     *            The size of the grid (length of one side).
     * @param balls
     *            The balls to place in this {@code Grid}.
     */
    public Grid(int size, BallList balls) {
        this(size);
        this.balls = balls;
    }

    /**
     * Create a grid with {@code nBalls} balls placed randomly.
     * 
     * @param size
     *            The size of the grid (length of one side).
     * @param nBalls
     *            Number of balls to generate.
     */
    public Grid(int size, int nBalls) {
        this(size);
        Random rand = new Random();
        for (int k = 0; k < nBalls; k++) {
            balls.add(rand.nextInt(SIZE), rand.nextInt(SIZE));
        }
    }

    public int getSize() {
        return SIZE;
    }

    public int getNBalls() {
        return balls.size();
    }

    /**
     * Return whether there is a ball at the specified position. The position may be
     * out of the grid bounds. In this case, {@code false} is returned.
     * 
     * @param i
     *            The row number to check.
     * @param j
     *            The col number to check.
     * @return {@code true} if a ball is at the specified position.
     */
    protected boolean isBall(int i, int j) {
        return balls.contains(i, j);
    }

    /**
     * Shoots a ray from every {@link EntryPort}. After a call to this method, no
     * {@code EntryPort} can be in the {@link PortState#UNKNOWN} state .
     */
    public void shootAll() {
        for (Side s : Side.values()) {
            for (int i = 0; i < SIZE; i++) {
                shoot(s, i);
            }
        }
    }

    /**
     * Shoots a ray from the specified {@link Side}, at the specified position on
     * this side.
     * 
     * @param side
     *            The {@code Side} to shoot from.
     * @param index
     *            The index of the {@link EntryPort} on the specified {@code Side}.
     * @return {@code true} if the entry port had never been tried, {@code false}
     *         otherwise. If {@code false} is returned, nothing has been done.
     */
    public boolean shoot(Side side, int index) {
        EntryPort ep = entryPorts.get(side)[index];
        if (ep.getState() != PortState.UNKNOWN) {
            return false;
        }
        int i, j;
        switch (side) {
        case TOP:
            i = -1;
            j = index;
            break;
        case BOTTOM:
            i = SIZE;
            j = index;
            break;
        case LEFT:
            i = index;
            j = -1;
            break;
        case RIGHT:
        default:
            i = index;
            j = SIZE;
        }
        propagateRay(ep, i, j, side.getDirection());
        return true;
    }

    /**
     * Propagates a ray in the specified {@link Direction}, from the specified
     * position.
     * 
     * @param ep
     *            The {@code EntryPort} the ray has been shot from.
     * @param i
     *            The current row.
     * @param j
     *            The current column.
     * @param dir
     *            The {@code Direction} to follow.
     * @return The destination {@code EntryPort} (the twin) in case of a
     *         {@link PortState#DETOUR}, {@code null} otherwise.
     */
    private EntryPort propagateRay(EntryPort ep, int i, int j, Direction dir) {
        int iStraight = i, jStraight = j;
        int iFrontLeft = i, jFrontLeft = j;
        int iFrontRight = i, jFrontRight = j;
        switch (dir) {
        case UP:
            iStraight--;
            iFrontLeft--;
            jFrontLeft--;
            iFrontRight--;
            jFrontRight++;
            break;
        case DOWN:
            iStraight++;
            iFrontLeft++;
            jFrontLeft++;
            iFrontRight++;
            jFrontRight--;
            break;
        case LEFT:
            jStraight--;
            iFrontLeft++;
            jFrontLeft--;
            iFrontRight--;
            jFrontRight--;
            break;
        case RIGHT:
            jStraight++;
            iFrontLeft--;
            jFrontLeft++;
            iFrontRight++;
            jFrontRight++;
            break;
        }
        // the ray hits
        if (isBall(iStraight, jStraight)) {
            ep.setState(PortState.HIT);
            return null;
        }
        // the ray moves
        int iNext, jNext;
        Direction dirNext;
        boolean ballFrontRight = isBall(iFrontRight, jFrontRight);
        boolean ballFrontLeft = isBall(iFrontLeft, jFrontLeft);
        if (ballFrontLeft) {
            if (ballFrontRight) {
                // go back
                iNext = i;
                jNext = j;
                dirNext = dir.opposite();
            } else {
                // turn right
                iNext = i;
                jNext = j;
                dirNext = dir.turnRight();
            }
        } else {
            if (ballFrontRight) {
                // turn left
                iNext = i;
                jNext = j;
                dirNext = dir.turnLeft();
            } else {
                // go straight
                iNext = iStraight;
                jNext = jStraight;
                dirNext = dir;
            }
        }
        if (isOnEdge(iNext, jNext)) {
            // running into an edge
            EntryPort dest = getEntryPort(iNext, jNext);
            if (dest == ep) {
                ep.setState(PortState.REFLECT);
                return null;
            } else {
                ep.setDetourTo(dest, detourCount);
                detourCount++;
                return dest;
            }
        }
        return propagateRay(ep, iNext, jNext, dirNext);
    }

    /**
     * Returns whether the specified position in out of the grid, just on the edge.
     * 
     * @param i
     *            The row to check.
     * @param j
     *            The column to check.
     * @return {@code true} if the specified position in out of the grid, just on the
     *         edge.
     */
    private boolean isOnEdge(int i, int j) {
        return i == -1 || i == SIZE || j == -1 || j == SIZE;
    }

    /**
     * Returns the {@link EntryPort} located at the specified position.
     * 
     * @param side
     *            The {@link Side} of the {@code EntryPort} to get.
     * @param index
     *            The index of the {@code EntryPort} to get.
     * @return The {@code EntryPort} located at the specified position.
     */
    public EntryPort getEntryPort(Side side, int index) {
        return entryPorts.get(side)[index];
    }

    /**
     * Returns the {@link EntryPort} located at the specified position.
     * 
     * @param i
     *            The row of the {@code EntryPort} to get.
     * @param j
     *            The column of the {@code EntryPort} to get.
     * @return The {@code EntryPort} located at the specified position if this
     *         position verifies {@link #isOnEdge(int, int)}, {@code null} otherwise.
     */
    private EntryPort getEntryPort(int i, int j) {
        if (i == -1) {
            return entryPorts.get(Side.TOP)[j];
        } else if (i == SIZE) {
            return entryPorts.get(Side.BOTTOM)[j];
        } else if (j == -1) {
            return entryPorts.get(Side.LEFT)[i];
        } else if (j == SIZE) {
            return entryPorts.get(Side.RIGHT)[i];
        } else {
            return null;
        }
    }

    /**
     * Returns a {@code String} representation of the {@link EntryPort}s of the
     * specified {@link Side}.
     * 
     * @param side
     *            The {@code Side} of the {@code EntryPort}s to display.
     * @return a {@code String} representation of the {@code EntryPort}s of the
     *         specified {@code Side}.
     */
    private String getEntryPortsStr(Side side) {
        EntryPort[] eps = entryPorts.get(side);
        StringBuilder sb = new StringBuilder();
        for (EntryPort ep : eps) {
            sb.append(ep.stateToString());
        }
        return sb.toString();
    }
    
    /**
     * Returns a {@code String} representation of this grid.
     * 
     * @param ballsToDisplay
     *            The balls to display in the grid.
     * @return a {@code String} representation of this grid.
     */
    protected String toString(BallList ballsToDisplay) {
        StringBuilder sb = new StringBuilder();
        String newLine = BoxDrawing.NEW_LINE;
        EntryPort[] epLeft = entryPorts.get(Side.LEFT);
        EntryPort[] epRight = entryPorts.get(Side.RIGHT);
        sb.append("  ");
        sb.append(getEntryPortsStr(Side.TOP));
        sb.append(newLine);
        for (int i = 0; i < SIZE; i++) {
            sb.append(epLeft[i].stateToString());
            sb.append(BoxDrawing.LIGHT_VERTICAL);
            for (int j = 0; j < SIZE; j++) {
                if (balls.contains(i, j)) {
                    sb.append("O");
                } else {
                    sb.append(" ");
                }
            }
            sb.append(BoxDrawing.LIGHT_VERTICAL);
            sb.append(epRight[i].stateToString());
            sb.append(newLine);
        }
        sb.append("  ");
        sb.append(getEntryPortsStr(Side.BOTTOM));
        sb.append(newLine);
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(balls);
    }
}
