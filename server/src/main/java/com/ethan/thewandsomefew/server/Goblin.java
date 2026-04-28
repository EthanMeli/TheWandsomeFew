/**
 * File: Goblin.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/27/2026
 * Last Modified: 4/28/2026
 * 
 * Purpose:
 *   Concrete NPC type representing a basic goblin enemy. Models the
 *   OSRS Lvl-2 goblin: weak, slow, fights back when attacked.
 * 
 * Responsibilities:
 *   - Hardcode goblin stats (HP, max hit, attack speed)
 * 
 * Notes:
 *   - Stats are hardcoded constants for v1. When a config/data system
 *     exists, these will move out of the class.
 *   - No goblin-specific behavior fields yet (e.g. aggression range,
 *     leash range). Those get added when the systems that need them exist.
 */
package com.ethan.thewandsomefew.server;

public final class Goblin extends Npc {

    private static final int GOBLIN_MAX_HP = 5;
    private static final int GOBLIN_MAX_HIT = 1;
    private static final int GOBLIN_ATTACK_SPEED = 4;
    private static final NpcType TYPE = NpcType.GOBLIN;
    private static final int GOBLIN_RESPAWN_DELAY_TICKS = 10;

    public Goblin(int id, Tile spawnTile) {
        super(id, TYPE, spawnTile, GOBLIN_MAX_HP, GOBLIN_MAX_HIT, GOBLIN_ATTACK_SPEED, GOBLIN_RESPAWN_DELAY_TICKS);
    }
}
