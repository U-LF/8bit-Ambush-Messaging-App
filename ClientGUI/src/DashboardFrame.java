import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.border.Border;

public class DashboardFrame {

    public void showDashboard() {
        // Create the main dashboard frame
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setMinimumSize(new Dimension(500, 400));  // Set a minimum size
        dashboardFrame.setPreferredSize(new Dimension(700, 500));

        // Set the background image
        String imageUrl = "https://img.freepik.com/free-photo/photorealistic-view-wild-bear-its-natural-environment_23-2151427243.jpg";
        try {
            ImageIcon backgroundImage = new ImageIcon(new URL(imageUrl)); // Load image from URL
            JLabel backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setLayout(new BorderLayout());
            dashboardFrame.setContentPane(backgroundLabel);  // Set background image

            // Dynamic resizing of the background image on window resize
            dashboardFrame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent evt) {
                    // Resize background image based on window size
                    ImageIcon resizedIcon = new ImageIcon(backgroundImage.getImage().getScaledInstance(
                            dashboardFrame.getWidth(), dashboardFrame.getHeight(), Image.SCALE_SMOOTH));
                    backgroundLabel.setIcon(resizedIcon);
                }
            });

        } catch (MalformedURLException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }

        dashboardFrame.setLayout(new GridBagLayout());

        // Create neon-style buttons
        JButton connectButton = createStyledButton("Connect to Server", e -> {
            dashboardFrame.dispose();
            ClientGUI.connectToServer();
        });

        JButton configButton = createStyledButton("Config", e -> {
            dashboardFrame.dispose();
            openConfigEditor(dashboardFrame);
        });

        // Panel for buttons with centered layout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 10, 30, 10); // Padding between buttons

        // Add buttons to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(connectButton, gbc);

        gbc.gridy++;
        buttonPanel.add(configButton, gbc);

        // Add the button panel to the frame
        dashboardFrame.add(buttonPanel, gbc);

        // Enable dynamic resizing of button fonts
        dashboardFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                int newFontSize = Math.max(20, dashboardFrame.getWidth() / 35);
                connectButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
                configButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
            }
        });

        // Set frame properties
        dashboardFrame.pack();
        dashboardFrame.setLocationRelativeTo(null); // Center on screen
        dashboardFrame.setVisible(true);
    }

    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // Initial styles
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 0, 0));
                button.setForeground(new Color(0, 255, 255));
                button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 4));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(30, 30, 30));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(Color.BLACK);
            }
        });

        // Add action listener
        button.addActionListener(actionListener);

        return button;
    }

    private void openConfigEditor(JFrame dashboardFrame) {
        JFrame configFrame = new JFrame("Edit Config");
        configFrame.setSize(400, 300);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setLayout(new BorderLayout());

        JTextArea configTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(configTextArea);
        configFrame.add(scrollPane, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        configFrame.add(saveButton, BorderLayout.SOUTH);

        // Load current content of the config file
        try (BufferedReader reader = new BufferedReader(new FileReader("Ip and port.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                configTextArea.append(line + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(configFrame, "Error loading config file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Save edited content to the file
        saveButton.addActionListener(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Ip and port.txt"))) {
                writer.write(configTextArea.getText());
                JOptionPane.showMessageDialog(configFrame, "Config saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                configFrame.dispose();
                dashboardFrame.setVisible(true); // Show dashboard again
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(configFrame, "Error saving config file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        configFrame.setLocationRelativeTo(null);
        configFrame.setVisible(true);
    }
}
