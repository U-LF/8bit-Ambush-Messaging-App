import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
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
            System.out.println("\n" + clientName + " has joined the chat.");
            Server.ServerMsgPrompt("\r");

            broadcastConnectedClientsList();

            // Send welcome message to the client
            outToClient.writeBytes("Welcome " + clientName + "! You are now connected to the chat room.\n");

            // Notify all clients that a new user has joined
            broadcastMessage(clientName + " has joined the chat.", this);

            String clientMessage;
            while ((clientMessage = inFromClient.readLine()) != null) {
                System.out.println("\n" + clientName + ": " + clientMessage);
                Server.ServerMsgPrompt("\r");
                // Broadcast the message to all clients
                broadcastMessage(clientName + ": " + clientMessage, this);
            }
        } catch (IOException e) {
            System.err.println("Connection error with client: " + clientName);
        } finally {
            try {
                // Clean up and remove client from the list of active clients
                System.out.println("\n" + clientName + " has left the chat.");
                Server.ServerMsgPrompt("\r");
                broadcastMessage(clientName + " has left the chat.", this);
                Server.clients.remove(this);
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to send a message to all clients except the sender
    public static void broadcastMessage(String message, ClientHandler sender) {
        synchronized (Server.clients) {
            for (ClientHandler client : Server.clients) {
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

    private void broadcastConnectedClientsList() throws IOException {
        synchronized (Server.clients) {
            StringBuilder clientList = new StringBuilder("Currently connected clients: ");
            for (ClientHandler client : Server.clients) {
                clientList.append(client.clientName).append(", ");
            }
            // Remove the trailing comma and space
            if (clientList.length() > 0) {
                clientList.setLength(clientList.length() - 2);
            }
            for (ClientHandler client : Server.clients) {
                try {
                    client.outToClient.writeBytes(clientList.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}