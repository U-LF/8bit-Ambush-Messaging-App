import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ClientGUIFrame extends Component {
    private final DataOutputStream outToServer;
    private final JTextArea messageArea;
    private final JTextField inputField;
    private String username = "Anonymous"; // Default username
    private ThemeManagerDashboard themeManager;
    private final ActiveUsersManager activeUsersManager;
    private final SettingsManager settingsManager;
    private final MessageAppender messageAppender;
    private JList<String> activeUsersList; // JList to display active users
    private DashboardFrame dashboardFrame; // Reference to the DashboardFrame

    public ClientGUIFrame(DataOutputStream outToServer) {
        this.outToServer = outToServer;
        this.themeManager = new ThemeManagerDashboard(); // Correct initialization
        this.messageArea = new JTextArea();
        this.inputField = new RoundedTextField(20);
        this.activeUsersManager = new ActiveUsersManager(themeManager);
        this.settingsManager = new SettingsManager();
        this.messageAppender = new MessageAppender(messageArea);
        this.dashboardFrame = new DashboardFrame(); // Initialize the reference correctly
    }

    public void showGUI() {
        JFrame frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout());

        // Message area panel
        JPanel messagePanel = new MessagePanelFactory(themeManager).createMessagePanel(messageArea);

        JButton sendButton = SendButtonFactory.createSendButton(); // Create send button

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
        backToDashboardButton.setFont(new Font("Arial", Font.BOLD, 14));
        backToDashboardButton.setBackground(new Color(0, 123, 255)); // Button color
        backToDashboardButton.setForeground(Color.WHITE);
        backToDashboardButton.setFocusPainted(false);

        // Action listener for the button
        backToDashboardButton.addActionListener(e -> {
            frame.dispose(); // Close the chat window
            dashboardFrame.showDashboard(); // Show the dashboard again
        });

        // Add the button at the top of the frame
        frame.add(backToDashboardButton, BorderLayout.NORTH);

        frame.add(messagePanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        sendButton.addActionListener(e -> MessageSender.sendMessage(outToServer, inputField, messageArea, username));
        inputField.addActionListener(e -> MessageSender.sendMessage(outToServer, inputField, messageArea, username));
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
        updateActiveUsersList(new String[]{"Hafiz Sohaib", "Abdullah", "Khuzaima", "BugFixer"});

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
