/**
 * File: NetworkThread.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 4/22/2026
 * Last Modified: 4/28/2026
 *
 * Purpose:
 *   This file is responsible for defining the network thread logic for game clients.
 *   This will read packet streams as well as write to the server.
 *
 * Responsibilities:
 *   - Connect users to the game server
 *   - Handle user interaction with the client
 *   - Send data to the game server
 * 
 * Notes:
 *   - We essentially move the logic previously in GameClient here so we are able to maintain
 *     a server connection while the application window is open
 */
package com.ethan.thewandsomefew.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketCodec;
import com.ethan.thewandsomefew.protocol.packets.HelloPacket;
import com.ethan.thewandsomefew.protocol.packets.NpcJoinPacket;
import com.ethan.thewandsomefew.protocol.packets.NpcLeavePacket;
import com.ethan.thewandsomefew.protocol.packets.NpcPositionPacket;
import com.ethan.thewandsomefew.protocol.packets.PlayerJoinPacket;
import com.ethan.thewandsomefew.protocol.packets.PlayerLeavePacket;
import com.ethan.thewandsomefew.protocol.packets.PlayerPositionPacket;
import com.ethan.thewandsomefew.protocol.packets.WelcomePacket;

import javafx.application.Platform;

public final class NetworkThread implements Runnable {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final PacketCodec codec;
    private final ClientState clientState;
    private volatile boolean running = true;

    public NetworkThread(ClientState clientState, String host, int port) throws IOException {
        this.clientState = clientState;
        this.socket = new Socket(host, port);

        System.out.println("Connected to server."); // socket connection successful
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        codec = new PacketCodec();
    }

    public void sendPacket(Packet packet) throws IOException {
        codec.writePacket(out, packet);
        out.flush();
    }

    public void stop () {
        running = false;
    }

    @Override
    public void run() {
        try {
            sendPacket(new HelloPacket(258));

            while (running) {
                Packet packet = codec.readPacket(in);

                if (packet instanceof PlayerPositionPacket playerPosPacket) {
                    System.out.println("Player " + playerPosPacket.playerId() + " Position Packet Received: x=" + playerPosPacket.x() + " y=" + playerPosPacket.y());
                    Platform.runLater(() -> {
                        clientState.updatePlayerPosition(playerPosPacket.playerId(), playerPosPacket.x(), playerPosPacket.y());
                    });
                } else if (packet instanceof PlayerJoinPacket join) {
                    System.out.println("Player " + join.playerId() + " joined at (" + join.x() + ", " + join.y() + ")");
                    Platform.runLater(() -> {
                        clientState.addPlayer(join.playerId(), join.x(), join.y());
                    });
                } else if (packet instanceof PlayerLeavePacket leave) {
                    System.out.println("Player " + leave.playerId() + " left");
                    Platform.runLater(() -> {
                        clientState.removePlayer(leave.playerId());
                    });
                } else if (packet instanceof WelcomePacket welcome) {
                    System.out.println("Welcome Player Id: " + welcome.playerId());
                    Platform.runLater(() -> {
                        clientState.setLocalPlayerId(welcome.playerId());
                    });
                } else if (packet instanceof NpcJoinPacket join) {
                    System.out.println("NPC " + join.npcId() + " (type=" + join.npcType() + ") at (" + join.x() + ", " + join.y() + ")");
                    Platform.runLater(() -> {
                        clientState.addNpc(join.npcId(), join.npcType(), join.x(), join.y());
                    });
                } else if (packet instanceof NpcPositionPacket position) {
                    Platform.runLater(() -> {
                        clientState.updateNpcPosition(position.npcId(), position.x(), position.y());
                    });
                } else if (packet instanceof NpcLeavePacket leave) {
                    System.out.println("NPC " + leave.npcId() + " left.");
                    Platform.runLater(() -> {
                        clientState.removeNpc(leave.npcId());
                    });
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
    }
}
