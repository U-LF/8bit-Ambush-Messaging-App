package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    // Set to hold all connected clients' output streams
    private static Set<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2456);
        System.out.println("Server started, waiting for clients...");

        while (true) {
            // Accept new client connections
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Create a new handler for the client and start a new thread
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    // ClientHandler class that handles communication with each client
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader inFromClient;
        private DataOutputStream outToClient;
        private String clientName;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Create input and output streams for the client
                inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToClient = new DataOutputStream(clientSocket.getOutputStream());

                // Ask for the client's name
                clientName = inFromClient.readLine();
                System.out.println(clientName + " has joined the chat.");

                // Send welcome message to the client
                outToClient.writeBytes("Welcome " + clientName + "! You are now connected to the chat room.\n");

                // Notify all clients that a new user has joined
                broadcastMessage(clientName + " has joined the chat.", this);

                String clientMessage;
                while ((clientMessage = inFromClient.readLine()) != null) {
                    System.out.println(clientName + ": " + clientMessage);
                    // Broadcast the message to all clients
                    broadcastMessage(clientName + ": " + clientMessage, this);
                }
            } catch (IOException e) {
                System.err.println("Connection error with client: " + clientName);
            } finally {
                try {
                    // Clean up and remove client from the list of active clients
                    System.out.println(clientName + " has left the chat.");
                    broadcastMessage(clientName + " has left the chat.", this);
                    clients.remove(this);
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Method to send a message to all clients except the sender
        private void broadcastMessage(String message, ClientHandler sender) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    if (client != sender) {
                        try {
                            client.outToClient.writeBytes(message + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}