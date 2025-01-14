import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, switchToSignupButton;
    private JLabel errorLabel;

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        switchToSignupButton = new JButton("Go to Signup");
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginButton);
        formPanel.add(switchToSignupButton);

        add(formPanel, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);

        loginButton.addActionListener(this::handleLogin);
        switchToSignupButton.addActionListener(e -> {
            dispose();
            new SignupFrame();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (BackendService.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
            new DashboardFrame().showDashboard();
        } else {
            errorLabel.setText("Invalid credentials.");
        }
    }
}
