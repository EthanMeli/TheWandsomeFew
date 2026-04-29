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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.packets.NpcJoinPacket;
import com.ethan.thewandsomefew.protocol.packets.NpcLeavePacket;
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

    // --- World ---
    private final TileMap worldTileMap;
    private final BfsPathFinder pathFinder;
    private final AtomicInteger nextId = new AtomicInteger(0);

    // --- Player ---
    private final ConcurrentLinkedQueue<PlayerAction> actions;
    private final Map<ClientSession, ConnectedPlayer> clientPlayerMap;

    // --- NPCs ---
    private final List<Tile> goblinSpawnPoints;
    private final Map<Integer, Entity> entities;
    private final List<PendingRespawn> pendingRespawns;
    private final Random random;

    public World() {
        actions = new ConcurrentLinkedQueue<>();
        clientPlayerMap = new HashMap<>();
        entities = new HashMap<>();
        worldTileMap = new TileMap();
        pathFinder = new BfsPathFinder(worldTileMap);
        goblinSpawnPoints = buildGoblinSpawnPoints();
        pendingRespawns = new ArrayList<>();
        random = new Random();
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
        if (leaving == null) {
            return;
        }

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

        p.clearCombatTarget();

        Deque<Tile> path = pathFinder.findPath(from, acceptableTargets);
        p.setPath(path);
    }

    private void handlePlayerDeath(Player p) {
        p.setPosition(0, 0);
        p.restoreHp(p.maxHp());
        p.clearCombatTarget();
        p.clearPath();
        p.clearAttackTimer();

        for (Entity e : entities.values()) {
            if (e instanceof LivingEntity living) {
                if (living.combatTargetId() == p.id()) {
                    living.clearCombatTarget();
                }
            }
        }
    }

    // --- Combat ---
    private void handleAttackAction(ClientSession client, int npcId) {
        Player player = getPlayerFromClient(client);
        Entity target = getEntityFromId(npcId);

        if (!(target instanceof Npc npcTarget)) {
            return;
        }

        player.setCombatTarget(npcId);

        pathToCombatTarget(player, npcTarget);
    }

    private void pathToCombatTarget(LivingEntity attacker, LivingEntity target) {
        Tile from = worldTileMap.tileAt(attacker.x(), attacker.y());
        Set<Tile> acceptableTargets = adjacentWalkableTiles(target);

        if (acceptableTargets.isEmpty()) {
            attacker.clearPath();
            return;
        }

        Deque<Tile> path = pathFinder.findPath(from, acceptableTargets);
        attacker.setPath(path);
    }

    private Set<Tile> adjacentWalkableTiles(LivingEntity target) {
        int[][] directions = {
            {0, -1},  // west
            {0, 1},   // east
            {1, 0},   // south
            {-1, 0},  // north
            {1, -1},  // south-west
            {1, 1},   // south-east
            {-1, -1}, // north-west
            {-1, 1}   // north-east
        };
        Set<Tile> adjacentTiles = new HashSet<>();
        for (int[] dir : directions) {
            int nx = target.x() + dir[0];
            int ny = target.y() + dir[1];
            if (worldTileMap.isWalkable(nx, ny)) {
                adjacentTiles.add(worldTileMap.tileAt(nx, ny));
            }
        }
        return adjacentTiles;
    }

    private boolean isInMeleeRange(LivingEntity a, LivingEntity b) {
        int dx = Math.abs(a.x() - b.x());
        int dy = Math.abs(a.y() - b.y());
        int chebyshev = Math.max(dx, dy);
        return chebyshev == 1;
    }

    // --- Action Helpers ---
    private Player getPlayerFromClient(ClientSession client) {
        return clientPlayerMap.get(client).player();
    }

    private Entity getEntityFromId(int entityId) {
        return entities.get(entityId);
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
                case PlayerAction.Attack(ClientSession client, int npcId) ->
                    handleAttackAction(client, npcId);
            }
        }
    }

    // --- Npc-related functions and classes ---
    private static final class PendingRespawn {

        final NpcType type;
        int ticksRemaining;

        PendingRespawn(NpcType type, int ticksRemaining) {
            this.type = type;
            this.ticksRemaining = ticksRemaining;
        }
    }

    private void spawnInitialNpcs() {
        spawnNpc(NpcType.GOBLIN);
    }

    private void processRespawns() {
        Iterator<PendingRespawn> it = pendingRespawns.iterator();

        while (it.hasNext()) {
            PendingRespawn respawn = it.next();
            respawn.ticksRemaining--;
            if (respawn.ticksRemaining <= 0) {
                spawnNpc(respawn.type);
                it.remove();
            }
        }
    }

    private void spawnNpc(NpcType type) {
        int npcId = nextId.incrementAndGet();
        Tile spawnTile = pickSpawnTile(type);
        Npc npc = createNpc(type, npcId, spawnTile);
        entities.put(npc.id(), npc);

        // Broadcast join to all connected clients
        for (ConnectedPlayer p : clientPlayerMap.values()) {
            trySend(p.clientSession(), new NpcJoinPacket(npcId, npc.type().value(), npc.x(), npc.y()));
        }
    }

    private Tile pickSpawnTile(NpcType type) {
        List<Tile> spawnPoints = switch (type) {
            case GOBLIN -> goblinSpawnPoints;
        };
        return spawnPoints.get(random.nextInt(spawnPoints.size()));
    }

    private Npc createNpc(NpcType type, int npcId, Tile spawnTile) {
        return switch (type) {
            case GOBLIN -> new Goblin(npcId, spawnTile);
        };
    }

    private List<Tile> buildGoblinSpawnPoints() {
        List<Tile> points = new ArrayList<>();
        int[][] coords = {{10, 10}, {10, 11}, {11, 10}, {11, 11}};
        for (int[] c : coords) {
            Tile t = worldTileMap.tileAt(c[0], c[1]);
            if (!t.isWalkable()) {
                throw new IllegalStateException("Goblin spawn point is not walkable: " + t);
            }
            points.add(t);
        }
        return points;
    }

    /**
     * Death handling involves removing for the entity map, broadcasting to all
     * clients, and scheduling the NPC's respawn
     */
    private void handleNpcDeath(Npc npc) {
        entities.remove(npc.id());

        for (ConnectedPlayer p : clientPlayerMap.values()) {
            trySend(p.clientSession(), new NpcLeavePacket(npc.id()));
        }

        scheduleRespawn(npc.type(), npc.respawnDelayTicks());
    }

    private void scheduleRespawn(NpcType type, int respawnDelayTicks) {
        PendingRespawn schedule = new PendingRespawn(type, respawnDelayTicks);
        pendingRespawns.add(schedule);
    }

    /**
     * Picks a tile for an Npc in combat to shuffle to in the instance
     * that a player is stacked on top of its target
     * @param entity to be moved
     * @return tile for entity to be moved to
     */
    private Tile pickShuffleTile(LivingEntity entity) {
        int[][] directions = {
            {0, -1},  // west
            {0, 1},   // east
            {1, 0},   // south
            {-1, 0},  // north
            {1, -1},  // south-west
            {1, 1},   // south-east
            {-1, -1}, // north-west
            {-1, 1}   // north-east
        };
        for (int[] dir : directions) {
            int nx = entity.x() + dir[0];
            int ny = entity.y() + dir[1];
            if (worldTileMap.isWalkable(nx, ny)) {
                return worldTileMap.tileAt(nx, ny);
            }
        }
        return null;
    }

    /**
     * Helper function to send a packet to a client, queueing a disconnect if
     * the send fails.
     */
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
        processRespawns();

        List<Npc> npcsToKill = new ArrayList<>();
        List<Player> playersWhoDied = new ArrayList<>();

        // iterate and apply combat logic for all entities
        for (Entity e : entities.values()) {
            if (!(e instanceof LivingEntity attacker) || !attacker.hasCombatTarget()) {
                continue;
            }

            attacker.tickAttackTimer();

            Entity targetEntity = entities.get(attacker.combatTargetId());
            if (!(targetEntity instanceof LivingEntity target)) {
                continue; // re-path loop clears combat target next iteration
            }

            if (!isInMeleeRange(attacker, target)) {
                if (attacker.x() == target.x() && attacker.y() == target.y()) {
                    // Same tile - shuffle out (NPC only)
                    if (attacker instanceof Npc) {
                        Tile shuffleTile = pickShuffleTile(attacker);
                        if (shuffleTile != null) {
                            Deque<Tile> shufflePath = new ArrayDeque<>();
                            shufflePath.add(shuffleTile);
                            attacker.setPath(shufflePath);
                        }
                    } else {
                        pathToCombatTarget(attacker, target);
                    }
                } else {
                    pathToCombatTarget(attacker, target);
                }
                continue; // not close enough yet
            }

            if (!attacker.canAttack()) {
                continue; // on cooldown
            }

            if (target instanceof Npc) {
                target.setCombatTarget(e.id()); // set npc to retaliate when reached (don't do for player since they may be running away)
            }

            int damage = random.nextInt(attacker.maxHit() + 1);
            boolean killed = target.takeDamage(damage);
            attacker.resetAttackTimer();

            System.out.println("Entity " + attacker.id() + " hit entity " + target.id() + " for " + damage + " hp.");
            System.out.println("Entity " + attacker.id() + " hp: " + attacker.hp());

            if (killed) {
                switch (target) {
                    case Npc n -> npcsToKill.add(n);
                    case Player p -> playersWhoDied.add(p);
                    default -> {
                    }
                }
            }
        }

        for (Npc n : npcsToKill) {
            handleNpcDeath(n);
        }

        for (Player p : playersWhoDied) {
            System.out.println("Player " + p.id() + " died.");
            handlePlayerDeath(p);
        }

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
