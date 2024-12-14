package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    // Set to hold all connected clients' handlers
    protected static Set<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2456);
        System.out.println("Server started, waiting for clients...");

        Thread ServerTalkThread = new Thread(() -> {
            try {
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));
                String serverMessage;
                while (true) {
                    System.out.print("Server (type message to send): ");
                    serverMessage = serverInput.readLine();

                    if (serverMessage != null && !serverMessage.trim().isEmpty()) {
                        // Broadcast the message to all clients
                        ClientHandler.broadcastMessage("Server: " + serverMessage, null);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading server input: " + e.getMessage());
            }
        });
        ServerTalkThread.start();

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
}