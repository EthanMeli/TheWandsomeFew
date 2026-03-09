/**
 * File: HelloPacket.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 3/4/2026
 * Last Modified: 3/8/2026
 * 
 * Purpose:
 *   This file is responsible for defining the HelloPacket class.
 *   The primary use is to identify Client Session connections to the
 *   Game Server by corresponding protocol versions.
 * 
 * Responsibilities:
 *   - Extract protocol version for client sessions
 * 
 * Notes:
 *   - Extends the Packet class (defining basic implementation for server packets)
 *   - Current use is primarily to distinguish client protocol versions to maintain consistency in game server
 */

package com.ethan.thewandsomefew.protocol.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketId;

/**
 * The HelloPacket class exchanges a client session's protocol version with the Game Server.
 * 
 * <p>Responsibilities:
 * <ul>
 *    <li>Read protocol version from client input stream</li>
 *    <li>Write protocol version to Game Server</li>
 * </ul>
 * 
 * <p>Format:
 * <ul>
 *   <li>[protocolVersion:int]</li>
 * </ul>
 */
public final class HelloPacket implements Packet {
  
  private final int protocolVersion;

  public HelloPacket(int protocolVersion) {
    this.protocolVersion = protocolVersion;
  }

  public int protocolVersion() {
    return protocolVersion;
  }

  @Override
  public PacketId id() {
    return PacketId.HELLO;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.protocolVersion);
  }

  public static HelloPacket read(DataInput in) throws IOException {
    int protocolVersion = in.readInt();
    return new HelloPacket(protocolVersion);
  }
}
