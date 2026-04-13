/**
 * File: PlayerLeavePacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 4/13/2026
 * Last Modified: 4/13/2026
 *
 * Purpose:
 *   This file defines the PlayerLeavePacket class.
 *   The primary use is to send an implicit leave packet
 *   to the server so it has all necessary information to
 *   disconnect and derender the connected player.
 *
 * Responsibilities:
 *   - Send id of player to disconnect
 * 
 */
package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The PlayerLeavePacket class is used to send an implicit leave packet
 * to the server so it has the proper playerId to disconnect and derender
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Send specific playerId to be disconnected from the server</li>
 * </ul>
 *
 * <p>
 * Format:
 * <ul>
 * <li>[playerId:int]</li>
 * </ul>
 */
public final class PlayerLeavePacket implements Packet {

    private final int playerId;

    public PlayerLeavePacket(int playerId) {
        this.playerId = playerId;
    }

    public int playerId() {
        return playerId;
    }

    @Override
    public PacketId id() {
        return PacketId.PLAYER_LEAVE;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.playerId);
    }

    public static PlayerLeavePacket read(DataInput in) throws IOException {
        int playerId = in.readInt();
        return new PlayerLeavePacket(playerId);
    }
}
