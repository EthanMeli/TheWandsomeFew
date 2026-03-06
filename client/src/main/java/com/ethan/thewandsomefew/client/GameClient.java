package com.ethan.thewandsomefew.client;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.ethan.thewandsomefew.protocol.HelloPacket;
import com.ethan.thewandsomefew.protocol.PacketCodec;

/**
 * Game Client connects to Server Socket
 * Define host IP and port to send Socket DataOutputStream
 * Create PacketCodec object for efficient write
 * Create Socket on defined host and port
 * Create DataOutputStream from socket initially as BufferedOutputStream for optimization
 * Send DataOutputStream to Socket
 */
public final class GameClient {
  public static void main(String[] args) throws Exception {
    String host = "127.0.0.1";
    int port = 43594;

    PacketCodec codec = new PacketCodec();

    try (Socket socket = new Socket(host, port)) {
      System.out.println("Connected to server.");

      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
      codec.writePacket(out, new HelloPacket(258));
      out.flush();
    }
  }
}
