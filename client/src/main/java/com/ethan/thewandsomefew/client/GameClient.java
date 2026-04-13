/**
 * File: GameClient.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 3/5/2026
 * Last Modified: 4/13/2026
 *
 * Purpose:
 *   This file is responsible for defining the logic for Game Clients,
 *   handling user input, and writing relevant packets to the game server.
 *
 * Responsibilities:
 *   - Connect users to the game server
 *   - Handle user interaction with the client
 *   - Send data to the game server
 *
 * Notes:
 *   - Currently very simple: connects to the server and sends a Hello Packet (protocol version) to the server.
 */
package com.ethan.thewandsomefew.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketCodec;
import com.ethan.thewandsomefew.protocol.packets.ClickToWalkPacket;
import com.ethan.thewandsomefew.protocol.packets.HelloPacket;
import com.ethan.thewandsomefew.protocol.packets.PlayerJoinPacket;
import com.ethan.thewandsomefew.protocol.packets.PlayerLeavePacket;
import com.ethan.thewandsomefew.protocol.packets.PlayerPositionPacket;

/**
 * The GameClient class handles the packet exchange between individual clients
 * and the game server on the client side.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Connect users to the game server</li>
 * <li>Handle user interaction with the client</li>
 * <li>Send data to the game server</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 * <li>Currently very simple, but eventually will contain complex logic for an
 * interactive user experience
 * </ul>
 */
public final class GameClient {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 43594;

        PacketCodec codec = new PacketCodec();

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to server.");

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            codec.writePacket(out, new HelloPacket(258));
            out.flush();

            codec.writePacket(out, new ClickToWalkPacket(10, 15));
            out.flush();

            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            while (true) {
                Packet packet = codec.readPacket(in);

                if (packet instanceof PlayerPositionPacket playerPosPacket) {
                    System.out.println("Player " + playerPosPacket.playerId() + " Position Packet Received: x=" + playerPosPacket.x() + " y=" + playerPosPacket.y());
                } else if (packet instanceof PlayerJoinPacket join) {
                    System.out.println("Player " + join.playerId() + " joined at (" + join.x() + ", " + join.y() + ")");
                } else if (packet instanceof PlayerLeavePacket leave) {
                    System.out.println("Player " + leave.playerId() + " left");
                }
            }
        }
    }
}
