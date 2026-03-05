package com.ethan.thewandsomefew.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class HelloPacket implements Packet {
  
  private final int protocolVersion;

  public HelloPacket(int protocolVersion) {
    this.protocolVersion = protocolVersion;
  }

  public int protocolVersion() {
    return protocolVersion;
  }

  /**
   * Return HELLO PacketId enum
   */
  @Override
  public PacketId id() {
    return PacketId.HELLO;
  }

  /**
   * Write protocolVersion to DataOutput stream
   * Throw IOException if error occurs
   */
  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.protocolVersion);
  }

  /**
   * @param in (DataInput)
   * Read input parameter as int to find protocolVersion
   * @return Packet corresponding to protocolVersion
   * @throws IOException
   */
  public static HelloPacket read(DataInput in) throws IOException {
    int protocolVersion = in.readInt();
    return new HelloPacket(protocolVersion);
  }
}
