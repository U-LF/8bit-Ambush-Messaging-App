/**
 * P2P Chat Client
 *
 * - Establishes UDP connection to a server to get peer IP and port.
 * - Creates TCP connection with a peer for communication.
 * - Listens and sends messages over the specified ports.
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class P2PClient extends Thread {

    private final String activity;
    private static String myIp;
    private static int myPort;
    private static String peerIp;
    private static int peerPort;

    public P2PClient(String activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            if ("listen".equals(activity)) {
                peerListen();
            } else {
                peerSend();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String serverName = "203.101.178.27";
        int port = 54545;

        // Create UDP connection to the server
        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.setReuseAddress(true);

        byte[] sendData = "Hello".getBytes();
        System.out.println("Sending 'Hello' to server...");

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverName), port);
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
        clientSocket.receive(receivePacket);
        clientSocket.close();

        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        String[] splitResponse = response.split(":");
        myIp = splitResponse[0];
        myPort = Integer.parseInt(splitResponse[1]);
        peerIp = splitResponse[2];
        peerPort = Integer.parseInt(splitResponse[3]);

        System.out.println("My Info: " + myIp + ":" + myPort);
        System.out.println("Peer Info: " + peerIp + ":" + peerPort);

        Thread listenThread = new P2PClient("listen");
        listenThread.start();

        Thread.sleep(2000);

        Thread sendThread = new P2PClient("send");
        sendThread.start();

        listenThread.join();
        sendThread.join();
    }

    private static void peerSend() throws IOException {
        try {
            Socket mySoc = new Socket();
            mySoc.setReuseAddress(true);
            mySoc.connect(new InetSocketAddress(peerIp, peerPort), 5000);
            System.out.println("Direct connection established with peer: " + peerIp + ":" + peerPort);

            DataOutputStream out = new DataOutputStream(mySoc.getOutputStream());
            DataInputStream in = new DataInputStream(mySoc.getInputStream());

            String msg = getTime() + "\t" + myPort + ": Can you hear me?";
            out.writeUTF(msg);
            System.out.println("Sent: " + msg);

            String newMsg = in.readUTF();
            System.out.println("Received: " + newMsg);

            mySoc.close();
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
            fallbackToTurnServer();
        }
    }

    private static void fallbackToTurnServer() {
        try {
            DatagramSocket turnSocket = new DatagramSocket();
            byte[] sendData = ("TURN:" + peerIp + ":" + peerPort).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("203.101.178.27"), 54546);
            turnSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
            turnSocket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received via TURN server: " + response);

            turnSocket.close();
        } catch (IOException e) {
            System.out.println("TURN server communication failed: " + e.getMessage());
        }
    }

    private static void peerListen() throws IOException {
        System.out.println("Listening on: 0.0.0.0:" + myPort);

        ServerSocket peerSocket = new ServerSocket();
        peerSocket.setReuseAddress(true);
        peerSocket.bind(new InetSocketAddress("0.0.0.0", myPort));

        Socket peer = peerSocket.accept();
        System.out.println("Connected with peer");

        DataInputStream in = new DataInputStream(peer.getInputStream());
        DataOutputStream out = new DataOutputStream(peer.getOutputStream());

        String msg = in.readUTF();
        System.out.println("Received: " + msg);

        String newMsg = getTime() + "\t" + myPort + ": Yes, I can hear you!";
        out.writeUTF(newMsg);
        System.out.println("Sent: " + newMsg);

        peerSocket.close();
    }

    private static String getTime() {
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        return df.format(new Date());
    }
}