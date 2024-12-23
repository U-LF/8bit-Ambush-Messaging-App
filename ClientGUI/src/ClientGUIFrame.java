import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ClientGUIFrame {
    private final DataOutputStream outToServer;
    private JTextArea messageArea;
    private JTextField inputField;

    public ClientGUIFrame(DataOutputStream outToServer) {
        this.outToServer = outToServer;
    }

    public void showGUI() {
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

        // Create a panel for the input field and buttons
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        JButton configButton = createConfigButton();

        // Add components to the input panel
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(configButton, BorderLayout.WEST);

        // Add panels to the frame
        frame.add(messagePanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Show the frame
        frame.setVisible(true);

        // Send the message when Enter key is pressed
        inputField.addActionListener(this::sendMessage);

        // Add action listener for the send button
        sendButton.addActionListener(this::sendMessage);
    }

    private JButton createConfigButton() {
        // Load the config icon
        ImageIcon configIcon = new ImageIcon("config-icon.png");

        // Resize the icon if necessary
        Image resizedImage = configIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JButton configButton = new JButton(resizedIcon);

        // Set button size
        configButton.setPreferredSize(new Dimension(30, 30)); // Adjust as needed
        configButton.setContentAreaFilled(false);
        configButton.setBorderPainted(false);
        configButton.setFocusPainted(false);
        configButton.setToolTipText("Edit Config");

        // Add action listener for the config button
        configButton.addActionListener(e -> openConfigEditor());

        return configButton;
    }

    public void appendMessage(String message) {
        //messageArea.append(message + "\n");
        messageArea.append(message);
    }

    private void sendMessage(ActionEvent event) {
        try {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                // Append the message to the chat area for the client
                appendMessage("You: " + message + "\n");

                // Send the message to the server
                outToServer.writeBytes(message + "\n");

                // Clear the input field
                inputField.setText("");
            }
        } catch (IOException e) {
            appendMessage("Error sending message: " + e.getMessage() + "\n");
        }
    }

    private void openConfigEditor() {
        JFrame configFrame = new JFrame("Edit Config");
        configFrame.setSize(400, 300);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setLayout(new BorderLayout());

        JTextArea configTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(configTextArea);
        configFrame.add(scrollPane, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        configFrame.add(saveButton, BorderLayout.SOUTH);

        // Load the current content of the config file
        try (BufferedReader reader = new BufferedReader(new FileReader("Ip and port.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                configTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(configFrame, "Error loading config file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Save the edited content back to the file
        saveButton.addActionListener(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Ip and port.txt"))) {
                writer.write(configTextArea.getText());
                JOptionPane.showMessageDialog(configFrame, "Config saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(configFrame, "Error saving config file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        configFrame.setLocationRelativeTo(null);
        configFrame.setVisible(true);
    }
}
