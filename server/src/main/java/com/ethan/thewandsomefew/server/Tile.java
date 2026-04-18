/**
 * File: Tile.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/18/2026
 * Last Modified: 4/18/2026
 *
 * Purpose:
 *   Store information about a Tile for the TileMap
 *
 * Responsibilities:
 *   - Store the coordinates relating to a specific tile in the TileMap
 *   - Store whether the specific tile can be walked to (for pathfinding, etc.)
 */
package com.ethan.thewandsomefew.server;

public record Tile(int x, int y, boolean isWalkable) {

}
