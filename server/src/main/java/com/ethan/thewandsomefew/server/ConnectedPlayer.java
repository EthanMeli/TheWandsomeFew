/**
 * File: ConnectedPlayer.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/4/2026
 * Last Modified: 4/27/2026
 *
 * Purpose:
 *   To provide an abstraction for a connected player and their associated
 *   network session.
 */
package com.ethan.thewandsomefew.server;

/**
 * The ConnectedPlayer class exists so World can treat (session + player)
 * as a single unit rather than managing parallel maps
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Store a unique id, Player object, and Client Session for each
 * ConnectedPlayer</li>
 * </ul>
 */
public final class ConnectedPlayer {

    private final Player player;
    private final ClientSession clientSession;

    public ConnectedPlayer(Player player, ClientSession clientSession) {
        this.player = player;
        this.clientSession = clientSession;
    }

    public Player player() {
        return player;
    }

    public ClientSession clientSession() {
        return clientSession;
    }
}
