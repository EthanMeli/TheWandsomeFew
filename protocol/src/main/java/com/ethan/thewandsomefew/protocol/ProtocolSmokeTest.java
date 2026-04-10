/**
 * File: ProtocolSmokeTest.java
 * Module: protocol
 * Authored By: Ethan Meli
 * Created: 3/4/2026
 * Last Modified: 3/8/2026
 *
 * Purpose:
 *   The purpose of this file is to test the Hello Packet
 *   functionality, as well as basic Packet Codec functions.
 *
 * Responsibilities:
 *   - Ensure Hello Packet is properly tested using Packet Codec
 *
 * Notes:
 *   - Used initially for testing protocol versions
 *   - May be expaned upon or deleted in the future
 */
package com.ethan.thewandsomefew.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.ethan.thewandsomefew.protocol.packets.HelloPacket;

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
