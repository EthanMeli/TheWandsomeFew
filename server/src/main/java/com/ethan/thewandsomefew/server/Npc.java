/**
 * File: Npc.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/27/2026
 * Last Modified: 4/28/2026
 * 
 * Purpose:
 *   Base class for all non-player living entities. Goblins, future
 *   enemies, and any other server-controlled combatant extend this.
 * 
 * Responsibilities:
 *   - Identify what kind of NPC this is (for client rendering)
 *   - Remember the tile this NPC was spawned at (for leashing and respawn)
 * 
 * Notes:
 *   - Concrete subclasses hardcode their stats in their constructors
 *     and call super().
 *   - Spawn tile is stored as a Tile, not just (x, y), because a Tile
 *     carries walkability info: useful when respawn logic needs to pick
 *     a valid tile from a list.
 */
package com.ethan.thewandsomefew.server;

public abstract class Npc extends LivingEntity {
  
    private final NpcType type;
    private final Tile spawnTile;
    private final int respawnDelayTicks;

    protected Npc(int id, NpcType type, Tile spawnTile, int maxHp, int maxHit, int attackSpeed, int respawnDelayTicks) {
        super(id, spawnTile.x(), spawnTile.y(), maxHp, maxHit, attackSpeed);

        this.type = type;
        this.spawnTile = spawnTile;
        this.respawnDelayTicks = respawnDelayTicks;
    }

    public NpcType type() {
        return type;
    }

    public Tile spawnTile() {
        return spawnTile;
    }

    public int respawnDelayTicks() {
        return respawnDelayTicks;
    }
}
