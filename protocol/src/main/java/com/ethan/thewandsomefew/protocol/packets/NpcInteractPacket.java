/**
 * File: NpcInteractPacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 4/28/2026
 * Last Modified: 4/28/2026
 *
 * Purpose:
 *   This file defines the NpcInteractPacket class.
 *   The primary use is to send an interact actionType to a
 *   specific NPC.
 *
 * Responsibilities:
 *   - Store npcId for the NPC that the action is performed upon
 *   - Store actionType to be performed on NPC
 */
package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The NpcInteractPacket class represents an interact request for a particular NPC
 * and action type.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Store npcId for the NPC that the action is performed upon</li>
 * <li>Store actionType to be performed on NPC</li>
 * </ul>
 *
 * <p>
 * Format:
 * <ul>
 * <li>[npcId:int][actionType:int]</li>
 * </ul>
 */
public final class NpcInteractPacket implements Packet {

    public static final int ACTION_ATTACK = 1;

    private final int npcId;
    private final int actionType;

    public NpcInteractPacket(int npcId, int actionType) {
        this.npcId = npcId;
        this.actionType = actionType;
    }

    public int npcId() {
        return npcId;
    }

    public int actionType() {
        return actionType;
    }

    @Override
    public PacketId id() {
        return PacketId.NPC_INTERACT;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.npcId);
        out.writeInt(this.actionType);
    }

    public static NpcInteractPacket read(DataInput in) throws IOException {
        int npcId = in.readInt();
        int actionType = in.readInt();
        return new NpcInteractPacket(npcId, actionType);
    }
}
