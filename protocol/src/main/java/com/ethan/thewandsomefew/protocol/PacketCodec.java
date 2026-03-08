/**
 * File: PacketCodec.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 3/4/2026
 * Last Modified: 3/8/2026
 * 
 * Purpose:
 *   This file is responsible for defining the PacketCodec class.
 *   The primary use is to encode and ecode packet information
 *   for consistent data exchange between client and game server.
 * 
 * Resposibilities:
 *   - Encode packet information from client consistently across packet types and write to server
 *   - Decode packet information consistently across packet types when server received packet
 * 
 * Notes:
 *   - Used currently for all packet types
 *   - Left intentionally minimal for now; will expand later (length prefixing, etc.)
 */

package com.ethan.thewandsomefew.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The PacketCodec class encodes and decodes packets on a binary stream.
 * 
 * <p>Responsibilites:
 * <ul>
 *    <li>Encode information from client input and write to server</li>
 *    <li>Decode packet sent from client to server for proper handling</li>
 * </ul>
 * 
 * <p>Format:
 * <ul>
 *   <li>[packetId:int][packetBody...]</li>
 * </ul>
 */
public final class PacketCodec {

  private final Map<Integer, Packet.Reader<? extends Packet>> readersById = new HashMap<>();

  /**
   * Creates a new PacketCodec.
   * 
   * <p>Notes
   * <ul>
   *   <li>Currently only registers Hello Packet, will eventually need to register different packet types.</li>
   * </ul>
   */
  public PacketCodec() {
    register(PacketId.HELLO, HelloPacket::read);
  }

  /**
   * Register a packet reader for a given PacketId (store in readersById map)
   * 
   * @param id the id corresponding to specific Packet type
   * @param reader represents the reader logic for a specific Packet type
   */
  public void register(PacketId id, Packet.Reader<? extends Packet> reader) {
    readersById.put(id.value(), reader);
  }

  /**
   * Writes a packet id along with its corresponding write logic for the body to game server.
   * 
   * @param out the binary output stream to the game server
   * @param packet the packet to be written to the game server
   * @throws IOException if the stream cannot be written to
   */
  public void writePacket(DataOutput out, Packet packet) throws IOException {
    out.writeInt(packet.id().value());
    packet.write(out);
    // don't flush here yet; caller may batch writes
  }

  /**
   * Reads a packet from the input stream.
   * 
   * <p>The packet format begins with an integer packet ID, followed by a
   * packet-specific binary payload. The packet ID is used to locate the
   * registered reader for that packet type.
   * 
   * @param in the binary input stream to read from
   * @return the decoded packet
   * @throws IOException if the packet ID is uknown or the stream cannot be read
   */
  public Packet readPacket(DataInput in) throws IOException {
    int id = in.readInt();
    Packet.Reader<? extends Packet> reader = readersById.get(id);

    if (reader == null) throw new IOException("Unknown packet id: " + id);

    return reader.read(in);
  }
}
