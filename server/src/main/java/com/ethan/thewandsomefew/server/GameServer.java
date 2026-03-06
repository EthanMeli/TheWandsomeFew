package com.ethan.thewandsomefew.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.ethan.thewandsomefew.protocol.HelloPacket;
import com.ethan.thewandsomefew.protocol.Packet;
import com.ethan.thewandsomefew.protocol.PacketCodec;

/**
 * Game Server creates Server Socket and listens on specified port
 * PacketCodec object initialized to read input stream
 * Create Socket on Server Socket accept
 * Fetch DataInputStream initially as BufferedInputStream for optimization
 * Read DataInput using codec object to return a Packet
 */
public final class GameServer {
  public static void main(String[] args) throws Exception {
    int port = 43594;
    PacketCodec codec = new PacketCodec();

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server listening on " + port);

      try (Socket socket = serverSocket.accept()) {
        System.out.println("Client connected: " + socket.getRemoteSocketAddress());

        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        Packet inPacket = codec.readPacket(in);
        
        if (inPacket instanceof HelloPacket helloPacket) {
          System.out.println("Protcol Version: " + helloPacket.protocolVersion());
        } else {
          System.out.println("Packet Type: " + inPacket.id().toString());
        }
      }
    }
  }
}