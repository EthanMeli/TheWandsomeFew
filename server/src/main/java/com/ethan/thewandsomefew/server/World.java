/**
 * File: World.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 4/28/2026
 *
 * Purpose:
 *   This file is responsible for defining the World state, and performing
 *   logic associated with various entities.
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
import com.ethan.thewandsomefew.protocol.packets.NpcJoinPacket;
import com.ethan.thewandsomefew.protocol.packets.NpcPositionPacket;
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
    private final Map<Integer, Entity> entities;
    private final TileMap worldTileMap;
    private final BfsPathFinder pathFinder;
    private final AtomicInteger nextId = new AtomicInteger(0);

    public World() {
        actions = new ConcurrentLinkedQueue<>();
        clientPlayerMap = new HashMap<>();
        entities = new HashMap<>();
        worldTileMap = new TileMap();
        pathFinder = new BfsPathFinder(worldTileMap);
        spawnInitialNpcs();
    }

    // --- Player-related functions ---
    private void connectPlayer(ClientSession client, Player player) {
        ConnectedPlayer newPlayer = new ConnectedPlayer(player, client);

        // 1: Tell every existing player about the new one
        for (ConnectedPlayer existing : clientPlayerMap.values()) {
            trySend(existing.clientSession(), new PlayerJoinPacket(player.id(), player.x(), player.y()));
        }

        // 2: Add the new player to the maps
        clientPlayerMap.put(client, newPlayer);
        entities.put(player.id(), player);

        // 3: Send player's client respective playerId to control client state
        trySend(client, new WelcomePacket(player.id()));

        // 4: Tell the new player about everyone in the world (including themselves)
        for (ConnectedPlayer existing : clientPlayerMap.values()) {
            trySend(client, new PlayerJoinPacket(existing.player().id(), existing.player().x(), existing.player().y()));
        }

        // 5: Tell the new player about every NPC in the world
        for (Entity entity : entities.values()) {
            if (entity instanceof Npc npc) {
                trySend(client, new NpcJoinPacket(
                    npc.id(), npc.type().value(), npc.x(), npc.y()
                ));
            }
        }
    }

    private void disconnectPlayer(ClientSession client) {
        ConnectedPlayer leaving = clientPlayerMap.get(client);
        if (leaving == null) return;

        // Remove first so we don't try to send a leave packet to the leaver
        clientPlayerMap.remove(client);
        entities.remove(leaving.player().id());

        // Tell every other client
        for (ConnectedPlayer remaining : clientPlayerMap.values()) {
            trySend(remaining.clientSession(), new PlayerLeavePacket(leaving.player().id()));
        }
    }

    private void setPlayerPath(ClientSession client, int x, int y) {
        if (!worldTileMap.isWalkable(x, y)) {
            return;
        }

        Player p = getPlayerFromClient(client);
        Tile from = worldTileMap.tileAt(p.x(), p.y());
        Set<Tile> acceptableTargets = new HashSet<>();
        acceptableTargets.add(worldTileMap.tileAt(x, y));

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
                case PlayerAction.Connect(ClientSession client) -> {
                    int newId = nextId.incrementAndGet();
                    int spawnX = 0;
                    int spawnY = 0;
                    Player player = new Player(newId, spawnX, spawnY);
                    connectPlayer(client, player);
                }
                case PlayerAction.Disconnect(ClientSession client) ->
                    disconnectPlayer(client);
                case PlayerAction.Walk(ClientSession client, int x, int y) ->
                    setPlayerPath(client, x, y);
            }
        }
    }

    // --- Npc-related functions ---
    private void spawnInitialNpcs() {
        int goblinId = nextId.incrementAndGet();
        Tile spawnTile = worldTileMap.tileAt(10, 10);
        Goblin goblin = new Goblin(goblinId, spawnTile);
        entities.put(goblin.id(), goblin);
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

        // iterate and apply tick movement for all entities
        for (Entity e : entities.values()) {
            if (e instanceof LivingEntity living) {
                living.tickMovement();
            }
        }

        // subject (whose position is sent)
        for (ConnectedPlayer recipient : clientPlayerMap.values()) {
            for (ConnectedPlayer subject : clientPlayerMap.values()) {
                trySend(recipient.clientSession(), new PlayerPositionPacket(
                    subject.player().id(), subject.player().x(), subject.player().y()
                ));
            }
            for (Entity entity : entities.values()) {
                if (entity instanceof Npc npc) {
                    trySend(recipient.clientSession(), new NpcPositionPacket(
                        npc.id(), npc.x(), npc.y()
                    ));
                }
            }
        }
    }
}
