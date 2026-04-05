/**
 * File: World.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 4/4/2026
 * 
 * Purpose:
 *   This file is responsible for defining the World state, and performing
 *   logic associated with various entities (only Player for now, but eventually
 *   will include NPCs, Quests, etc.)
 */

package com.ethan.thewandsomefew.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.ethan.thewandsomefew.protocol.packets.PlayerPositionPacket;

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
  private Map<ClientSession, ConnectedPlayer> map;
  private static final AtomicInteger nextId = new AtomicInteger(0);

  public World() {
    map = new HashMap<>();
  }

  public void connectPlayer(ClientSession client) {
    map.put(client, new ConnectedPlayer(nextId.incrementAndGet(), new Player(0, 0), client));
  }

  public void disconnectPlayer(ClientSession client) {
    map.remove(client);
  }

  public Player getPlayerFromClient(ClientSession client) {
    return map.get(client).player();
  }

  // tick() currently only updates and logs player movement towards a target position,
  // but will need to be expanded upon when new actions and entities are introduced
  // to the World
  public void tick() {
    map.forEach((client, connectedPlayer) -> {
      if (client != null) {
        connectedPlayer.player().tickMovement();
        try {
          client.sendPacket(new PlayerPositionPacket(connectedPlayer.player().x(), connectedPlayer.player().y()));
        } catch (IOException e) {
          System.out.println("Error sending player position to client: " + e.getMessage());
          e.printStackTrace();
        }
        System.out.println("Player " + connectedPlayer.id() + " Position: x=" + connectedPlayer.player().x() + ", y=" + connectedPlayer.player().y());
      }
    });
  }
}