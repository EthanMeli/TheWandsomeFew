package com.ethan.thewandsomefew.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Base interface for all packets exchanged between client and server
 * 
 * For now, packets are encoded/decoded using DataInput/DataOutput.
 * Add codec in future
 */
public interface Packet {
  
  PacketId id();

  void write(DataOutput out) throws IOException;

  interface Reader<T extends Packet> {
    T read(DataInput in) throws IOException;
  }
}
