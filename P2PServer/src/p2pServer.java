import java.io.*;
import java.net.*;
import java.util.*;

public class p2pServer {
    private static final int SERVER_PORT = 2456;
    private static Map<String, PrintWriter> activeClients = new HashMap<>();
    private static Map<String, String> connectionRequests = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running and waiting for clients to connect...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;
        private String clientAddress;
        private int clientPort;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                String userInput;

                clientAddress = clientSocket.getInetAddress().getHostAddress();
                clientPort = clientSocket.getPort();

                out.println("Welcome to the server. Please enter your name:");
                clientName = in.readLine();

                if (clientName != null && !clientName.trim().isEmpty()) {
                    synchronized (activeClients) {
                        if (activeClients.containsKey(clientName)) {
                            out.println("Name already taken, please choose a different name.");
                            clientSocket.close();
                            return;
                        }
                        activeClients.put(clientName, out);
                    }
                    System.out.println(clientName + " has connected.");

                    // Notify all clients about the new client
                    synchronized (activeClients) {
                        for (PrintWriter writer : activeClients.values()) {
                            writer.println(clientName + " has joined.");
                        }
                    }

                    while ((userInput = in.readLine()) != null) {
                        if (userInput.startsWith("CONNECT")) {
                            handleConnectionRequest(clientName, userInput);
                        } else if (userInput.equalsIgnoreCase("exit")) {
                            break;
                        } else {
                            out.println("Unknown command: " + userInput);
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println("Error with client " + clientName + ": " + e.getMessage());
            } finally {
                try {
                    if (clientName != null) {
                        synchronized (activeClients) {
                            activeClients.remove(clientName);
                        }
                    }
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }

        private void handleConnectionRequest(String clientName, String userInput) {
            String[] parts = userInput.split(" ");
            if (parts.length < 2) {
                out.println("Invalid command. Usage: CONNECT <target_client_name>");
                return;
            }
            String targetClientName = parts[1];

            synchronized (activeClients) {
                if (activeClients.containsKey(targetClientName)) {
                    // Notify the target client of the connection request
                    PrintWriter targetClientOut = activeClients.get(targetClientName);
                    targetClientOut.println(clientName + " wants to connect with you");

                    // Wait for the response (either accept or reject)
                    try {
                        String response = in.readLine();
                        if ("ACCEPT".equalsIgnoreCase(response)) {
                            out.println("Connection request accepted.");
                            targetClientOut.println("Connection request accepted.");

                            // Send the connection details to both clients
                            String connectInfo = "Connecting you to " + clientName + " at " + clientAddress + ":" + clientPort;
                            out.println(connectInfo);
                            targetClientOut.println(connectInfo);

                            // Optionally, forward the target client's connection details to the requesting client
                            String targetClientAddress = targetClientOut.toString();
                            int targetClientPort = 2456; // Set appropriate port
                            String targetConnectInfo = "Connecting you to " + targetClientName + " at " + clientAddress + ":" + clientPort;
                            out.println(targetConnectInfo);
                            targetClientOut.println(targetConnectInfo);

                        } else if ("REJECT".equalsIgnoreCase(response)) {
                            out.println("Connection request rejected.");
                        } else {
                            out.println("Invalid response. Connection request rejected.");
                        }
                    } catch (IOException e) {
                        System.err.println("Error during connection negotiation with " + clientName + ": " + e.getMessage());
                    }
                } else {
                    out.println("Target client " + targetClientName + " is not connected.");
                }
            }
        }
    }
}