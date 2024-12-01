package Client;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        String serverAddress = "203.101.178.27"; // Change to server IP if needed
        int serverPort = 2456;

        // Create a socket connection to the server
        Socket clientSocket = new Socket(serverAddress, serverPort);
        System.out.println("Connected to server at " + serverAddress + ":" + serverPort);

        // Create input and output streams for communication with the server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        // Get the client's name
        System.out.print("Enter your name: ");
        String name = userInput.readLine();
        outToServer.writeBytes(name + "\n");

        // Thread to listen for messages from the server (chat messages)
        Thread listenThread = new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = inFromServer.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        });
        listenThread.start();

        // Main thread to send messages to the server
        String message;
        while (true) {
            System.out.print("Enter message (or 'exit' to quit): ");
            message = userInput.readLine();
            if (message.equalsIgnoreCase("exit")) {
                break;
            }
            outToServer.writeBytes(message + "\n");
        }

        clientSocket.close();
        System.out.println("Disconnected from server.");
    }
}
