package com.jbion.games.blackbox.model.ports;

import java.util.Arrays;

import com.jbion.games.blackbox.model.Grid;
import com.jbion.games.blackbox.model.Side;

public class Ports {

    private State[] states;
    private DetourList detours;

    public Ports() {
        states = new State[size()];
        Arrays.fill(states, State.UNKNOWN);
        detours = new DetourList();
    }
    
    public static int size() {
        return Grid.size * 4;
    }

    public static int getPortNum(Side side, int index) {
        return side.ordinal() * Grid.size + index;
    }

    private static final Side[] SIDES = Side.values();

    public static Side getSide(int port) {
        return SIDES[port / Grid.size];
    }

    public static int getIndex(int port) {
        return port % Grid.size;
    }

    public boolean isKnown(int port) {
        return getState(port) != State.UNKNOWN;
    }

    public State getState(Side side, int index) {
        return states[getPortNum(side, index)];
    }

    public State getState(int port) {
        return states[port];
    }

    private void setState(int port, State state) {
        states[port] = state;
    }

    public void resetState(int port) {
        if (getState(port) == State.DETOUR) {
            int twin = detours.remove(port);
            setState(twin, State.UNKNOWN);
        }
        setState(port, State.UNKNOWN);
    }

    public void setHit(int port) {
        setState(port, State.HIT);
    }

    public void setReflect(int port) {
        setState(port, State.REFLECT);
    }

    public void setDetour(int port, int twin) {
        setState(port, State.DETOUR);
        setState(twin, State.DETOUR);
        detours.add(port, twin);
    }

    public boolean isConsistent(int port, Ports compared) {
        State state = getState(port);
        if (compared.getState(port) != state) {
            return false;
        }
        return state != State.DETOUR || compared.detours.getTwin(port) == detours.getTwin(port);
    }

    /**
     * Returns a {@code String} representing the current state of the specified port.
     * 
     * @param port
     *            The port to get the state representation from.
     * 
     * @return a space if this port has not been tested yet, "H" for a Hit, "R" for a
     *         reflection, and an integer representing the detour number in case of a
     *         detour.
     */
    public String getStateString(int port) {
        switch (getState(port)) {
        case UNKNOWN:
            return " ";
        case HIT:
            return "H";
        case REFLECT:
            return "R";
        case DETOUR:
            return Integer.toString(detours.getDetourNumber(port));
        }
        throw new RuntimeException("Impossible state: " + getState(port));
    }

    /**
     * Returns a {@code String} representing the current state of the specified port.
     * 
     * @param side
     *            The side of the port to get the state representation from.
     * @param index
     *            The index of the port to get the state representation from.
     * 
     * @return a space if this port has not been tested yet, "H" for a Hit, "R" for a
     *         reflection, and an integer representing the detour number in case of a
     *         detour.
     */
    public String getStateString(Side side, int index) {
        return getStateString(getPortNum(side, index));
    }
}
