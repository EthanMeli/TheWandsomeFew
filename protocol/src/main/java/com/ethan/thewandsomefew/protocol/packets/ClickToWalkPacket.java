/**
 * File: ClickToWalkPacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 3/8/2026
 *
 * Purpose:
 *   This file defines the ClickToWalkPacket class.
 *   The primary use is to send a target tile coordinate from client to server
 *   when the player clicks to move.
 *
 * Responsibilities:
 *   - Store the tile coordinates selected by the client
 *   - Serialize tile coordinates into binary format for transmission
 *   - Deserialize tile coordinates from binary input
 *
 * Notes:
 *   - Field order must remain consistent between write() and read().
 *   - This packet currently represents intent only; actual movement logic will be handled later.
 */

package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The ClickToWalkPacket class represents a movement request sent from
 * client to server.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Store a clicked tile position</li>
 *   <li>Write tile coordinates to a binary stream</li>
 *   <li>Reconstruct tile coordinates from a binary stream</li>
 * </ul>
 *
 * <p>Format:
 * <ul>
 *   <li>[x:int][y:int]</li>
 * </ul>
 */
public final class ClickToWalkPacket implements Packet {
  
  private final int x;
  private final int y;

  public ClickToWalkPacket(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

  @Override
  public PacketId id() {
    return PacketId.CLICK_TO_WALK;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.x);
    out.writeInt(this.y);
  }

  public static ClickToWalkPacket read(DataInput in) throws IOException {
    int x = in.readInt();
    int y = in.readInt();
    return new ClickToWalkPacket(x, y);
  }
}
