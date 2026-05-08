/**
 * File: BfsPathFinder.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/18/2026
 * Last Modified: 5/7/2026
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
    private static final int SEARCH_RADIUS = 64;

    public BfsPathFinder(TileMap worldTileMap) {
        this.worldTileMap = worldTileMap;
    }

    private boolean inSearchRadius(Tile from, int x, int y) {
        return Math.abs(from.x() - x) <= SEARCH_RADIUS && Math.abs(from.y() - y) <= SEARCH_RADIUS;
    }

    public ArrayDeque<Tile> findPath(Tile from, Set<Tile> acceptableTargets) {
        Map<Tile, Tile> visited = new HashMap<>();
        Deque<Tile> queue = new ArrayDeque<>();
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

        queue.add(from);
        visited.put(from, null);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();

            if (acceptableTargets.contains(current)) {
                return reconstructPath(current, visited);
            }

            for (int[] dir : directions) {
                int newX = current.x() + dir[0];
                int newY = current.y() + dir[1];
                if (inSearchRadius(from, newX, newY) && worldTileMap.isWalkable(newX, newY) && !visited.containsKey(worldTileMap.tileAt(newX, newY))) {
                    Tile neighbor = worldTileMap.tileAt(newX, newY);
                    queue.add(neighbor);
                    visited.put(neighbor, current);
                }
            }
        }

        return new ArrayDeque<>();
    }

    private ArrayDeque<Tile> reconstructPath(Tile goal, Map<Tile, Tile> visited) {
        ArrayDeque<Tile> path = new ArrayDeque<>();
        Tile current = goal;

        while (visited.get(current) != null) {
            path.addFirst(current);
            current = visited.get(current);
        }

        return path;
    }
}
