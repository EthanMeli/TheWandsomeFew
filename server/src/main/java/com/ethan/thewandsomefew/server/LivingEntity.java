/**
 * File: LivingEntity.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/27/2026
 * Last Modified: 4/28/2026
 *
 * Purpose:
 *   Base class for entities that can move, fight, take damage, and die.
 *   Sits between Entity (pure spatial identity) and concrete combatants
 *   like Player nad NPC.
 *
 * Responsbilities:
 *   - Track HP and combat stats (max HP, max hit, attack speed)
 *   - Track current movement path and update position per tick
 *   - Track current combat target (by ID) and attack cooldown timer
 *
 * Notes:
 *   - Combat target is stored as an entity ID, not a direct reference.
 *     World resolves the ID to an entity each tick. ID 0 means no target.
 *     This way, when a target dies and is removed from World.entities,
 *     the lookup naturally neturns null and combat code can react.
 *   - Combat logic itself (damage rolls, range checks, attack triggering)
 *     lives in World. This class only holds state.
 */
package com.ethan.thewandsomefew.server;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class LivingEntity extends Entity {

    public static final int NO_TARGET = 0;

    private int hp;

    // Extract below into separate record CombatStats
    private final int maxHp;
    private final int maxHit;
    private final int attackSpeed;

    private final Deque<Tile> path;
    private int combatTargetId; // ID of combat target (0 means no target since IDs start at 1)
    private int attackTimer;

    protected LivingEntity(int id, int x, int y, int maxHp, int maxHit, int attackSpeed) {
        
        super(id, x, y);
        
        this.hp = maxHp; // Full health on spawn
        this.maxHp = maxHp;
        this.maxHit = maxHit;
        this.attackSpeed = attackSpeed;

        path = new ArrayDeque<>();
        combatTargetId = NO_TARGET;
        attackTimer = 0; // Ready to attack immediately
    }

    // --- HP ---

    public int hp() {
        return hp;
    }

    public int maxHp() {
        return maxHp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    /**
     * Apply damage to this entity. HP cannot go below 0.
     * 
     * @param amount of damage to take
     * @return true if this damage caused the entity to die
     */
    public boolean takeDamage(int amount) {
        if (isDead()) {
            return false; // already dead, this damage doesn't kill again
        }
        hp = Math.max(0, hp - amount);
        return isDead();
    }

    /**
     * Apply restore hp to this entity. HP cannot go above maxHp.
     * 
     * @param amount of hp to restore
     */
    public void restoreHp(int amount) {
        hp = Math.min(hp + amount, maxHp);
    }

    // --- Combat stats ---

    public int maxHit() {
        return maxHit;
    }

    public int attackSpeed() {
        return attackSpeed;
    }

    // --- Combat target ---

    public int combatTargetId() {
        return combatTargetId;
    }

    public boolean hasCombatTarget() {
        return combatTargetId != NO_TARGET;
    }

    public void setCombatTarget(int entityId) {
        combatTargetId = entityId;
    }

    public void clearCombatTarget() {
        combatTargetId = NO_TARGET;
    }

    // --- Attack Timer ---

    public int attackTimer() {
        return attackTimer;
    }

    public boolean canAttack() {
        return attackTimer == 0;
    }

    public void resetAttackTimer() {
        attackTimer = attackSpeed;
    }

    public void clearAttackTimer() {
        attackTimer = 0;
    }

    public void tickAttackTimer() {
        if (attackTimer > 0) {
            attackTimer--;
        }
    }

    // --- Movement ---

    public void setPath(Deque<Tile> newPath) {
        path.clear();
        path.addAll(newPath);
    }

    public boolean hasPath() {
        return !path.isEmpty();
    }

    public void clearPath() {
        path.clear();
    }

    /**
     * Advance one step along the current path. Called by World each tick.
     * If the path is empty, does nothing
     */
    public void tickMovement() {
        Tile nextTile;
        if ((nextTile = path.poll()) != null) {
            setPosition(nextTile.x(), nextTile.y());
        }
    }
}
