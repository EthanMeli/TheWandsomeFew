/**
 * File: Clientstate.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 4/20/2026
 * Last Modified: 4/28/2026
 *
 * Purpose:
 *   Key information about state of the world for client side
 *   Equivalent of World object for the client
 */
package com.ethan.thewandsomefew.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientState {
    
    private final Map<Integer, PlayerState> players = new HashMap<>();
    private final Map<Integer, NpcState> npcs = new HashMap<>();
    private int localPlayerId;

    public ClientState() {

    }

    // --- Player-related functions ---
    public void addPlayer(int id, int x, int y) {
        players.put(id, new PlayerState(id, x, y));
    }

    public void removePlayer(int id) {
        players.remove(id);
    }

    public void updatePlayerPosition(int id, int x, int y) {
        addPlayer(id, x, y); // overwrites existing entry
    }

    public void setLocalPlayerId(int id) {
        localPlayerId = id;
    }

    public PlayerState getLocalPlayer() {
        return players.get(localPlayerId);
    }

    public Collection<PlayerState> getPlayers() {
        return players.values();
    }

    // --- NPC-related functions ---
    public void addNpc(int id, int type, int x, int y) {
        npcs.put(id, new NpcState(id, type, x, y));
    }

    public void removeNpc(int id) {
        npcs.remove(id);
    }

    public void updateNpcPosition(int id, int x, int y) {
        NpcState existing = npcs.get(id);
        if (existing == null) {
            return;
        }
        npcs.put(id, new NpcState(id, existing.type(), x, y));
    }

    public Collection<NpcState> getNpcs() {
        return npcs.values();
    }
}
