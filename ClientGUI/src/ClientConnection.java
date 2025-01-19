import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

import ClientShared.ConnectionAddress;
import ClientShared.MessageUtils;

public class ClientConnection {
    private static DataOutputStream outToServer;
    private static final List<String> connectedClients = new ArrayList<>();

    // Method to connect to the server
    public static void connectToServer(JFrame dashboardFrame) {
        try {
            // Load server connection details
            ConnectionAddress serverAddress = new ConnectionAddress("Ip and port.txt");

            // Connect to the server
            Socket clientSocket = new Socket(serverAddress.getIpAddress(), serverAddress.getPort());
            System.out.println("Connected to server at " + serverAddress.getIpAddress() + ":" + serverAddress.getPort());

            // Initialize streams for communication
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Create and show the GUI
            ClientGUIFrame gui = new ClientGUIFrame(outToServer, clientSocket);
            gui.showGUI();

            // Get the client's name
            String name = JOptionPane.showInputDialog(null, "Enter your name:", "Welcome", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.trim().isEmpty()) {
                name = "Anonymous";
            }
            gui.setUsername(name);
            outToServer.writeBytes(name + "\n");

            // Thread to listen for messages from the server
            Thread listenThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = inFromServer.readLine()) != null) {
                        if (serverMessage.startsWith("Currently connected clients:")) {
                            updateConnectedClientsList(serverMessage);
                            gui.updateActiveUsersList(connectedClients.toArray(new String[0]));
                        } else {
                            gui.appendMessage(serverMessage + "\n");
                        }
                    }
                } catch (IOException e) {
                    gui.appendMessage("Error reading from server: " + e.getMessage() + "\n");
                }
            });
            listenThread.start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void CloseConnection(Socket clientSocket){
        try{
            clientSocket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateConnectedClientsList(String serverMessage) {
        connectedClients.clear();
        connectedClients.addAll(List.of(MessageUtils.makeStringArray(MessageUtils.trimString(serverMessage, "Currently connected clients:"))));
    }
}