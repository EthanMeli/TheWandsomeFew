package com.ethan.thewandsomefew.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Encodes and decodes packets on a binary stream
 * 
 * Format:
 *   [pacletId:int][packetBody...]
 * 
 * This is intentionally minimal; will expand later (length prefixing, etc.).
 */
public final class PacketCodec {

  private final Map<Integer, Packet.Reader<? extends Packet>> readersById = new HashMap<>();

  public PacketCodec() {
    // register only HelloPacket
    register(PacketId.HELLO, HelloPacket::read);
  }

  /**
   * Register a packet reader for a given PacketId
   */
  public void register(PacketId id, Packet.Reader<? extends Packet> reader) {
    readersById.put(id.value(), reader);
  }

  /**
   * Writes:
   *   1) packet id as int
   *   2) packet body
   */
  public void writePacket(DataOutput out, Packet packet) throws IOException {
    out.writeInt(packet.id().value());
    packet.write(out);
    // don't flush here yet; caller may batch writes
  }

  /**
   * Reads:
   *   1) packet id as int
   *   2) looks up reader
   *   3) uses reader to read body and construct the Packet
   */
  public Packet readPacket(DataInput in) throws IOException {
    int id = in.readInt();
    Packet.Reader<? extends Packet> reader = readersById.get(id);

    if (reader == null) throw new IOException("Unknown packet id: " + id);

    return reader.read(in);
  }
}
