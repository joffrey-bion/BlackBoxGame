package com.joffrey_bion.games.blackbox.model;

public class EntryPort {

    private Side side;
    private int index;
    private PortState state;
    private int detourNumber;
    private EntryPort twin;

    public EntryPort(Side side, int index) {
        this.side = side;
        this.index = index;
        this.state = PortState.UNKNOWN;
        this.detourNumber = -1;
        this.twin = null;
    }

    public Side getSide() {
        return side;
    }

    public int getIndex() {
        return index;
    }

    public void setState(PortState state) {
        this.state = state;
    }

    public PortState getState() {
        return state;
    }

    public void setTwin(EntryPort twin) {
        this.twin = twin;
    }

    void setDetourTo(EntryPort twin, int detourNumber) {
        if (this.twin != null) {
            throw new RuntimeException("Twin already set.");
        }
        this.twin = twin;
        this.state = PortState.DETOUR;
        this.detourNumber = detourNumber;
        twin.twin = this;
        twin.state = PortState.DETOUR;
        twin.detourNumber = detourNumber;
    }

    public boolean equals(EntryPort ep) {
        boolean equals = side == ep.side && index == ep.index && state == ep.state;
        if (equals && state == PortState.DETOUR) {
            return twin.side == ep.twin.side && twin.index == ep.twin.index;
        }
        return equals;
    }

    public String stateToString() {
        switch (state) {
        case UNKNOWN:
            return " ";
        case HIT:
            return "H";
        case REFLECT:
            return "R";
        case DETOUR:
            return Integer.toString(detourNumber);
        }
        return null;
    }

    public String positionToString() {
        return side + "-" + index;
    }

    @Override
    public String toString() {
        return positionToString() + " (" + state + (twin == null ? "" : " to " + twin.positionToString())
                + ")";
    }
}
