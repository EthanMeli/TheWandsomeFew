/**
 * File: ClientSession.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 3/8/2026
 * 
 * Purpose:
 *   This file is responsible for defining the logic for individual
 *   client sessions, acting as the primary communication between
 *   clients and the game server.
 * 
 * Responsibilities:
 *   - Handle information exchange for individual clients with the game server
 *   - Creates an independent thread for client sessions to divide logic to be
 *     handled for the game server
 * 
 * Notes:
 *   - Currently very simple: connects to the client and reads a sent Hello Packet (protocol version).
 *     Interprets a read click to walk packet and calls the corresponding world player action.
 *     Then disconnects and cleans up the client's disconnection.
 */

package com.ethan.thewandsomefew.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketCodec;
import com.ethan.thewandsomefew.protocol.packets.ClickToWalkPacket;
import com.ethan.thewandsomefew.protocol.packets.HelloPacket;

/**
 * The ClientSession class handles the packet exchange between individual clients and the game server
 * on the server side.
 * 
 * <p>Responsibilities:
 * <ul>
 *    <li>Create an independent thread for individual clients</li>
 *    <li>Create data stream between individual clients and the game server</li>
 * </ul>
 */
public final class ClientSession implements Runnable {
  
  private final Socket socket;
  private final PacketCodec codec;
  private final World world;
  private final DataInputStream in;
  private volatile boolean running = true;

  /**
   * Creates a new ClientSession.
   *
   * @param socket represents the networking component connecting client sessions to the game server
   * @param codec is used to encode/decode packets for efficient and consistent data exchange with the game server
   * @param world represents the world state and provides world tick actions
   * @throws IOException if the socket was created incorrectly or the DataInputStream fails to be created (this.in)
   */
  public ClientSession(Socket socket, PacketCodec codec, World world) throws IOException {
    this.socket = socket;
    this.codec = codec;
    this.world = world;
    this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
  }

  public void stop() {
    running = false;
  }

  /**
   * Defines the logic for Client Sessions connecting to the server, processing data
   * while connected, and safely closing the connection.
   *
   * @exception IOException occurs when the client disconnects from the server
   * 
   * Note:
   *   - Clients may disconnect for a number of reasons, so the finally block
   *     provides the opportunity to safely cleanup the client disconnection
   */
  @Override
  public void run() {
    System.out.println("Session started for " + socket.getRemoteSocketAddress());

    try {
      while (running) {
        Packet packet = codec.readPacket(in);
        if (packet instanceof HelloPacket helloPacket) {
          System.out.println("Received HelloPacket: Protocol Version = " + helloPacket.protocolVersion());
        } else if (packet instanceof ClickToWalkPacket clickToWalkPacket) {
          System.out.println("Received ClickToWalkPacket: x=" + clickToWalkPacket.x() + " y=" + clickToWalkPacket.y());
          world.player().setWalkTarget(clickToWalkPacket.x(), clickToWalkPacket.y());
        } else {
          System.out.println(" Received Packet Type: " + packet.getClass().getSimpleName());
        }
      }
    } catch (IOException e) {
      System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
    } finally {
      System.out.println("=== Client Session Cleanup ===");
      if (socket != null) {
        try {
          socket.close();
          System.out.println("Client socket disconnected successfully.");
        } catch (IOException e) {
          System.out.println("Error closing client socket: " + e.getMessage());
          e.printStackTrace();
        }
      }
    }
  }
}
