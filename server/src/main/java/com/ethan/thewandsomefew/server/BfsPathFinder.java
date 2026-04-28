/**
 * File: BfsPathFinder.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/18/2026
 * Last Modified: 4/27/2026
 *
 * Purpose:
 *   This file is responsible for defining the logic for path finding
 *
 * Responsibilities:
 *   - Implement BFS Path Finding to set a player's path for navigation
 *
 */
package com.ethan.thewandsomefew.server;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BfsPathFinder {

    private final TileMap worldTileMap;

    public BfsPathFinder(TileMap worldTileMap) {
        this.worldTileMap = worldTileMap;
    }

    public ArrayDeque<Tile> findPath(Tile from, Set<Tile> acceptableTargets) {
        Map<Tile, Tile> visited = new HashMap<>();
        Deque<Tile> neighbors = new ArrayDeque<>();
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

        Tile currentTile = from;
        visited.put(from, null);
        while (currentTile != null && !acceptableTargets.contains(currentTile)) {
            for (int[] dir : directions) {
                int newX = currentTile.x() + dir[0];
                int newY = currentTile.y() + dir[1];
                if (worldTileMap.isWalkable(newX, newY) && !visited.containsKey(worldTileMap.tileAt(newX, newY))) {
                    neighbors.add(worldTileMap.tileAt(newX, newY));
                    visited.put(worldTileMap.tileAt(newX, newY), currentTile);
                }
            }
            currentTile = neighbors.poll();
        }

        ArrayDeque<Tile> path = new ArrayDeque<>();
        if (currentTile == null) {
            return path;
        }

        while (currentTile != from) {
            path.addFirst(currentTile);
            currentTile = visited.get(currentTile);
        }

        return path;
    }
}
