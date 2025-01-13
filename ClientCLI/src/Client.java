import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ClientShared.ConnectionAddress;
import ClientShared.MessageUtils;

public class Client {
    private static List<String> connectedClients = new ArrayList<>(); // List to store connected clients

    private static final String AUTH_SERVER_HOST = "localhost";
    private static final int AUTH_SERVER_PORT = 3456;

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(AUTH_SERVER_HOST, AUTH_SERVER_PORT);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Authentication server");
            String choice;

            do {
                System.out.println("\n1. Login");
                System.out.println("2. Signup");
                System.out.println("3. Exit");
                System.out.print("Choice: ");
                choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        if(handleLogin(writer, reader, scanner)){
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
                        } else {

                        }
                        break;
                    case "2":
                        if(handleSignup(writer, reader, scanner)){

                        } else {

                        }
                        break;
                    case "3":
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } while (!"3".equals(choice));

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }

    }

    // Method to update the list of connected clients
    private static void updateConnectedClientsList(String serverMessage) {
        connectedClients.clear();  // Clear the existing list
        connectedClients.addAll(List.of(MessageUtils.makeStringArray(MessageUtils.trimString(serverMessage, "Currently connected clients:")))); //adds the entire list to the local list
    }

    //sign-in / sign-up functions
    private static boolean handleLogin(BufferedWriter writer, BufferedReader reader, Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String request = String.format(
                "{\"action\":\"login\",\"username\":\"%s\",\"password\":\"%s\"}",
                username, password
        );

        System.out.println("Sending login request...");
        writer.write(request);
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        System.out.println("Server response: " + response);

        // Manually parse the response string
        if (response.contains("\"status\":\"success\"") && response.contains("\"message\":\"Login successful\"")) {
            return true;
        }

        return false;
    }


    private static boolean handleSignup(BufferedWriter writer, BufferedReader reader, Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Display name: ");
        String displayName = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String request = String.format(
                "{\"action\":\"signup\",\"username\":\"%s\",\"display_name\":\"%s\",\"password\":\"%s\"}",
                username, displayName, password
        );

        System.out.println("Sending signup request...");
        writer.write(request);
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        System.out.println("Server response: " + response);

        if (response.contains("\"status\":\"success\"") && response.contains("\"message\":\"Account created successfully\"")) {
            return true;
        }

        return false;
    }
}