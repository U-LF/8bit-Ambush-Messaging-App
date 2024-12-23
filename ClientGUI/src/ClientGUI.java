import javax.swing.*;
import java.io.*;
import java.net.*;

import ClientShared.ConnectionAddress;

public class ClientGUI {
    private static DataOutputStream outToServer;

    public static void main(String[] args) {
        // Create and show the Dashboard first
        SwingUtilities.invokeLater(() -> {
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.showDashboard();
        });
    }

    // Method to connect to the server
    public static void connectToServer() {
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
            ClientGUIFrame gui = new ClientGUIFrame(outToServer);
            gui.showGUI();

            // Get the client's name
            String name = JOptionPane.showInputDialog(null, "Enter your name:", "Welcome", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.trim().isEmpty()) {
                name = "Anonymous";
            }
            outToServer.writeBytes(name + "\n");

            // Thread to listen for messages from the server
            Thread listenThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = inFromServer.readLine()) != null) {
                        gui.appendMessage(serverMessage + "\n");
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
}