/**
 * File: NpcLeavePacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 4/27/2026
 * Last Modified: 4/27/2026
 *
 * Purpose:
 *   This file defines the NpcLeavePacket class.
 *   The primary use is to send an implicit leave packet
 *   to the server so it has all necessary information to
 *   disconnect and derender the npc.
 *
 * Responsibilities:
 *   - Send id of npc to disconnect
 */
package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The NpcLeavePacket class is used to send an implicit leave packet
 * to the server so it has the proper npcId to disconnect and derender
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Send specific npcId to be disconnected from the server</li>
 * </ul>
 *
 * <p>
 * Format:
 * <ul>
 * <li>[npcId:int]</li>
 * </ul>
 */
public final class NpcLeavePacket implements Packet {

    private final int npcId;

    public NpcLeavePacket(int npcId) {
        this.npcId = npcId;
    }

    public int npcId() {
        return npcId;
    }

    @Override
    public PacketId id() {
        return PacketId.NPC_LEAVE;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.npcId);
    }

    public static NpcLeavePacket read(DataInput in) throws IOException {
        int npcId = in.readInt();
        return new NpcLeavePacket(npcId);
    }
}

