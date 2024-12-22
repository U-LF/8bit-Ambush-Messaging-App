import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;

public class ClientGUI {
    private static JTextArea messageArea; // To display received messages
    private static JTextField inputField; // To type messages
    private static DataOutputStream outToServer;

    public static void main(String[] args) {
        try {
            // Load server connection details
            ConnectionAddress serverAddress = new ConnectionAddress("Ip and port.txt");

            // Connect to the server
            Socket clientSocket = new Socket(serverAddress.getIpAddress(), serverAddress.getPort());
            System.out.println("Connected to server at " + serverAddress.getIpAddress() + ":" + serverAddress.getPort());

            // Initialize streams for communication
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Create GUI for the client
            createGUI();

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
                        messageArea.append(serverMessage + "\n");
                    }
                } catch (IOException e) {
                    messageArea.append("Error reading from server: " + e.getMessage() + "\n");
                }
            });
            listenThread.start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Messaging App by 8bit Ambush");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Create a panel for the message area
        JPanel messagePanel = new JPanel(new BorderLayout());
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        messagePanel.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the input field and send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        // Add action listener for the send button
        sendButton.addActionListener(ClientGUI::sendMessage);

        // Add components to the input panel
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add panels to the frame
        frame.add(messagePanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Show the frame
        frame.setVisible(true);

        // Send the message when Enter key is pressed
        inputField.addActionListener(ClientGUI::sendMessage);
    }

    private static void sendMessage(ActionEvent event) {
        try {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                outToServer.writeBytes(message + "\n");
                inputField.setText(""); // Clear the input field
            }
        } catch (IOException e) {
            messageArea.append("Error sending message: " + e.getMessage() + "\n");
        }
    }
}