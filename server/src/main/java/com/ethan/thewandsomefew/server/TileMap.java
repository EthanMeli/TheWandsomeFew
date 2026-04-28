/**
 * File: TileMap.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/18/2026
 * Last Modified: 4/27/2026
 *
 * Purpose:
 *   Store the map of tiles for the World
 *
 * Responsibilities:
 *   - Store all tiles relating to the world map for the server
 *   - Provide functions to see whether tiles can be walked to
 *   - Create a test map for testing pathfinding functionality
 */
package com.ethan.thewandsomefew.server;

public class TileMap {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private final Tile[][] map;

    public TileMap() {
        map = createTestMap();
    }

    public Tile[][] map() {
        return this.map;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public boolean isWalkable(int x, int y) {
        return inBounds(x, y) && map[x][y].isWalkable();
    }

    public Tile tileAt(int x, int y) {
        if (!inBounds(x, y)) {
            throw new IllegalArgumentException("Tile out of bounds: (" + x + ", " + y + ")");
        }
        return map[x][y];
    }

    private Tile[][] createTestMap() {
        Tile[][] testMap = new Tile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                testMap[x][y] = new Tile(x, y, true);
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
        for (int y = 0; y < HEIGHT; y++) {
            testMap[48][y] = new Tile(48, y, false);
        }

        return testMap;
    }
}
