/**
 * File: PlayerState.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 4/20/2026
 * Last Modified: 4/20/2026
 *
 * Purpose:
 *   Store key information (id and position) of player for ClientState
 *
 * Responsibilities:
 *   - Store the id and position of a player for ClientState to properly render
 */
package com.ethan.thewandsomefew.client;

public record PlayerState(int id, int x, int y) {

}
