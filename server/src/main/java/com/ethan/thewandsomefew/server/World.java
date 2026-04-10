/**
 * File: World.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 4/9/2026
 *
 * Purpose:
 *   This file is responsible for defining the World state, and performing
 *   logic associated with various entities (only Player for now, but eventually
 *   will include NPCs, Quests, etc.)
 */
package com.ethan.thewandsomefew.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.ethan.thewandsomefew.protocol.packets.PlayerPositionPacket;

/**
 * The World class defines and tracks the current World state, performing
 * specific actions on tick for entities within the World.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Defines and tracks entire World state</li>
 * <li>Updates state and entities within on tick</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 * <li>Current implementation is very simple, storing only a single Player, but
 * will eventually include all related World objects such as NPCs, Quests,
 * Objects, etc.</li>
 * </ul>
 */
public final class World {

    private final ConcurrentLinkedQueue<PlayerAction> actions;
    private final Map<ClientSession, ConnectedPlayer> map;
    private static final AtomicInteger nextId = new AtomicInteger(0);

    public World() {
        actions = new ConcurrentLinkedQueue<>();
        map = new HashMap<>();
    }

    private void connectPlayer(ClientSession client, Player player) {
        map.put(client, new ConnectedPlayer(nextId.incrementAndGet(), player, client));
    }

    private void disconnectPlayer(ClientSession client) {
        map.remove(client);
    }

    private Player getPlayerFromClient(ClientSession client) {
        return map.get(client).player();
    }

    public void submitAction(PlayerAction action) {
        actions.add(action);
    }

    private void processActions() {
        PlayerAction action;
        while ((action = actions.poll()) != null) {
            switch (action) {
                case PlayerAction.Connect(ClientSession client, Player player) ->
                    connectPlayer(client, player);
                case PlayerAction.Disconnect(ClientSession client) ->
                    disconnectPlayer(client);
                case PlayerAction.Walk(ClientSession client, int x, int y) ->
                    getPlayerFromClient(client).setWalkTarget(x, y);
            }
        }
    }

    // tick() currently only updates and logs player movement towards a target position,
    // but will need to be expanded upon when new actions and entities are introduced
    // to the World
    public void tick() {
        processActions();

        map.forEach((client, connectedPlayer) -> {
            if (client != null) {
                connectedPlayer.player().tickMovement();
                try {
                    client.sendPacket(new PlayerPositionPacket(connectedPlayer.player().x(), connectedPlayer.player().y()));
                } catch (IOException e) {
                    System.err.println("Error sending player position to client: " + e.getMessage());
                    e.printStackTrace();
                    submitAction(new PlayerAction.Disconnect(client));
                }
                System.out.println("Player " + connectedPlayer.id() + " Position: x=" + connectedPlayer.player().x() + ", y=" + connectedPlayer.player().y());
            }
        });
    }
}
