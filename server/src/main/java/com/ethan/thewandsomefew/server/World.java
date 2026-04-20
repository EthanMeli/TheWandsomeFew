/**
 * File: World.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 4/20/2026
 *
 * Purpose:
 *   This file is responsible for defining the World state, and performing
 *   logic associated with various entities (only Player for now, but eventually
 *   will include NPCs, Quests, etc.)
 */
package com.ethan.thewandsomefew.server;

import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.packets.PlayerJoinPacket;
import com.ethan.thewandsomefew.protocol.packets.PlayerLeavePacket;
import com.ethan.thewandsomefew.protocol.packets.PlayerPositionPacket;
import com.ethan.thewandsomefew.protocol.packets.WelcomePacket;

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
    private final Map<ClientSession, ConnectedPlayer> clientPlayerMap;
    private final TileMap worldTileMap;
    private final Tile[][] tileMap;
    private final BfsPathFinder pathFinder;
    private final AtomicInteger nextId = new AtomicInteger(0);

    public World() {
        actions = new ConcurrentLinkedQueue<>();
        clientPlayerMap = new HashMap<>();
        worldTileMap = new TileMap();
        tileMap = worldTileMap.map();
        pathFinder = new BfsPathFinder(worldTileMap);
    }

    private void connectPlayer(ClientSession client, Player player) {
        int newId = nextId.incrementAndGet();
        ConnectedPlayer newPlayer = new ConnectedPlayer(newId, player, client);

        // 1: Tell every existing player about the new one
        // (do this BEFORE adding to map as to not send to ourselves twice)
        for (ConnectedPlayer existing : clientPlayerMap.values()) {
            trySend(existing.clientSession(), new PlayerJoinPacket(newId, player.x(), player.y()));
        }

        // 2: Add the new player to the map
        clientPlayerMap.put(client, newPlayer);

        // 3: Send player's client respective playerId to control client state
        trySend(client, new WelcomePacket(newId));

        // 4: Tell the new player about everyone in the world (including themselves)
        for (ConnectedPlayer existing : clientPlayerMap.values()) {
            trySend(client, new PlayerJoinPacket(existing.id(), existing.player().x(), existing.player().y()));
        }
    }

    private void disconnectPlayer(ClientSession client) {
        ConnectedPlayer leaving = clientPlayerMap.get(client);
        if (leaving == null) return;

        // Remove first so we don't try to send a leave packet to the leaver
        clientPlayerMap.remove(client);

        // Tell every other client
        for (ConnectedPlayer remaining : clientPlayerMap.values()) {
            trySend(remaining.clientSession(), new PlayerLeavePacket(leaving.id()));
        }
    }

    private void setPlayerPath(ClientSession client, int x, int y) {
        Player p = getPlayerFromClient(client);
        Tile from = tileMap[p.x()][p.y()];
        Set<Tile> acceptableTargets = new HashSet<>();
        acceptableTargets.add(tileMap[x][y]);

        Deque<Tile> path = pathFinder.findPath(from, acceptableTargets);
        p.setPath(path);
    }

    private Player getPlayerFromClient(ClientSession client) {
        return clientPlayerMap.get(client).player();
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
                    setPlayerPath(client, x, y);
            }
        }
    }

    /** Helper function to send a packet to a client, queueing a disconnect if the send fails. */
    private void trySend(ClientSession client, Packet packet) {
        try {
            client.sendPacket(packet);
        } catch (IOException e) {
            System.err.println("Send failed, queueing disconnect: " + e.getMessage());
            submitAction(new PlayerAction.Disconnect(client));
        }
    }

    // tick() currently only updates and logs player movement towards a target position,
    // but will need to be expanded upon when new actions and entities are introduced
    // to the World
    public void tick() {
        processActions();

        // recipient of packet
        clientPlayerMap.forEach((client, connectedPlayer) -> {
            connectedPlayer.player().tickMovement();
            System.out.println("Player " + connectedPlayer.id() + " Position: x=" + connectedPlayer.player().x() + ", y=" + connectedPlayer.player().y());
        });
        // subject (whose position is sent)
        clientPlayerMap.forEach((clientRecipient, recipient) -> {
            clientPlayerMap.forEach((clientSubject, subject) -> {
                try {
                    clientRecipient.sendPacket(
                            new PlayerPositionPacket(
                                    subject.id(),
                                    subject.player().x(),
                                    subject.player().y()
                            )
                    );
                } catch (IOException e) {
                    System.err.println("Error sending player position to client " + recipient.id() + ": " + e.getMessage());
                    submitAction(new PlayerAction.Disconnect(clientRecipient));
                    return;
                }
            });
        });
    }
}
