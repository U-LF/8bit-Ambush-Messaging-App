/**
 * Client side P2P chat
 *
 * Creates UDP connection to server
 * Gets IP and Port
 * Creates TCP connection with peer
 * listens on port
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class p2pClient extends Thread{

    String activity;
    static String myIp;
    static int myPort;
    static String peerIp;
    static int peerPort;

    public p2pClient(String activity){
        this.activity = activity;
        this.myIp = "";
        this.peerIp = "";
    }

    /**************************************************
     * runs the threads to listen to the port and talk to the peer
     */
    public void run(){
        try{
            if(activity == "listen"){
                peerListen();
            }else{
                peerSend();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        //String serverName = "teamone.onthewifi.com";
        String serverName = "203.101.178.27";
        int port = 54545;

        // prepare Socket and data to send
        DatagramSocket clientSocket = new DatagramSocket();
        //System.out.println(clientSocket.isBound());
        clientSocket.setReuseAddress(true);


        byte[] sendData = "Hello".getBytes();
        System.out.println("sending Hello to server");

        // send Data to Server
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverName), port);
        clientSocket.send(sendPacket);

        // receive Data
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
        clientSocket.receive(receivePacket);
        System.out.println("received data from server");
        clientSocket.close();
        // Convert Response to IP and Port
        String response = new String(receivePacket.getData());
        String[] splitResponse = response.split(":");
        myIp = splitResponse[0];
        myPort = Integer.parseInt(splitResponse[1]);
        peerIp = splitResponse[2];
        peerPort = Integer.parseInt(splitResponse[3]);
        System.out.println("my Info: " + myIp + ":" + myPort );
        System.out.println("peer Info: " + peerIp + ":" + peerPort );
        System.out.println("\n\n");

        //clientSocket.bind(myPort);
        //clientSocket.close();

        //listen to port
        Thread listen = new p2pClient("listen");
        listen.start();

        Thread.sleep(2000);

        //send datagram
        Thread send = new p2pClient("send");
        send.start();

        //waits for thread to end
        listen.join();
        send.join();
    }//main

    /**************************************************
     * sends the p2p chat
     */
    private static void peerSend() throws SocketException, UnknownHostException, IOException {
        try {
            // Try to connect directly to peer using TCP
            Socket mySoc = new Socket();
            mySoc.setReuseAddress(true);
            mySoc.connect(new InetSocketAddress(peerIp, peerPort), 5000);  // 5000ms timeout
            System.out.println("Direct connection established with peer: " + peerIp + ":" + peerPort);

            // Proceed with the regular peer communication
            DataOutputStream out = new DataOutputStream(mySoc.getOutputStream());
            DataInputStream in = new DataInputStream(mySoc.getInputStream());

            String msg = getTime() + "\t" + myPort + ": Can you hear me?";
            out.writeUTF(msg);
            System.out.println(msg);

            String newMsg = in.readUTF();
            System.out.println(newMsg + "\n");

            mySoc.close();

		/*} catch (SocketException | UnknownHostException e) {
			System.out.println("Socket exception occurred: " + e.getMessage());*/
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
            System.out.println("Direct connection failed, trying TURN server...");

            // If direct connection fails, fall back to TURN server (UDP)
            DatagramSocket turnSocket = new DatagramSocket();
            byte[] sendData = ("TURN:" + peerIp + ":" + peerPort).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("203.101.178.27"), 54546);
            turnSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
            turnSocket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received message via TURN server: " + response);

            turnSocket.close();
        }
    }

    /**************************************************
     * The listens to the socket
     * @throws Exception
     */
    private static void peerListen() throws Exception{
        //create and listen to socket
        System.out.println("Listening on Socket: " + myPort);
        ServerSocket peerSocket = new ServerSocket();
        peerSocket.setReuseAddress(true);
        peerSocket.bind( new InetSocketAddress(myIp, myPort) );
        peerSocket.setReuseAddress(true);
        Socket peer = peerSocket.accept();

        System.out.println("Just connected with peer");

        //create a stream to talk to other peer
        DataInputStream in = new DataInputStream(peer.getInputStream());
        DataOutputStream out = new DataOutputStream(peer.getOutputStream());

        //get string from client A
        String msg = in.readUTF();
        System.out.println(msg);

        //create a message and send it to Client A
        String newMsg = getTime() + "\t" +myPort+ ": Yes I can hear you!";
        out.writeUTF(newMsg);
        System.out.println(newMsg + "\n");

        //close socket
        peerSocket.close();
    }

    /**************************************************
     * Creates a time stamp
     */
    private static String getTime(){
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        Date dateobj = new Date();
        return df.format(dateobj);
    }

    /**************************************************
     * Creates a random timer to wait
     */
    private static void pause()throws Exception{
        //create random number between 1 and 15
        Random rand = new Random();
        int breath = rand.nextInt(5) + 1;
        Thread.sleep(breath*1000);
    }
}