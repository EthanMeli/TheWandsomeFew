/**
 * File: NpcState.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 4/28/2026
 * Last Modified: 4/28/2026
 *
 * Purpose:
 *   Store key information (id and position) of NPC for ClientState.
 *   Additionally, holds a type integer for rendering different sprites.
 *
 * Responsibilities:
 *   - Store the id, type, and position of a NPC for ClientState to properly render
 */
package com.ethan.thewandsomefew.client;

public record NpcState(int id, int type, int x, int y) {

}
