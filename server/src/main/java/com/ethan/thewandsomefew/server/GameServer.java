/**
 * File: GameServer.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/5/2026
 * Last Modified: 4/4/2026
 *
 * Purpose:
 *   This file is responsible for defining the logic for central game server,
 *   running the main tick engine, establishing a server socket gateway, and
 *   creating, connecting, and processing client sessions.
 *
 * Responsibilities:
 *   - Run Tick Engine loop
 *   - Establish Server Socket gateway for client connections
 *   - Create, connect, and process client sessions on independent threads
 *
 * Notes:
 *   - Currently very simple: runs basic tick engine loop, and creates a single
 *     client session
 */
package com.ethan.thewandsomefew.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.ethan.thewandsomefew.protocol.PacketCodec;

/**
 * The GameServer class handles the logic for the central game server,
 * establishing a network gateway for client sessions, as well as creating
 * independent threads for individual client sessions.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Run Tick Engine loop</li>
 * <li>Establish Server Socket gateway for client connections</li>
 * <li>Create, connect, and process client sessions on independent threads</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 * <li>Currently very simple, only creating a processing a single client session
 * connection</li>
 * </ul>
 */
public final class GameServer {

    public static void main(String[] args) throws Exception {
        int port = 43594;
        PacketCodec codec = new PacketCodec();
        World world = new World();

        TickEngine tickEngine = new TickEngine(600, () -> {
            System.out.println("Tick");
            world.tick();
        });
        new Thread(tickEngine, "tick-engine").start();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientSession clientSession = new ClientSession(socket, codec, world);
                world.submitAction(new PlayerAction.Connect(clientSession, new Player(0, 0)));
                new Thread(clientSession, "client-" + socket.getPort()).start();
            }
        }
    }
}
