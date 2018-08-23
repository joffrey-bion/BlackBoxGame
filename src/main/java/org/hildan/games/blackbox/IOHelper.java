package org.hildan.games.blackbox;

import java.util.Scanner;

import org.hildan.games.blackbox.model.Grid;
import org.hildan.games.blackbox.model.Side;
import org.hildan.games.blackbox.model.ports.State;

public class IOHelper {

    private final Scanner sc;

    public IOHelper() {
        this.sc = new Scanner(System.in);
    }

    public int getNumberOfBalls() {
        System.out.print("Enter number of balls to find: ");
        return getIntegerBetween(1, Grid.size);
    }

    public Side getSide() {
        Side side = null;
        while (sc.hasNext()) {
            String sideStr = sc.next();
            try {
                side = Side.valueOf(sideStr.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("TOP, BOTTOM, LEFT or RIGHT expected.");
            }
        }
        return side;
    }

    public State getState() {
        State state = null;
        while (sc.hasNext()) {
            String stateStr = sc.next();
            try {
                if (stateStr.equalsIgnoreCase("UNKNOWN")) {
                    throw new IllegalArgumentException();
                }
                state = State.valueOf(stateStr.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("HIT, REFLECT or DETOUR expected.");
            }
        }
        return state;
    }

    public int getIndex() {
        return getIntegerBetween(0, Grid.size - 1);
    }

    public int getIntegerBetween(int min, int max) {
        int i = -1;
        while (sc.hasNext()) {
            if (!sc.hasNextInt()) {
                System.err.println("Please enter an integer.");
                sc.next();
            } else {
                i = sc.nextInt();
                if (i >= min && i <= max) {
                    break;
                } else {
                    System.err.println("Please enter a value between " + min + " and " + max + ".");
                }
            }
        }
        return i;
    }

    public boolean getYesNo() {
        while (sc.hasNext()) {
            String s = sc.next();
            if (s.equalsIgnoreCase("Y")) {
                return true;
            } else if (s.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("Please enter Y or N.");
            }
        }
        throw new RuntimeException("Unexpected end of input for a yes/no question.");
    }
}
