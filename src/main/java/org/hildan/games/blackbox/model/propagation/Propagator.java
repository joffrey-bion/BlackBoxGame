package org.hildan.games.blackbox.model.propagation;

import org.hildan.games.blackbox.model.BallList;
import org.hildan.games.blackbox.model.Grid;
import org.hildan.games.blackbox.model.Side;
import org.hildan.games.blackbox.model.ports.Ports;
import org.hildan.games.blackbox.model.ports.State;

public class Propagator {

    public BallList balls;

    public Propagator(BallList balls) {
        this.balls = balls;
    }

    /**
     * Return whether there is a ball at the specified position. The position may be
     * out of the grid bounds. In this case, {@code false} is returned.
     * 
     * @param pos
     *            The position to check.
     * @return {@code true} if a ball is at the specified position.
     */
    public boolean isBall(Position pos) {
        return balls.contains(pos.row, pos.col);
    }

    /**
     * Propagates a ray from the specified port.
     * 
     * @param side
     *            The {@code Side} to shoot from.
     * @param index
     *            The index of the port within the specified {@code Side}.
     * @return The number of the reached port, or {@code null} in case of a
     *         {@link State#HIT}.
     */
    public Integer propagateRay(Side side, int index) {
        int port = Ports.getPortNum(side, index);
        Position pos = Position.get(side, index);
        return propagateRay(port, pos, side.getDirection());
    }

    /**
     * Propagates a ray in the specified {@link Direction}, from the specified
     * position.
     * 
     * @param port
     *            The entry port the ray has been shot from.
     * @param pos
     *            The current position of the ray.
     * @param dir
     *            The {@code Direction} to follow.
     * @return The number of the reached port, or {@code null} in case of a
     *         {@link State#HIT}.
     */
    public Integer propagateRay(int port, Position pos, Direction dir) {
        Position straight = new Position(pos);
        Position frontLeft = new Position(pos);
        Position frontRight = new Position(pos);
        straight.move(dir);
        frontLeft.move(dir);
        frontLeft.move(dir.turnLeft());
        frontRight.move(dir);
        frontRight.move(dir.turnRight());
        // the ray hits
        if (isBall(straight)) {
            return null;
        }
        // the ray moves
        Position next = pos;
        Direction dirNext;
        boolean ballFrontRight = isBall(frontRight);
        boolean ballFrontLeft = isBall(frontLeft);
        if (ballFrontLeft) {
            if (ballFrontRight) {
                // go back
                dirNext = dir.opposite();
            } else {
                // turn right
                dirNext = dir.turnRight();
            }
        } else {
            if (ballFrontRight) {
                // turn left
                dirNext = dir.turnLeft();
            } else {
                // go straight
                next = straight;
                dirNext = dir;
            }
        }
        if (isOnEdge(next)) {
            // running into an edge
            return getEntryPort(next);
        }
        return propagateRay(port, next, dirNext);
    }

    /**
     * Returns whether the specified position in out of the grid, just on the edge.
     * 
     * @param pos
     *            The position to check.
     * @return {@code true} if the specified position in out of the grid, just on the
     *         edge, {@code false} otherwise.
     */
    private static boolean isOnEdge(Position pos) {
        return pos.row == -1 || pos.row == Grid.size || pos.col == -1 || pos.col == Grid.size;
    }

    /**
     * Returns the number of the port located at the specified position. The
     * specified position must verify {@link #isOnEdge(Position)}, an exception is
     * thrown otherwise.
     * 
     * @param pos
     *            The position of the port to get.
     * @return The number of the port located at the specified position.
     */
    private static int getEntryPort(Position pos) {
        if (pos.row == -1) {
            return Ports.getPortNum(Side.TOP, pos.col);
        } else if (pos.row == Grid.size) {
            return Ports.getPortNum(Side.BOTTOM, pos.col);
        } else if (pos.col == -1) {
            return Ports.getPortNum(Side.LEFT, pos.row);
        } else if (pos.col == Grid.size) {
            return Ports.getPortNum(Side.RIGHT, pos.row);
        } else {
            throw new IllegalArgumentException("The specified position is not on an edge.");
        }
    }

}
