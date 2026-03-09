/**
 * File: Packet.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 3/4/2026
 * Last Modified: 3/8/2026
 * 
 * Purpose:
 *   This file is responsible for defining the Packet interface.
 *   The primary use is to define a consistent template for different 
 *   packet types to be created with custom read and write methods for
 *   data exchange between a Client Session and the Game Server.
 * 
 * Responsibilities:
 *   - Define Packet template for different packet types' information 
 *     exchange between clients and game server
 * 
 * Notes:
 *   - Uses Enum defined in PacketId for different Packet types
 */

package com.ethan.thewandsomefew.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The Packet interface defines template read and write functions
 * for different Packet Types to expand upon
 */
public interface Packet {
  
  PacketId id();

  void write(DataOutput out) throws IOException;

  interface Reader<T extends Packet> {
    T read(DataInput in) throws IOException;
  }
}
