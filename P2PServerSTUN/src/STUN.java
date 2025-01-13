/**
 * A UDP server that listens on port 54545 and exchanges IP addresses and ports between clients.
 */

import java.net.*;
import java.io.*;

public class STUN {

    private static final int PORT = 54545;

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                System.out.println("Waiting for clients...");

                // Receive data from the first client
                ClientInfo client1 = receiveClientInfo(serverSocket);
                System.out.println("Client 1 connected: " + client1);

                // Receive data from the second client
                ClientInfo client2 = receiveClientInfo(serverSocket);
                System.out.println("Client 2 connected: " + client2);

                // Exchange information between clients
                sendClientInfo(serverSocket, client1, client2);
                sendClientInfo(serverSocket, client2, client1);
            }

        } catch (IOException e) {
            System.err.println("Server encountered an error: " + e.getMessage());
        }
    }

    private static ClientInfo receiveClientInfo(DatagramSocket serverSocket) throws IOException {
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
        serverSocket.receive(receivePacket);

        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        return new ClientInfo(clientAddress, clientPort);
    }

    private static void sendClientInfo(DatagramSocket serverSocket, ClientInfo sender, ClientInfo recipient) throws IOException {
        String message = String.format("%s:%d:%s:%d:end",
                sender.getAddress(), sender.getPort(),
                recipient.getAddress(), recipient.getPort());

        byte[] messageBytes = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(messageBytes, messageBytes.length, sender.getInetAddress(), sender.getPort());

        serverSocket.send(sendPacket);
        System.out.println("Sent to " + sender + ": " + message);
    }

    /**
     * Helper class to encapsulate client information.
     */
    private static class ClientInfo {
        private final InetAddress inetAddress;
        private final int port;

        public ClientInfo(InetAddress inetAddress, int port) {
            this.inetAddress = inetAddress;
            this.port = port;
        }

        public InetAddress getInetAddress() {
            return inetAddress;
        }

        public String getAddress() {
            return inetAddress.getHostAddress();
        }

        public int getPort() {
            return port;
        }

        @Override
        public String toString() {
            return getAddress() + ":" + port;
        }
    }
}