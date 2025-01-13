import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import ClientShared.ConnectionAddress;
import ClientShared.MessageUtils;

public class Client {
    private static List<String> connectedClients = new ArrayList<>(); // List to store connected clients

    public static void main(String[] args) throws IOException {
        ConnectionAddress ServerAddress = new ConnectionAddress("Ip and port.txt");

        // Create a socket connection to the server
        Socket clientSocket = new Socket(ServerAddress.getIpAddress(), ServerAddress.getPort());
        System.out.println("Connected to server at " + ServerAddress.getIpAddress() + ":" + ServerAddress.getPort());

        // Create input and output streams for communication with the server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        StringBuilder messageBuilder = new StringBuilder();

        // Get the client's name
        System.out.print("Enter your name: ");
        String name = userInput.readLine();
        outToServer.writeBytes(name + "\n");

        // Thread to listen for messages from the server (chat messages)
        Thread listenThread = new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = inFromServer.readLine()) != null) {
                    if (serverMessage.startsWith("Currently connected clients:")) {
                        // Parse and display the list of connected clients
                        updateConnectedClientsList(serverMessage);
                        System.out.println();
                        MessageUtils.displayStringArray(connectedClients.toArray(new String[0]));
                    } else {
                        System.out.println("\n" + serverMessage);
                        System.out.print("\rEnter message (or 'exit' to quit): " + messageBuilder.toString());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        });
        listenThread.start();

        while (true) {
            // Print current message being typed
            System.out.print("\rEnter message (or 'exit' to quit): " + messageBuilder.toString());

            // Read character by character
            int input = System.in.read();

            char charInput = (char) input;

            if (charInput == '\n') { // Enter key pressed
                String message = messageBuilder.toString();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                outToServer.writeBytes(message + "\n");
                messageBuilder.setLength(0); // Reset message after sending
            } else {
                messageBuilder.append(charInput);
            }
        }

        clientSocket.close();
        System.out.println("Disconnected from server.");
    }

    // Method to update the list of connected clients
    private static void updateConnectedClientsList(String serverMessage) {
        connectedClients.clear();  // Clear the existing list
        connectedClients.addAll(List.of(MessageUtils.makeStringArray(MessageUtils.trimString(serverMessage, "Currently connected clients:")))); //adds the entire list to the local list
    }
}