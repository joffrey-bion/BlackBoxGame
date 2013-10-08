package com.joffrey_bion.games.blackbox.model;

import java.util.HashMap;
import java.util.Random;

public class Grid {

    private static final int SIZE = 8;

    private boolean[][] balls;
    private HashMap<Side, EntryPort[]> entryPorts;

    public Grid(int nBalls) {
        balls = new boolean[SIZE][SIZE];
        randomizeBalls(nBalls);
        entryPorts = new HashMap<>();
        for (Side s : Side.values()) {
            EntryPort[] ports = new EntryPort[SIZE];
            for (int i = 0; i < SIZE; i++) {
                ports[i] = new EntryPort(s, i);
            }
            entryPorts.put(s, ports);
        }
    }

    private void randomizeBalls(int nBalls) {
        Random rand = new Random();
        for (int k = 0; k < nBalls; k++) {
            int i = rand.nextInt(SIZE);
            int j = rand.nextInt(SIZE);
            balls[i][j] = true;
        }
    }

    public boolean isBall(int i, int j) {
        if (i < 0 || i >= SIZE || j < 0 || j >= SIZE) {
            return false;
        }
        return balls[i][j];
    }

    public EntryPort shoot(Side side, int index) {
        EntryPort ep = entryPorts.get(side)[index];
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
        return ep;
    }

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
                ep.setState(PortState.DETOUR);
                dest.setState(PortState.DETOUR);
                ep.setTwin(dest);
                dest.setTwin(ep);
                return dest;
            }
        }
        return propagateRay(ep, iNext, jNext, dirNext);
    }

    private static boolean isOnEdge(int i, int j) {
        return i == -1 || i == SIZE || j == -1 || j == SIZE;
    }

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

    private String getEntryPortsStr(Side side) {
        EntryPort[] eps = entryPorts.get(side);
        StringBuilder sb = new StringBuilder();
        for (EntryPort ep : eps) {
            sb.append(ep.toString());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        EntryPort[] epLeft = entryPorts.get(Side.LEFT);
        EntryPort[] epRight = entryPorts.get(Side.RIGHT);
        sb.append("  ");
        sb.append(getEntryPortsStr(Side.TOP));
        sb.append(newLine);
        for (int i = 0; i < SIZE; i++) {
            sb.append(epLeft[i].toString());
            sb.append("|");
            for (int j = 0; j < SIZE; j++) {
                if (balls[i][j]) {
                    sb.append("O");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("|");
            sb.append(epRight[i].toString());
            sb.append(newLine);
        }
        sb.append("  ");
        sb.append(getEntryPortsStr(Side.BOTTOM));
        sb.append(newLine);
        return sb.toString();
    }
}
