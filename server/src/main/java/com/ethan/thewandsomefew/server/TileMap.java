/**
 * File: TileMap.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/18/2026
 * Last Modified: 4/18/2026
 *
 * Purpose:
 *   Store the map of tiles for the World
 *
 * Responsibilities:
 *   - Store all tiles relating to the world map
 *   - Provide functions to see whether tiles can be walked to
 *   - Create a test map for testing pathfinding functionality
 */
package com.ethan.thewandsomefew.server;

public class TileMap {

    private final Tile[][] map;

    public TileMap() {
        map = createTestMap();
    }

    public Tile[][] map() {
        return this.map;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[x].length;
    }

    public boolean isWalkable(int x, int y) {
        return inBounds(x, y) && map[x][y].isWalkable();
    }

    public final Tile[][] createTestMap() {
        Tile[][] testMap = new Tile[50][50];

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                testMap[i][j] = new Tile(i, j, true);
            }
        }

        // Create wall
        testMap[4][2] = new Tile(4, 2, false);
        testMap[4][3] = new Tile(4, 3, false);
        testMap[4][4] = new Tile(4, 4, false);
        testMap[4][5] = new Tile(4, 5, false);
        testMap[4][6] = new Tile(4, 6, false);

        // Doorway to test player navigation through tight space
        testMap[2][1] = new Tile(2, 1, false);
        testMap[2][3] = new Tile(2, 3, false);

        // Wall that is unpassable
        for (int i = 0; i < 50; i++) {
            testMap[48][i] = new Tile(48, i, false);
        }

        return testMap;
    }
}
