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
        setSize(800, 500); // Adjusted frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background panel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, 800, 500);
        backgroundPanel.setBackground(Color.WHITE);

        // Add Image on the left side
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(50, 50, 350, 400); // Adjust size and position
        try {
            // Load image from the "icons" package
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/login.png"));
            Image scaledImage = imageIcon.getImage().getScaledInstance(350, 400, Image.SCALE_SMOOTH); // Scaling image
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setText("Image not found!");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        backgroundPanel.add(imageLabel);

        // Form Panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(450, 50, 300, 300);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 280, 30);

        JLabel usernameLabel = new JLabel("User Name");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setBounds(10, 50, 280, 20);

        usernameField = new JTextField();
        usernameField.setBounds(10, 70, 280, 30);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(10, 110, 280, 20);

        passwordField = new JPasswordField();
        passwordField.setBounds(10, 130, 280, 30);

        loginButton = new JButton("Login");
        loginButton.setBounds(10, 170, 280, 30);

        switchToSignupButton = new JButton("Register");
        switchToSignupButton.setBounds(10, 210, 280, 30);
        switchToSignupButton.setForeground(Color.BLUE);
        switchToSignupButton.setContentAreaFilled(false);
        switchToSignupButton.setBorderPainted(false);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(10, 250, 280, 20);

        formPanel.add(titleLabel);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginButton);
        formPanel.add(switchToSignupButton);
        formPanel.add(errorLabel);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);

        // Login button listener
        loginButton.addActionListener(this::handleLogin);

        // Switch to Signup screen
        switchToSignupButton.addActionListener(e -> {
            dispose();
            new SignupFrame();
        });

        // Add Enter key action listener to password field
        passwordField.addActionListener(this::handleLogin);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Handle login logic
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
