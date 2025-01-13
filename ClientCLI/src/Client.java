import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ClientShared.ConnectionAddress;
import ClientShared.MessageUtils;

public class Client {
    private static final List<String> connectedClients = new ArrayList<>();
    private static final String AUTH_SERVER_HOST = "localhost";
    private static final int AUTH_SERVER_PORT = 3456;

    public static void main(String[] args) {
        try (Socket socket = new Socket(AUTH_SERVER_HOST, AUTH_SERVER_PORT);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Authentication server");
            handleUserChoices(writer, reader, scanner);

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    private static void handleUserChoices(BufferedWriter writer, BufferedReader reader, Scanner scanner) throws IOException {
        String choice;
        do {
            displayMenu();
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (handleLogin(writer, reader, scanner)) {
                        handleServerConnection(scanner);
                    } else {
                        System.out.println("Login failed.");
                    }
                    break;
                case "2":
                    if (handleSignup(writer, reader, scanner)) {
                        System.out.println("Signup successful. You can now log in.");
                    } else {
                        System.out.println("Signup failed.");
                    }
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!"3".equals(choice));
    }

    private static void displayMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Signup");
        System.out.println("3. Exit");
        System.out.print("Choice: ");
    }

    private static boolean handleLogin(BufferedWriter writer, BufferedReader reader, Scanner scanner) throws IOException {
        String username = promptInput(scanner, "Username: ");
        String password = promptInput(scanner, "Password: ");

        String request = String.format(
                "{\"action\":\"login\",\"username\":\"%s\",\"password\":\"%s\"}",
                username, password
        );

        writer.write(request);
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        return response.contains("\"status\":\"success\"") && response.contains("\"message\":\"Login successful\"");
    }

    private static boolean handleSignup(BufferedWriter writer, BufferedReader reader, Scanner scanner) throws IOException {
        String username = promptInput(scanner, "Username: ");
        String displayName = promptInput(scanner, "Display name: ");
        String password = promptInput(scanner, "Password: ");

        String request = String.format(
                "{\"action\":\"signup\",\"username\":\"%s\",\"display_name\":\"%s\",\"password\":\"%s\"}",
                username, displayName, password
        );

        writer.write(request);
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        return response.contains("\"status\":\"success\"") && response.contains("\"message\":\"Account created successfully\"");
    }

    private static void handleServerConnection(Scanner scanner) {
        try {
            ConnectionAddress serverAddress = new ConnectionAddress("Ip and port.txt");
            try (Socket clientSocket = new Socket(serverAddress.getIpAddress(), serverAddress.getPort());
                 BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());) {

                System.out.println("Connected to server at " + serverAddress.getIpAddress() + ":" + serverAddress.getPort());

                String name = promptInput(scanner, "Enter your name: ");
                outToServer.writeBytes(name + "\n");

                Thread listenThread = new Thread(() -> listenForServerMessages(inFromServer));
                listenThread.start();

                handleUserMessages(scanner, outToServer);

            }
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    private static void listenForServerMessages(BufferedReader inFromServer) {
        try {
            String serverMessage;
            while ((serverMessage = inFromServer.readLine()) != null) {
                if (serverMessage.startsWith("Currently connected clients:")) {
                    updateConnectedClientsList(serverMessage);
                    MessageUtils.displayStringArray(connectedClients.toArray(new String[0]));
                } else {
                    System.out.println("\n" + serverMessage);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from server: " + e.getMessage());
        }
    }

    private static void handleUserMessages(Scanner scanner, DataOutputStream outToServer) throws IOException {
        StringBuilder messageBuilder = new StringBuilder();

        while (true) {
            System.out.print("\rEnter message (or 'exit' to quit): " + messageBuilder);
            char charInput = (char) System.in.read();

            if (charInput == '\n') {
                String message = messageBuilder.toString();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                outToServer.writeBytes(message + "\n");
                messageBuilder.setLength(0);
            } else {
                messageBuilder.append(charInput);
            }
        }
    }

    private static void updateConnectedClientsList(String serverMessage) {
        connectedClients.clear();
        connectedClients.addAll(List.of(MessageUtils.makeStringArray(MessageUtils.trimString(serverMessage, "Currently connected clients:"))));
    }

    private static String promptInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}