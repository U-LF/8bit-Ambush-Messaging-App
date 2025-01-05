import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

public class ClientGUIFrame {
    private final DataOutputStream outToServer;
    private JTextArea messageArea;
    private JTextField inputField;
    private String username = "Anonymous"; // Default username
    private boolean isDarkMode = false; // Flag to toggle between Dark and Light mode

    public ClientGUIFrame(DataOutputStream outToServer) {
        this.outToServer = outToServer;
    }

    public void showGUI() {
        JFrame frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Use BorderLayout for flexible positioning
        frame.setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel(new BorderLayout());
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        messagePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();

        // Create the send button using the image URL you provided
        JButton sendButton = createSendButton();

        // Create Active Users Button (Top-right corner)
        JButton activeUsersButton = createActiveUsersButton();

        // Create Settings Button (Top-left corner)
        JButton settingsButton = createSettingsButton();

        // Add buttons to the frame with dynamic resizing
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));

        // Add buttons to top panel (left and right positioning)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(settingsButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(activeUsersButton);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // Set up the input panel
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);  // Add the top panel (with buttons)
        frame.add(messagePanel, BorderLayout.CENTER);  // Add the message area
        frame.add(inputPanel, BorderLayout.SOUTH);  // Add the input panel

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        // Dynamically resize buttons when the frame size changes
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeButtons(frame);
            }
        });

        // Initial theme application
        updateTheme(frame);
    }

    private JButton createSendButton() {
        try {
            URL sendButtonUrl = new URL("https://cdn-icons-png.freepik.com/256/4458/4458496.png?semt=ais_hybrid");
            ImageIcon sendIcon = new ImageIcon(sendButtonUrl);
            Image img = sendIcon.getImage();  // Get the image
            int newSize = 40; // Set a larger size for the image
            Image resizedImg = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);  // Resize the image
            sendIcon = new ImageIcon(resizedImg);  // Set resized image

            JButton sendButton = new JButton(sendIcon);
            sendButton.setPreferredSize(new Dimension(newSize, newSize));
            sendButton.setBackground(Color.BLACK);
            sendButton.setContentAreaFilled(false);  // Make button background transparent
            sendButton.setFocusPainted(false);  // Remove focus paint
            return sendButton;
        } catch (Exception e) {
            System.err.println("Error loading send button icon: " + e.getMessage());
            return new JButton("Send");
        }
    }

    private JButton createActiveUsersButton() {
        // Active Users button with image from URL
        try {
            URL activeUsersUrl = new URL("https://www.voxco.com/wp-content/uploads/2022/03/DAILY-ACTIVE-USERS1.png");
            ImageIcon activeUsersIcon = new ImageIcon(activeUsersUrl);
            Image img = activeUsersIcon.getImage();  // Get the image
            int newSize = 50; // Set a larger size for the image
            Image resizedImg = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);  // Resize the image
            activeUsersIcon = new ImageIcon(resizedImg);  // Set resized image

            JButton activeUsersButton = new JButton(activeUsersIcon);
            activeUsersButton.setPreferredSize(new Dimension(newSize, newSize));
            activeUsersButton.setBackground(Color.BLACK);
            activeUsersButton.setContentAreaFilled(false);  // Make button background transparent
            activeUsersButton.setFocusPainted(false);  // Remove focus paint
            activeUsersButton.addActionListener(e -> showActiveUsersDialog());
            return activeUsersButton;
        } catch (Exception e) {
            System.err.println("Error loading active users icon: " + e.getMessage());
            return new JButton("Active Users");
        }
    }

    private void showActiveUsersDialog() {
        // Show active users list
        JOptionPane.showMessageDialog(null, "Active Users List");
    }

    private JButton createSettingsButton() {
        // Settings button with image from URL
        try {
            URL settingsUrl = new URL("https://png.pngtree.com/element_our/png/20181227/settings-glyph-black-icon-png_292947.jpg");
            ImageIcon settingsIcon = new ImageIcon(settingsUrl);
            Image img = settingsIcon.getImage();  // Get the image
            int newSize = 50; // Set a larger size for the image
            Image resizedImg = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);  // Resize the image
            settingsIcon = new ImageIcon(resizedImg);  // Set resized image

            JButton settingsButton = new JButton(settingsIcon);
            settingsButton.setPreferredSize(new Dimension(newSize, newSize));
            settingsButton.setBackground(Color.BLACK);
            settingsButton.setContentAreaFilled(false);  // Make button background transparent
            settingsButton.setFocusPainted(false);  // Remove focus paint
            settingsButton.addActionListener(e -> openSettingsDialog());
            return settingsButton;
        } catch (Exception e) {
            System.err.println("Error loading settings icon: " + e.getMessage());
            return new JButton("Settings");
        }
    }

    private void openSettingsDialog() {
        // Instead of null, pass the frame to set it as parent for the dialog
        JDialog settingsDialog = new JDialog((Frame) null, "Settings", true);
        settingsDialog.setSize(300, 200);
        settingsDialog.setLayout(new GridLayout(3, 1));

        // Button for username update
        JButton usernameButton = new JButton("Change Username");
        usernameButton.addActionListener(e -> changeUsername(settingsDialog));
        settingsDialog.add(usernameButton);

        // Button for switching themes
        JButton themeButton = new JButton("Switch Theme");
        themeButton.addActionListener(e -> switchTheme(settingsDialog));
        settingsDialog.add(themeButton);

        settingsDialog.setLocationRelativeTo(null);
        settingsDialog.setVisible(true);
    }

    private void changeUsername(JDialog settingsDialog) {
        String newUsername = JOptionPane.showInputDialog(settingsDialog, "Enter new username:", username);
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            username = newUsername.trim();
            sendUsernameToServer();  // Send the updated username to the server
        }
    }

    private void sendUsernameToServer() {
        try {
            outToServer.writeBytes("UPDATE_USERNAME " + username + "\n");
        } catch (IOException e) {
            System.err.println("Error updating username in server: " + e.getMessage());
        }
    }

    private void switchTheme(JDialog settingsDialog) {
        String[] options = {"Light Mode", "Dark Mode"};
        int choice = JOptionPane.showOptionDialog(settingsDialog, "Choose Theme", "Switch Theme", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            // Light mode
            isDarkMode = false;
            updateTheme((JFrame) SwingUtilities.getRoot(messageArea));  // Apply theme to root frame
        } else {
            // Dark mode
            isDarkMode = true;
            updateTheme((JFrame) SwingUtilities.getRoot(messageArea));  // Apply theme to root frame
        }
    }

    private void updateTheme(JFrame frame) {
        // Change colors for light mode or dark mode
        if (isDarkMode) {
            UIManager.put("Panel.background", Color.DARK_GRAY);
            UIManager.put("TextArea.background", Color.BLACK);
            UIManager.put("TextArea.foreground", Color.WHITE);
            UIManager.put("Button.background", Color.BLACK);
            UIManager.put("Button.foreground", Color.WHITE);
            messageArea.setBackground(Color.BLACK);
            messageArea.setForeground(Color.WHITE);
            inputField.setBackground(Color.BLACK);
            inputField.setForeground(Color.WHITE);
        } else {
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("TextArea.background", Color.WHITE);
            UIManager.put("TextArea.foreground", Color.BLACK);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);
            messageArea.setBackground(Color.WHITE);
            messageArea.setForeground(Color.BLACK);
            inputField.setBackground(Color.WHITE);
            inputField.setForeground(Color.BLACK);
        }

        // Apply changes to all UI components
        SwingUtilities.updateComponentTreeUI(frame); // Update entire frame
    }

    private void sendMessage() {
        try {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                appendMessage(username + ": " + message + "\n");
                outToServer.writeBytes(message + "\n");
                inputField.setText("");
            }
        } catch (IOException e) {
            appendMessage("Error sending message: " + e.getMessage() + "\n");
        }
    }

    public void appendMessage(String message) {
        messageArea.append(message);
    }

    // Resize buttons dynamically when the frame is resized
    private void resizeButtons(JFrame frame) {
        // Get the width and height of the frame
        int width = frame.getWidth();
        int height = frame.getHeight();

        // Set new button sizes based on frame size
        int newSize = Math.min(width, height) / 15; // Dynamic size proportional to window
        for (Component comp : frame.getComponents()) {
            if (comp instanceof JButton) {
                comp.setPreferredSize(new Dimension(newSize, newSize));
            }
        }
    }
}
