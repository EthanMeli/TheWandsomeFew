/**
 * File: Player.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 4/18/2026
 *
 * Purpose:
 *   This file is responsible for defining the logic for Players.
 *
 * Notes:
 *   - Currently very simple: runs basic movement logic, advancing tile by
 *     tile on a tick basis towards its target position
 */
package com.ethan.thewandsomefew.server;

import java.util.ArrayDeque;
import java.util.Deque;

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

    private Deque<Tile> path;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        path = new ArrayDeque<>();
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void setPath(Deque<Tile> path) {
        this.path = path;
    }

    // Paths are created by the BfsPathFinder class
    // The implementation is a simple BFS path finder that moves
    // in the order specified by 0002-tilemap-and-pathfinding
    public void tickMovement() {
        Tile nextTile;
        if ((nextTile = path.poll()) != null) {
            this.x = nextTile.x();
            this.y = nextTile.y();
        }
    }
}
