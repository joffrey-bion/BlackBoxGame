package org.hildan.games.blackbox.model.ports;

/**
 * An enum listing the possible states for an entry port.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey Bion</a>
 */
public enum State {
    /**
     * Not tested yet.
     */
    UNKNOWN,
    /**
     * The ray shot from the port ran into a ball.
     */
    HIT,
    /**
     * The ray shot from the port came back to this same port.
     */
    REFLECT,
    /**
     * A ray shot from another port arrived here.
     */
    DETOUR;
}
