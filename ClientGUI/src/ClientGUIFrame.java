import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;

public class ClientGUIFrame extends Component {
    private final DataOutputStream outToServer;
    private final JTextArea messageArea;
    private final JTextField inputField;
    private String username = "User"; // Default username
    private ThemeManagerDashboard themeManager;
    private final ActiveUsersManager activeUsersManager;
    private final SettingsManager settingsManager;
    private final MessageAppender messageAppender;
    private JList<String> activeUsersList; // JList to display active users
    private DashboardFrame dashboardFrame; // Reference to the DashboardFrame
    private Socket clientSocket;

    public ClientGUIFrame(DataOutputStream outToServer, Socket clientSocket) {
        this.outToServer = outToServer;
        this.themeManager = new ThemeManagerDashboard(); // Correct initialization
        this.messageArea = new JTextArea();
        this.inputField = new RoundedTextField(20);
        this.activeUsersManager = new ActiveUsersManager(themeManager);
        this.settingsManager = new SettingsManager();
        this.messageAppender = new MessageAppender(messageArea); // This should work fine now
        this.dashboardFrame = new DashboardFrame(); // Initialize the reference correctly
        this.clientSocket = clientSocket;
    }

    public void setUsername (String username){
        if(!(username == null || username.trim().isEmpty())){
            this.username = username;
        }
    }

    public void showGUI() {
        JFrame frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout());

        // Message area panel
        JPanel messagePanel = new MessagePanelFactory(themeManager).createMessagePanel(messageArea);

        JButton sendButton = SendButtonFactory.createSendButton(); // Create send button


        // Message area panel with correct scroll behavior
        JScrollPane messageScrollPane = createMessageScrollPane();

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField.setBackground(Color.LIGHT_GRAY);
        inputField.setForeground(Color.BLACK);

        // Add inputField and sendButton to inputPanel
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Sidebar for active users
        JPanel sidebarPanel = createSidebarPanel();
        frame.add(sidebarPanel, BorderLayout.WEST);

        // Back to Dashboard Button
        JButton backToDashboardButton = new JButton("Back to Dashboard");
        backToDashboardButton.setPreferredSize(new Dimension(200, 40)); // Adjust size as needed
        backToDashboardButton.setFont(new Font("Muli", Font.BOLD, 16));
        backToDashboardButton.setBackground(new Color(0, 123, 255)); // Button color
        backToDashboardButton.setForeground(Color.WHITE);
        backToDashboardButton.setFocusPainted(false);

        // Action listener for the button
        backToDashboardButton.addActionListener(e -> {
            frame.dispose(); // Close the chat window
            ClientConnection.CloseConnection(clientSocket);
            dashboardFrame.showDashboard(); // Show the dashboard again
        });

        // Add the button at the top of the frame
        frame.add(backToDashboardButton, BorderLayout.NORTH);

        frame.add(messageScrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add ActionListener for send button and input field
        sendButton.addActionListener(e -> MessageSender.sendMessage(outToServer, inputField, messageArea, username));
        inputField.addActionListener(e -> MessageSender.sendMessage(outToServer, inputField, messageArea, username));

        // Global KeyListener for backspace
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleBackspaceKey(frame, e);
            }
        });

        // Add KeyListener to the input field for consistent handling
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleBackspaceKey(frame, e);
            }
        });

        // Ensure the frame always requests focus for the KeyListener
        frame.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                frame.requestFocusInWindow();
            }
        });

        // Request focus to activate the KeyListener
        frame.setFocusable(true);
        frame.requestFocusInWindow();

    }

    private void handleBackspaceKey(JFrame frame, KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (inputField.isFocusOwner()) {
                // If the input field is focused
                if (inputField.getText().isEmpty()) {
                    // Input field is empty, show the back-to-dashboard prompt
                    int confirm = JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to return to the dashboard?",
                            "Confirm Navigation",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        frame.dispose(); // Close the chat window
                        ClientConnection.CloseConnection(clientSocket);
                        dashboardFrame.showDashboard(); // Show the dashboard again
                    }
                }
            } else {
                // If focus is anywhere else, directly show the back-to-dashboard prompt
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to return to the dashboard?",
                        "Confirm Navigation",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose(); // Close the chat window
                    ClientConnection.CloseConnection(clientSocket);
                    dashboardFrame.showDashboard(); // Show the dashboard again
                }
            }
        }
    }

    private JScrollPane createMessageScrollPane() {
        JScrollPane messageScrollPane = new JScrollPane(messageArea);

        // Make sure the JTextArea has word wrap enabled
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false); // Prevent editing directly

        // Hide the default scrollbars and apply the custom scrollbar
        messageScrollPane.getVerticalScrollBar().setUI(new SlimScrollBarUI());
        messageScrollPane.getHorizontalScrollBar().setUI(new SlimScrollBarUI());

        // Optionally set the size of the scrollbars
        messageScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Vertical scrollbar width
        messageScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8)); // Horizontal scrollbar height

        // Hide default scrollbar and only show custom scrollbar when needed
        messageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);  // Show vertical scrollbar only when needed
        messageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);  // Show horizontal scrollbar only when needed

        return messageScrollPane;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(200, 0)); // Set sidebar width
        sidebarPanel.setBackground(new Color(240, 240, 240));

        JLabel sidebarTitle = new JLabel("Active Users", SwingConstants.CENTER);
        sidebarTitle.setFont(new Font("Arial", Font.BOLD, 16));
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Active users list
        activeUsersList = new JList<>();
        activeUsersList.setFont(new Font("Arial", Font.PLAIN, 14));
        activeUsersList.setFixedCellHeight(30);
        activeUsersList.setBackground(new Color(245, 245, 245));
        JScrollPane listScrollPane = new JScrollPane(activeUsersList);

        sidebarPanel.add(sidebarTitle, BorderLayout.NORTH);
        sidebarPanel.add(listScrollPane, BorderLayout.CENTER);

        // Initial population of active users
        updateActiveUsersList(new String[]{" "});

        return sidebarPanel;
    }

    public void updateActiveUsersList(String[] activeUsers) {
        activeUsersList.setListData(activeUsers);
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

    public void appendMessage(String message) {
        messageAppender.appendMessage(message);
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public JTextField getInputField() {
        return inputField;
    }

    public ThemeManagerDashboard getThemeManager() {
        return themeManager;
    }
}
