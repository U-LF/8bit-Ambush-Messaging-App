import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignupFrame extends JFrame {
    private JTextField usernameField, displayNameField;
    private JPasswordField passwordField;
    private JButton signupButton, switchToLoginButton;
    private JLabel errorLabel;

    public SignupFrame() {
        setTitle("Signup");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel displayNameLabel = new JLabel("Display Name:");
        displayNameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        signupButton = new JButton("Signup");
        switchToLoginButton = new JButton("Go to Login");
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(displayNameLabel);
        formPanel.add(displayNameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(signupButton);
        formPanel.add(switchToLoginButton);

        add(formPanel, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);

        signupButton.addActionListener(this::handleSignup);
        switchToLoginButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleSignup(ActionEvent e) {
        String username = usernameField.getText().trim();
        String displayName = displayNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (BackendService.signup(username, displayName, password)) {
            JOptionPane.showMessageDialog(this, "Signup successful!");
            dispose();
            new LoginFrame();
        } else {
            errorLabel.setText("Signup failed. Try another username.");
        }
    }
}
