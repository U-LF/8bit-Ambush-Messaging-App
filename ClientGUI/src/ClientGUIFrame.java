import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ClientGUIFrame extends Component {
    private final DataOutputStream outToServer;
    private final JTextArea messageArea;
    private final JTextField inputField;
    private String username = "Anonymous"; // Default username
    private ThemeManagerDashboard themeManager; // Removed redundant initialization
    private final ActiveUsersManager activeUsersManager;
    private final SettingsManager settingsManager;
    private final MessageAppender messageAppender;

    public ClientGUIFrame(DataOutputStream outToServer) {
        this.outToServer = outToServer;
        this.themeManager = new ThemeManagerDashboard(); // Correct initialization
        this.messageArea = new JTextArea();
        this.inputField = new RoundedTextField(20);
        this.activeUsersManager = new ActiveUsersManager(themeManager);
        this.settingsManager = new SettingsManager();
        this.messageAppender = new MessageAppender(messageArea);
    }

    public void showGUI() {
        JFrame frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        frame.setLayout(new BorderLayout());

        JPanel messagePanel = new MessagePanelFactory(themeManager).createMessagePanel(messageArea);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField.setBackground(Color.LIGHT_GRAY);
        inputField.setForeground(Color.BLACK);

        JButton sendButton = SendButtonFactory.createSendButton();
        JButton activeUsersButton = activeUsersManager.createActiveUsersButton(frame);
        JButton settingsButton = settingsManager.createSettingsButton(this);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), 80));
        topPanel.setBackground(new Color(70, 130, 180));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(settingsButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
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

        sendButton.addActionListener(e -> MessageSender.sendMessage(outToServer, inputField, messageArea, username));
        inputField.addActionListener(e -> MessageSender.sendMessage(outToServer, inputField, messageArea, username));
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
