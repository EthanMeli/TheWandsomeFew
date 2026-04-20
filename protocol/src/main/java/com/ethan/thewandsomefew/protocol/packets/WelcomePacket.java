/**
 * File: WelcomePacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 4/20/2026
 * Last Modified: 4/20/2026
 *
 * Purpose:
 *   This file is responsible for defining the WelcomePacket class.
 *   This class is used to identify the player id for the client.
 */
package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The WelcomePacket class informs the client of the player id tied
 * to the ClientSession.
 *
 * <p>
 * Format:
 * <ul>
 * <li>[playerId:int]</li>
 * </ul>
 */
public final class WelcomePacket implements Packet {

    private final int playerId;

    public WelcomePacket(int playerId) {
        this.playerId = playerId;
    }

    public int playerId() {
        return playerId;
    }

    @Override
    public PacketId id() {
        return PacketId.WELCOME;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.playerId);
    }

    public static WelcomePacket read(DataInput in) throws IOException {
        int playerId = in.readInt();
        return new WelcomePacket(playerId);
    }
}
