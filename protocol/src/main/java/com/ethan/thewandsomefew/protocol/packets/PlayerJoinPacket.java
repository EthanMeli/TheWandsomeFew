/**
 * File: PlayerJoinPacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 4/13/2026
 * Last Modified: 4/13/2026
 *
 * Purpose:
 *   This file defines the PlayerJoinPacket class.
 *   The primary use is to send an implicit join packet
 *   to the server so it has all necessary information to
 *   render the proper player on connect.
 *
 * Responsibilities:
 *   - Store unique player ID to be identified by the server
 *   - Store initial tile coordinates to spawn player
 *
 * Notes:
 *   - Simple as of now, only storing initial position and ID,
 *     but will eventually store references to the player's appearance,
 *     equipment, etc.
 */
package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The PlayerJoinPacket class is used to send an implicit join packet
 * to the server so it has all necessary information to
 * render the proper player on connect.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Store unique player ID to be identified by the server</li>
 * <li>Store initial tile coordinates to spawn player</li>
 * <li>TODO: Store reference to player's appearance, equipment, etc.</li>
 * </ul>
 *
 * <p>
 * Format:
 * <ul>
 * <li>[playerId:int][x:int][y:int]</li>
 * </ul>
 */
public final class PlayerJoinPacket implements Packet {

    private final int playerId;
    private final int x;
    private final int y;

    public PlayerJoinPacket(int playerId, int x, int y) {
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
        return PacketId.PLAYER_JOIN;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.playerId);
        out.writeInt(this.x);
        out.writeInt(this.y);
    }

    public static PlayerJoinPacket read(DataInput in) throws IOException {
        int playerId = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        return new PlayerJoinPacket(playerId, x, y);
    }
}
