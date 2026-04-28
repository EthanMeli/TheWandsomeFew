/**
 * File: PacketId.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 3/4/2026
 * Last Modified: 4/28/2026
 *
 * Purpose:
 *   This file is responsible for defining the PacketId Enum,
 *   centralizing the identification of different Packet Types.
 *   Centralized IDs prevent client/server mismatches.
 *
 * Responsibilities:
 *   - Define existing and new packet types
 */
package com.ethan.thewandsomefew.protocol;

/**
 * Central registry of packet IDs
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Define existing and new packet types</li>
 * </ul>
 */
public enum PacketId {
    HELLO(1),
    CLICK_TO_WALK(2),
    PLAYER_POS(3),
    PLAYER_JOIN(4),
    PLAYER_LEAVE(5),
    WELCOME(6),
    NPC_JOIN(7),
    NPC_LEAVE(8),
    NPC_POS(9),
    NPC_INTERACT(10);

    private final int value;

    PacketId(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static PacketId fromValue(int value) {
        for (PacketId id : values()) {
            if (id.value == value) {
                return id;
            }
        }
        return null;
    }
}
