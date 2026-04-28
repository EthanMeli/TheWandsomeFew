/**
 * File: NpcType.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 4/27/2026
 * Last Modified: 4/27/2026
 * 
 * Purpose:
 *   Enumerates the kinds of NPCs that can exist in the world.
 */
package com.ethan.thewandsomefew.server;

public enum NpcType {
    GOBLIN(1);

    private final int value;

    NpcType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static NpcType fromValue(int value) {
        for (NpcType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
