/**
 * File: World.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 3/8/2026
 * 
 * Purpose:
 *   This file is responsible for defining the World state, and performing
 *   logic associated with various entities (only Player for now, but eventually
 *   will include NPCs, Quests, etc.)
 */

package com.ethan.thewandsomefew.server;

/**
 * The World class defines and tracks the current World state, performing
 * specific actions on tick for entities within the World.
 * 
 * <p>Responsibilities:
 * <ul>
 *   <li>Defines and tracks entire World state</li>
 *   <li>Updates state and entities within on tick</li>
 * </ul>
 * 
 * <p>Notes:
 * <ul>
 *   <li>Current implementation is very simple, storing only a single Player, but
 *       will eventually include all related World objects such as NPCs, Quests, Objects, etc.</li>
 * </ul>
 */
public final class World {
  private final Player player;

  public World() {
    player = new Player(0, 0);
  }

  public Player player() {
    return player;
  }

  // tick() currently only updates and logs player movement towards a target position,
  // but will need to be expanded upon when new actions and entities are introduced
  // to the World
  public void tick() {
    player.tickMovement();
    System.out.println("Player Position: x=" + player.x() + ", y=" + player.y());
  }
}