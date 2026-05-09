/**
 * File: BfsPathFinder.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/18/2026
 * Last Modified: 5/8/2026
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

    public ArrayDeque<Tile> findPath(Tile from, Set<Tile> acceptableTargets, Tile fallbackCenter) {
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

        if (fallbackCenter == null) {
            return new ArrayDeque<>();
        }
        
        Tile fallbackGoal = findClosestReachable(fallbackCenter, visited);
        if (fallbackGoal == null) {
            return new ArrayDeque<>();
        }

        return reconstructPath(fallbackGoal, visited);
    }

    /**
     * @param fallbackCenter
     * @param visited
     * 
     * Scans a 21x21 grid around the clicked tile (fallbackCenter). Calculates
     * the chebyshev distance for each tile in 21x21 grid with the fallbackCenter.
     * Minimizes the chebyshev distance as the primary goal, resolves tie breaks
     * by finding the tile with the shortest path from the player's current position.
     * 
     * @return closest reachable tile with the shortest path
     */
    private Tile findClosestReachable(Tile fallbackCenter, Map<Tile, Tile> visited) {
        Tile best = null;
        int bestChebyshev = Integer.MAX_VALUE;
        int bestPathLength = Integer.MAX_VALUE;

        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                int wx = fallbackCenter.x() + (i - 10);
                int wy = fallbackCenter.y() + (j - 10);

                if (!worldTileMap.inBounds(wx, wy)) continue;
                Tile candidate = worldTileMap.tileAt(wx, wy);
                if (!visited.containsKey(candidate)) continue;

                int chebyshev = chebyshev(candidate, fallbackCenter);
                if (chebyshev > bestChebyshev) continue;

                int pathLen = pathLengthFromVisited(candidate, visited);
                if (chebyshev < bestChebyshev || (chebyshev == bestChebyshev && pathLen < bestPathLength)) {
                    best = candidate;
                    bestChebyshev = chebyshev;
                    bestPathLength = pathLen;
                }
            }
        }

        return best;
    }

    private int chebyshev(Tile t1, Tile t2) {
        return Math.max(Math.abs(t1.x() - t2.x()), Math.abs(t1.y() - t2.y()));
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

    private int pathLengthFromVisited(Tile tile, Map<Tile, Tile> visited) {
        int length = 0;
        Tile current = tile;
        while (visited.get(current) != null) {
            length++;
            current = visited.get(current);
        }
        return length;
    }
}
