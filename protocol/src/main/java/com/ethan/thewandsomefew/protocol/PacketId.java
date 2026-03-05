package com.ethan.thewandsomefew.protocol;

/**
 * Central registry of packet IDs.
 * 
 * Keeping IDs in one place prevents client/server mismatches
 */
public enum PacketId {
  HELLO(1),
  CLICK_TO_WALK(2),
  PLAYER_POS(3);

  private final int value;

  PacketId(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public static PacketId fromValue(int value) {
    for (PacketId id : values()) {
      if (id.value == value) return id;
    }
    return null;
  }
}