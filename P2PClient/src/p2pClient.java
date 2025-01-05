import java.io.*;
import java.net.*;

public class p2pClient {
    private static final String SERVER_ADDRESS = "203.101.178.27";
    private static final int SERVER_PORT = 2456;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            // Start a separate thread to handle incoming messages from the server
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            }).start();

            String clientName = null;

            while (true) {
                System.out.print("Enter your input: ");
                String userCommand = userInput.readLine();

                if (userCommand == null || userCommand.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }

                if (clientName == null && userCommand.trim().isEmpty()) {
                    System.out.println("Please enter your name to proceed.");
                    continue;
                }

                if (clientName == null) {
                    clientName = userCommand;
                }

                out.println(userCommand);

                if (userCommand.startsWith("CONNECT")) {
                    String response = in.readLine(); // Blocking call to get server response
                    if (response != null && response.contains("wants to connect with you")) {
                        System.out.println(response); // Print the request message

                        System.out.print("Do you want to accept the connection? (yes/no): ");
                        String userResponse = userInput.readLine();

                        if ("yes".equalsIgnoreCase(userResponse)) {
                            out.println("ACCEPT"); // Send acceptance message to the server

                            // The server should then send the address and port of the requesting client
                            String connectInfo = in.readLine();
                            if (connectInfo != null && connectInfo.startsWith("Connecting you to")) {
                                try {
                                    String[] parts = connectInfo.split(" ");
                                    if (parts.length >= 4 && parts[3].contains(":")) {
                                        String[] addressParts = parts[3].split(":");
                                        String targetAddress = addressParts[0];
                                        int targetPort = Integer.parseInt(addressParts[1]);

                                        // Start the direct connection
                                        startDirectConnection(targetAddress, targetPort, clientName);
                                    } else {
                                        System.err.println("Invalid connection info from server.");
                                    }
                                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                    System.err.println("Error parsing connection info: " + e.getMessage());
                                }
                            } else {
                                System.err.println("Failed to receive connection details.");
                            }
                        } else {
                            out.println("REJECT"); // Send rejection message to the server
                            System.out.println("Connection request rejected.");
                        }
                    } else {
                        System.err.println("Unexpected response: " + response);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    private static void startDirectConnection(String targetAddress, int targetPort, String clientName) {
        try (Socket directSocket = new Socket(targetAddress, targetPort);
             PrintWriter out = new PrintWriter(directSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(directSocket.getInputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to " + targetAddress + ":" + targetPort);

            String message;
            while (true) {
                System.out.print("Enter message to send (or 'exit' to disconnect): ");
                message = userInput.readLine();

                if (message == null || message.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting direct connection...");
                    break;
                }

                // Send message to the connected client
                out.println(clientName + ": " + message);

                // Read response from the other client
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Connection closed by the other client.");
                    break;
                }
                System.out.println("Received: " + response);
            }
        } catch (IOException e) {
            System.err.println("Error during direct connection: " + e.getMessage());
        }
    }
}