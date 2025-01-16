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
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/signup.png"));
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
        formPanel.setBounds(450, 50, 300, 350); // Adjusted form panel size
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        JLabel titleLabel = new JLabel("Signup");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 280, 30);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setBounds(10, 50, 280, 20);

        usernameField = new JTextField();
        usernameField.setBounds(10, 70, 280, 30);

        JLabel displayNameLabel = new JLabel("Display Name");
        displayNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        displayNameLabel.setBounds(10, 110, 280, 20);

        displayNameField = new JTextField();
        displayNameField.setBounds(10, 130, 280, 30);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(10, 170, 280, 20);

        passwordField = new JPasswordField();
        passwordField.setBounds(10, 190, 280, 30);

        signupButton = new JButton("Signup");
        signupButton.setBounds(10, 230, 280, 30);

        switchToLoginButton = new JButton("Go to Login");
        switchToLoginButton.setBounds(10, 270, 280, 30);
        switchToLoginButton.setForeground(Color.BLUE);
        switchToLoginButton.setContentAreaFilled(false);
        switchToLoginButton.setBorderPainted(false);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(10, 310, 280, 20);

        formPanel.add(titleLabel);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(displayNameLabel);
        formPanel.add(displayNameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(signupButton);
        formPanel.add(switchToLoginButton);
        formPanel.add(errorLabel);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);

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
