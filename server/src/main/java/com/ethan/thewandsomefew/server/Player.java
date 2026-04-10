/**
 * File: Player.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 3/8/2026
 *
 * Purpose:
 *   This file is responsible for defining the logic for Players.
 *
 * Notes:
 *   - Currently very simple: runs basic movement logic, advancing tile by
 *     tile on a tick basis towards its target position
 */
package com.ethan.thewandsomefew.server;

/**
 * The Player class handles all logic related to Players (users).
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Stores current and target position for Player</li>
 * <li>Updates player position on tick depending on current and target
 * position</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 * <li>Current implementation is very simple, but will continue to be built upon
 * for entire Player related logic</li>
 * </ul>
 */
public final class Player {

    private int x;
    private int y;

    private Integer targetX; // Integer used for compare function in tickMovement
    private Integer targetY;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void setWalkTarget(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    // Movement is currently diagonal-prioritized and advances one tile per tick.
    // This is a temporary simplification until pathfinding and collision are introduced.
    public void tickMovement() {
        if (this.targetX != null && this.targetY != null) {
            this.x += Integer.compare(this.targetX, this.x);
            this.y += Integer.compare(this.targetY, this.y);

            if (Integer.compare(this.x, this.targetX) == 0 && Integer.compare(this.y, this.targetY) == 0) {
                System.out.println("Player has reached target position.");
                this.targetX = null;
                this.targetY = null;
            }
        }
    }
}
