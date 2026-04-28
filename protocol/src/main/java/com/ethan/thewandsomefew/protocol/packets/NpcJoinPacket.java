/**
 * File: NpcJoinPacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 4/27/2026
 * Last Modified: 4/27/2026
 *
 * Purpose:
 *   This file defines the NpcJoinPacket class.
 *   The primary use is to send an implicit join packet
 *   to the server so it has all necessary information to
 *   render the proper NPC on spawn.
 *
 * Responsibilities:
 *   - Store unique npc ID to be identified by the server
 *   - Store initial tile coordinates to spawn npc
 */
package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The NpcJoinPacket class is used to send an implicit join packet
 * to the server so it has all necessary information to
 * render the proper NPC on connect.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Store unique npc ID to be identified by the server</li>
 * <li>Store initial tile coordinates to spawn npc</li>
 * </ul>
 *
 * <p>
 * Format:
 * <ul>
 * <li>[npcId:int][npcType:int][x:int][y:int]</li>
 * </ul>
 */
public final class NpcJoinPacket implements Packet {

    private final int npcId;
    private final int npcType;
    private final int x;
    private final int y;

    public NpcJoinPacket(int npcId, int npcType, int x, int y) {
        this.npcId = npcId;
        this.npcType = npcType;
        this.x = x;
        this.y = y;
    }

    public int npcId() {
        return npcId;
    }

    public int npcType() {
        return npcType;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public PacketId id() {
        return PacketId.NPC_JOIN;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.npcId);
        out.writeInt(this.npcType);
        out.writeInt(this.x);
        out.writeInt(this.y);
    }

    public static NpcJoinPacket read(DataInput in) throws IOException {
        int npcId = in.readInt();
        int npcType = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        return new NpcJoinPacket(npcId, npcType, x, y);
    }
}
