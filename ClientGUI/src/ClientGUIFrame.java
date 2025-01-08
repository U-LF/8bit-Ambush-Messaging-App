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
    private ThemeManager themeManager;
    private ActiveUsersManager activeUsersManager; // New ActiveUsersManager instance
    private SettingsManager settingsManager; // New SettingsManager instance

    public ClientGUIFrame(DataOutputStream outToServer) {

        this.outToServer = outToServer;
        this.themeManager = new ThemeManager(isDarkMode);
        this.activeUsersManager = new ActiveUsersManager(); // Initialize ActiveUsersManager
        this.settingsManager = new SettingsManager(); // Initialize SettingsManager
    }

    public void showGUI() {
        JFrame frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        frame.setLayout(new BorderLayout());

        JPanel messagePanel = createMessagePanel();
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new RoundedTextField(20);
        inputField.setBackground(Color.LIGHT_GRAY);
        inputField.setForeground(Color.BLACK);

        JButton sendButton = createSendButton();
        JButton activeUsersButton = activeUsersManager.createActiveUsersButton(); // Use method from ActiveUsersManager
        JButton settingsButton = settingsManager.createSettingsButton(this); // Use method from SettingsManager

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), 80));
        topPanel.setBackground(new Color(70, 130, 180)); // Steel Blue background

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false); // Transparent background
        leftPanel.add(settingsButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false); // Transparent background
        rightPanel.add(activeUsersButton);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(messagePanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeButtons(frame);
            }
        });

        themeManager.updateTheme(frame, messageArea, inputField);
    }

    private JPanel createMessagePanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            private Image backgroundImage;

            {
                try {
                    URL imageUrl = new URL("https://www.shutterstock.com/image-vector/social-media-sketch-vector-seamless-600nw-1660950727.jpg");
                    backgroundImage = new ImageIcon(imageUrl).getImage();
                } catch (Exception e) {
                    System.err.println("Error loading background image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setOpaque(false); // Make the text area transparent
        messageArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JButton createSendButton() {
        try {
            URL sendButtonUrl = new URL("https://cdn-icons-png.freepik.com/256/4458/4458496.png?semt=ais_hybrid");
            ImageIcon sendIcon = new ImageIcon(sendButtonUrl);
            Image img = sendIcon.getImage();
            int newSize = 40;
            Image resizedImg = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
            sendIcon = new ImageIcon(resizedImg);

            JButton sendButton = new JButton(sendIcon);
            sendButton.setPreferredSize(new Dimension(newSize, newSize));
            sendButton.setContentAreaFilled(false);
            sendButton.setFocusPainted(false);
            return sendButton;
        } catch (Exception e) {
            System.err.println("Error loading send button icon: " + e.getMessage());
            return new JButton("Send");
        }
    }


    void changeUsername(JDialog settingsDialog) {
        String newUsername = JOptionPane.showInputDialog(settingsDialog, "Enter new username:", username);
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            username = newUsername.trim();
            sendUsernameToServer();
        }
    }

    private void sendUsernameToServer() {
        try {
            outToServer.writeBytes("UPDATE_USERNAME " + username + "\n");
        } catch (IOException e) {
            System.err.println("Error updating username in server: " + e.getMessage());
        }
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

    private void resizeButtons(JFrame frame) {
        int newSize = Math.min(frame.getWidth(), frame.getHeight()) / 15;
        for (Component comp : frame.getComponents()) {
            if (comp instanceof JButton) {
                comp.setPreferredSize(new Dimension(newSize, newSize));
            }
        }
    }
    // Getter for themeManager
    public ThemeManager getThemeManager() {
        return themeManager;
    }

    // Getter for messageArea
    public JTextArea getMessageArea() {
        return messageArea;
    }

    // Getter for inputField
    public JTextField getInputField() {
        return inputField;
    }
}


