/**
 * File: PlayerAction.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/6/2026
 * Last Modified: 4/6/2026
 * 
 * Purpose:
 *   This file is responsible for creating Player Actions which
 *   can be processed on a thread safe queue that multiple clients
 *   may touch
 * 
 * Responsibilities:
 *   - Define Player Actions and functionality
 * 
 * Notes:
 *   - The major player actions will be defined here, which are then
 *     added to a thread safe queue before being processed by the server
 */

package com.ethan.thewandsomefew.server;

/**
 * Represents an action submitted by a client thread to be
 * processed by the tick thread.
 *
 * Why this exists: client threads run concurrently with the
 * tick engine. If they modified World state directly, you'd
 * need locks everywhere. Instead, client threads just drop
 * actions into a thread-safe queue, and the tick thread
 * processes them one at a time at the start of each tick.
 *
 * This is the same pattern OSRS uses — all player inputs
 * are queued and processed at the top of each game tick,
 * which is why actions in OSRS always feel like they "snap"
 * to tick boundaries.
 */

public sealed interface PlayerAction {
  record Connect(ClientSession client, Player player) implements PlayerAction {}
  record Disconnect(ClientSession client) implements PlayerAction {}
  record Walk(ClientSession client, int x, int y) implements PlayerAction {}
}