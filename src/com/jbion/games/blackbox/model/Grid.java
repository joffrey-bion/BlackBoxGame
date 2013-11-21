package com.jbion.games.blackbox.model;

import java.util.Random;

import com.jbion.games.blackbox.model.ports.Ports;
import com.jbion.games.blackbox.model.ports.State;
import com.jbion.games.blackbox.model.propagation.Propagator;
import com.jbion.utils.grids.BoxChars;
import com.jbion.utils.grids.GridDrawer;

/**
 * Represents a grid of the BlackBox game.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
public class Grid {

    /**
     * The size of all {@code Grid}s.
     */
    public static Integer size = null;

    /**
     * The ports to shoot rays from in this {@code Grid}.
     */
    protected Ports entryPorts;

    protected Propagator propagator;

    /**
     * Creates a new empty {@code Grid}.
     */
    private Grid() {
        entryPorts = new Ports();
        propagator = new Propagator(new BallList());
    }

    /**
     * Create a grid with the specified balls.
     * 
     * @param balls
     *            The balls to place in this {@code Grid}.
     */
    public Grid(BallList balls) {
        this();
        this.propagator.balls = balls;
    }

    /**
     * Create a grid with {@code nBalls} balls placed randomly.
     * 
     * @param nBalls
     *            Number of balls to generate.
     */
    public Grid(int nBalls) {
        this();
        Random rand = new Random();
        for (int k = 0; k < nBalls; k++) {
            propagator.balls.add(rand.nextInt(size), rand.nextInt(size));
        }
    }

    /**
     * Returns the number of {@link Ball}s hidden in this {@code Grid}.
     * 
     * @return the number of {@link Ball}s hidden in this {@code Grid}.
     */
    public int getNBalls() {
        return propagator.balls.size();
    }

    /**
     * Returns the {@link State} of the specified port.
     * 
     * @param side
     *            The {@link Side} of the port to look for.
     * @param index
     *            The index of the port to look for.
     * @return the {@link State} of the specified port.
     */
    public State getState(Side side, int index) {
        return entryPorts.getState(side, index);
    }

    /**
     * Returns the {@link State} of the specified port.
     * 
     * @param port
     *            The number of the port to look for.
     * @return the {@link State} of the specified port.
     */
    public State getState(int port) {
        return entryPorts.getState(port);
    }

    public Ports getPorts() {
        return entryPorts;
    }

    /**
     * Shoots a ray from every port. After a call to this method, no port can be in
     * the {@link State#UNKNOWN} state .
     */
    public void shootAll() {
        for (Side s : Side.values()) {
            for (int i = 0; i < size; i++) {
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
     *            The index of the port within the specified {@code Side}.
     * @return The twin of the tested port if the result is a {@link State#DETOUR},
     *         {@code null} otherwise.
     */
    public Integer shoot(Side side, int index) {
        int port = Ports.getPortNum(side, index);
        Integer dest = propagator.propagateRay(side, index);
        if (dest == null) {
            entryPorts.setHit(port);
        } else if (dest == port) {
            entryPorts.setReflect(port);
        } else {
            entryPorts.setDetour(port, dest);
        }
        return dest;
    }

    /**
     * Shoots a ray from the specified port.
     * 
     * @param port
     *            The port to shoot from.
     * @return The twin of the tested port if the result is a {@link State#DETOUR},
     *         {@code null} otherwise.
     */
    public Integer shoot(int port) {
        Side side = Ports.getSide(port);
        int index = Ports.getIndex(port);
        return shoot(side, index);
    }

    private static final String BALL = " " + BoxChars.BLACK_CIRCLE;
    private static final String SPACE = " " + BoxChars.MIDDLE_DOT;

    private static final char H = BoxChars.BOX_DRAWINGS_LIGHT_HORIZONTAL;
    private static final char HD = H;
    private static final char V = BoxChars.BOX_DRAWINGS_LIGHT_VERTICAL;
    private static final char VD = V;
    private static final char ULC = BoxChars.BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT;
    private static final char DLC = BoxChars.BOX_DRAWINGS_LIGHT_UP_AND_RIGHT;
    private static final char URC = BoxChars.BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT;
    private static final char DRC = BoxChars.BOX_DRAWINGS_LIGHT_UP_AND_LEFT;
    private static final char CROSS = BoxChars.BOX_DRAWINGS_LIGHT_VERTICAL_AND_HORIZONTAL;

    /**
     * Returns a {@code String} representation of the ports of the specified
     * {@link Side}.
     * 
     * @param side
     *            The {@code Side} of the {@code EntryPort}s to display.
     * @return a {@code String} representation of the ports' states of the specified
     *         {@code Side}.
     */
    private String getEntryPortsStr(Side side) {
        StringBuilder sb = new StringBuilder();
        sb.append(V);
        for (int i = 0; i < size; i++) {
            String state = entryPorts.getStateString(side, i);
            sb.append(state.length() > 1 ? state : " " + state);
        }
        sb.append(V);
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
        String newLine = BoxChars.NEW_LINE;
        sb.append(GridDrawer.repeat(H, size, 2, "", "   " + ULC, URC));
        sb.append(newLine);
        sb.append("   ");
        sb.append(getEntryPortsStr(Side.TOP));
        sb.append(newLine);
        sb.append(GridDrawer.repeat(HD, size, 2, "", ULC + GridDrawer.repeat(H, 2) + CROSS, CROSS
                + GridDrawer.repeat(H, 2) + URC));
        sb.append(newLine);
        for (int i = 0; i < size; i++) {
            sb.append(V);
            String state = entryPorts.getStateString(Side.LEFT, i);
            sb.append(state.length() > 1 ? state : " " + state);
            sb.append(VD);
            for (int j = 0; j < size; j++) {
                if (ballsToDisplay.contains(i, j)) {
                    sb.append(BALL);
                } else {
                    sb.append(SPACE);
                }
            }
            sb.append(VD);
            state = entryPorts.getStateString(Side.RIGHT, i);
            sb.append(state.length() > 1 ? state : " " + state);
            sb.append(V);
            sb.append(newLine);
        }
        sb.append(GridDrawer.repeat(HD, size, 2, "", DLC + GridDrawer.repeat(H, 2) + CROSS, CROSS
                + GridDrawer.repeat(H, 2) + DRC));
        sb.append(newLine);
        sb.append("   ");
        sb.append(getEntryPortsStr(Side.BOTTOM));
        sb.append(newLine);
        sb.append(GridDrawer.repeat(H, size, 2, "", "   " + DLC, DRC));
        sb.append(newLine);
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(propagator.balls);
    }
}
