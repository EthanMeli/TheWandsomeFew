/**
 * File: TileMapData.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/19/2026
 * Last Modified: 4/19/2026
 *
 * Purpose:
 *   Store the map of tiles for the World (Client side)
 *
 * Responsibilities:
 *   - Store all tiles relating to the world map for the client
 *   - Provide functions to see whether tiles can be walked to
 *   - Create a test map for testing pathfinding functionality
 */
package com.ethan.thewandsomefew.client;

public class TileMapData {

    private final int width = 50;
    private final int height = 50;

    private final boolean[][] map;

    public TileMapData() {
        this.map = createTestMap();
    }

    public boolean isWalkable(int x, int y) {
        return inBounds(x, y) && map[x][y];
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int width() { return width; }
    public int height() { return height; }
    public boolean[][] map() { return map; }

    private boolean[][] createTestMap() {
        boolean[][] testMap = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                testMap[i][j] = true;
            }
        }

        // Create wall
        testMap[4][2] = false;
        testMap[4][3] = false;
        testMap[4][4] = false;
        testMap[4][5] = false;
        testMap[4][6] = false;

        // Doorway to test player navigation through tight space
        testMap[2][1] = false;
        testMap[2][3] = false;

        // Wall that is unpassable
        for (int i = 0; i < width; i++) {
            testMap[48][i] = false;
        }

        return testMap;
    }

}
