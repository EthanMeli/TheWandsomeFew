package com.ethan.thewandsomefew.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class ProtocolSmokeTest {
  
  public static void main(String[] args) throws Exception {

    // Create Packet Codec (read/write = decoder/encoder)
    PacketCodec codec = new PacketCodec();

    // Encode Packet (test for HelloPacket with protocolVersion = 258)
    HelloPacket helloPacket = new HelloPacket(258);
    ByteArrayOutputStream byteArrayOutStr = new ByteArrayOutputStream();
    DataOutputStream outputStream = new DataOutputStream(byteArrayOutStr);
    
    codec.writePacket(outputStream, helloPacket);
    outputStream.flush();

    byte[] buf = byteArrayOutStr.toByteArray();

    // Decode Packet (from above encoding format)
    ByteArrayInputStream byteArrayInStr = new ByteArrayInputStream(buf);
    DataInputStream inputStream = new DataInputStream(byteArrayInStr);

    Packet newPacket = codec.readPacket(inputStream);

    if (newPacket.id() == PacketId.HELLO) {
      HelloPacket newHelloPacket = (HelloPacket) newPacket;
      if (newHelloPacket.protocolVersion() == 258) {
        System.out.println("PASS");
      } else {
        System.out.println("FAIL. Incorrect Protocol Version");
      }
    } else {
      System.out.println("FAIL. Incorrect Packet Type");
    }
  } 
}
