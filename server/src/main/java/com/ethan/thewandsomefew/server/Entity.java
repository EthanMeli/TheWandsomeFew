/**
 * File: Entity.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/27/2026
 * Last Modified: 4/27/2026
 *
 * Purpose:
 *   Base class for all things that exist in the world with a position.
 *   This is the lowest common denominator, anything that occupies a tile
 *   and has a unique identifier is an Entity.
 *
 * Responsibilities:
 *   - Store a unique identifier shared across all entity types
 *   - Store the entity's current world position (x, y tile coordinates)
 *
 * Notes:
 *   - Position is mutable; identity is not.
 *   - This class is intentionally minimal. Combat, movement, and other
 *     behavioral concerns live on subclasses (LivingEntity).
 *   - Entities like static world objects (chests, trees, ground items)
 *     would extend Entity directly without picking up combat/movement.
 */
package com.ethan.thewandsomefew.server;

public abstract class Entity {

    private final int id;
    private int x;
    private int y;

    protected Entity(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int id() {
        return id;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
