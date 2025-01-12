import java.net.*;
import java.io.*;

public class TURN {

    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(54546);
        System.out.println("TURN Server Listening on Port 54546...");

        while(true) {
            // First client sends a message
            DatagramPacket receivePacket1 = new DatagramPacket(new byte[1024], 1024);
            serverSocket.receive(receivePacket1);
            InetAddress client1Address = receivePacket1.getAddress();
            int client1Port = receivePacket1.getPort();
            String msgFromClient1 = new String(receivePacket1.getData(), 0, receivePacket1.getLength());

            // Second client sends a message
            DatagramPacket receivePacket2 = new DatagramPacket(new byte[1024], 1024);
            serverSocket.receive(receivePacket2);
            InetAddress client2Address = receivePacket2.getAddress();
            int client2Port = receivePacket2.getPort();
            String msgFromClient2 = new String(receivePacket2.getData(), 0, receivePacket2.getLength());

            // Relay information from Client1 to Client2
            String relayMsgToClient2 = "From Client1: " + msgFromClient1;
            serverSocket.send(new DatagramPacket(relayMsgToClient2.getBytes(), relayMsgToClient2.length(), client2Address, client2Port));
            System.out.println("Relayed message from Client1 to Client2");

            // Relay information from Client2 to Client1
            String relayMsgToClient1 = "From Client2: " + msgFromClient2;
            serverSocket.send(new DatagramPacket(relayMsgToClient1.getBytes(), relayMsgToClient1.length(), client1Address, client1Port));
            System.out.println("Relayed message from Client2 to Client1");
        }
    }
}