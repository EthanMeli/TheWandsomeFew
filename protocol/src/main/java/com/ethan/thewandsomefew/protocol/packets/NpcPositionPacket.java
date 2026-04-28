/**
 * File: NpcPositionPacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 4/27/2026
 * Last Modified: 4/27/2026
 *
 * Purpose:
 *   This file defines the NpcPositionPacket class.
 *   The primary use is to send an NPC's position's coordinates from the
 *   server to a client.
 */
package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The NpcPositionPacket class represents an npc position sent from the
 * server to the client.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Store an npc's current position tile</li>
 * <li>Write tile coordinates to a binary stream</li>
 * <li>Reconstruct tile coordinates from a binary stream</li>
 * </ul>
 *
 * <p>
 * Format:
 * <ul>
 * <li>[npcId:int][x:int][y:int]</li>
 * </ul>
 */
public final class NpcPositionPacket implements Packet {

    private final int npcId;
    private final int x;
    private final int y;

    public NpcPositionPacket(int npcId, int x, int y) {
        this.npcId = npcId;
        this.x = x;
        this.y = y;
    }

    public int npcId() {
        return npcId;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public PacketId id() {
        return PacketId.NPC_POS;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.npcId);
        out.writeInt(this.x);
        out.writeInt(this.y);
    }

    public static NpcPositionPacket read(DataInput in) throws IOException {
        int npcId = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        return new NpcPositionPacket(npcId, x, y);
    }
}
