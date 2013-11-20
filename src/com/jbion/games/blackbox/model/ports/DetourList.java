package com.jbion.games.blackbox.model.ports;

import java.util.ArrayList;

class DetourList {

    private ArrayList<Detour> detours;

    public DetourList() {
        detours = new ArrayList<>();
    }

    public void add(int port1, int port2) {
        detours.add(new Detour(port1, port2));
    }

    /**
     * Removes the detour corresponding to the specified port.
     * 
     * @param port
     *            The port to remove the detour for.
     * @return The former twin of the specified port.
     */
    public int remove(int port) {
        Detour d = detours.remove(getDetourNumber(port));
        if (d.port1 == port) {
            return d.port2;
        } else {
            return d.port1;
        }
    }

    public Detour getDetour(int port) {
        for (Detour d : detours) {
            if (d.port1 == port || d.port2 == port) {
                return d;
            }
        }
        throw new RuntimeException("Detour not found.");
    }

    public int getTwin(int port) {
        Detour d = getDetour(port);
        if (d.port1 == port) {
            return d.port2;
        } else {
            return d.port1;
        }
    }

    public int getDetourNumber(int port) {
        for (int i = 0; i < detours.size(); i++) {
            Detour d = detours.get(i);
            if (d.port1 == port || d.port2 == port) {
                return i;
            }
        }
        throw new RuntimeException("Detour not found.");
    }

    private class Detour {
        int port1;
        int port2;

        public Detour(int port1, int port2) {
            this.port1 = port1;
            this.port2 = port2;
        }
    }
}