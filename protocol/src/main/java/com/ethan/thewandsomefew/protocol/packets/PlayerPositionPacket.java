/**
 * File: PlayerPositionPacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 4/4/2026
 * Last Modified: 4/10/2026
 *
 * Purpose:
 *   This file defines the PlayerPosition class.
 *   The primary use is to send a player position's coordinates from the
 *   server to a client.
 */
package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The PlayerPositionPacket class represents a player position sent from the
 * server to the client.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Store a player's current position tile</li>
 * <li>Write tile coordinates to a binary stream</li>
 * <li>Reconstruct tile coordinates from a binary stream</li>
 * </ul>
 *
 * <p>
 * Format:
 * <ul>
 * <li>[playerId:int][x:int][y:int]</li>
 * </ul>
 */
public final class PlayerPositionPacket implements Packet {

    private final int playerId;
    private final int x;
    private final int y;

    public PlayerPositionPacket(int playerId, int x, int y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }

    public int playerId() {
        return playerId;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public PacketId id() {
        return PacketId.PLAYER_POS;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.playerId);
        out.writeInt(this.x);
        out.writeInt(this.y);
    }

    public static PlayerPositionPacket read(DataInput in) throws IOException {
        int playerId = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        return new PlayerPositionPacket(playerId, x, y);
    }
}
