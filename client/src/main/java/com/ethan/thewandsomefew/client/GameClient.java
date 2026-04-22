/**
 * File: GameClient.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 3/5/2026
 * Last Modified: 4/22/2026
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

import javafx.application.Application;

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

        Application.launch(GameWindow.class, args);

    }
}
