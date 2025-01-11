/**
 * The refactored code now includes a dedicated ThemeManager class to manage themes. The DashboardFrame class
 * delegates the responsibility of theme management to ThemeManager.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class DashboardFrame {
    public void showDashboard() {
        // Create the main dashboard frame
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setMinimumSize(new Dimension(500, 400));
        dashboardFrame.setPreferredSize(new Dimension(700, 500));

        // Set the background image
        String imageUrl = "https://img.freepik.com/free-photo/photorealistic-view-wild-bear-its-natural-environment_23-2151427243.jpg";
        try {
            ImageIcon backgroundImage = new ImageIcon(new URL(imageUrl));
            JLabel backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setLayout(new BorderLayout());
            dashboardFrame.setContentPane(backgroundLabel);

            dashboardFrame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent evt) {
                    ImageIcon resizedIcon = new ImageIcon(backgroundImage.getImage().getScaledInstance(
                            dashboardFrame.getWidth(), dashboardFrame.getHeight(), Image.SCALE_SMOOTH));
                    backgroundLabel.setIcon(resizedIcon);
                }
            });
        } catch (MalformedURLException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }

        dashboardFrame.setLayout(new GridBagLayout());

        // Initialize ThemeManager
        ThemeManagerDashboard themeManager = new ThemeManagerDashboard();
        themeManager.updateTheme(dashboardFrame); // Apply the initial theme

        // Create buttons
        JButton connectButton = createStyledButton("Connect to ChatRoom", e -> {
            dashboardFrame.dispose();
            ClientConnection.connectToServer();
        });

        JButton configButton = createStyledButton("Config", e -> {
            dashboardFrame.dispose();
            openConfigEditor(dashboardFrame);
        });

        JButton aboutButton = createStyledButton("About", e -> {
            openAboutDialog(dashboardFrame);
        });

        JButton themeButton = createStyledButton("Theme", e -> {
            themeManager.switchTheme(dashboardFrame);
        });

        // Create P2P button
        JButton p2pButton = createStyledButton("P2P", e -> {
            openP2PDialog(dashboardFrame, themeManager);  // Open the P2P dialog with theme applied
        });


        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between buttons

        // Add P2P button to the button panel
        gbc.gridx = 0; // All buttons in the first column
        gbc.gridy = 0; // First row
        buttonPanel.add(p2pButton, gbc);

        gbc.gridy++; // Move to the next row
        buttonPanel.add(connectButton, gbc);

        gbc.gridy++; // Move to the next row
        buttonPanel.add(configButton, gbc);

        gbc.gridy++; // Move to the next row
        buttonPanel.add(aboutButton, gbc);

        gbc.gridy++; // Move to the next row
        buttonPanel.add(themeButton, gbc);

        // Add the button panel to the main frame
        dashboardFrame.add(buttonPanel, gbc);

        // Dynamic resizing of fonts
        dashboardFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                int newFontSize = Math.max(20, dashboardFrame.getWidth() / 35);
                connectButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
                configButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
                aboutButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
                themeButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
            }
        });

        dashboardFrame.pack();
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setVisible(true);
    }


    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK);
            }
        });

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

        try (BufferedReader reader = new BufferedReader(new FileReader("Ip and port.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                configTextArea.append(line + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(configFrame, "Error loading config file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        saveButton.addActionListener(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Ip and port.txt"))) {
                writer.write(configTextArea.getText());
                JOptionPane.showMessageDialog(configFrame, "Config saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                configFrame.dispose();
                dashboardFrame.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(configFrame, "Error saving config file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        configFrame.setLocationRelativeTo(null);
        configFrame.setVisible(true);
    }

    private void openAboutDialog(JFrame dashboardFrame) {
        JDialog aboutDialog = new JDialog(dashboardFrame, "About", true);
        aboutDialog.setSize(300, 200);
        aboutDialog.setLayout(new FlowLayout());

        JButton developersButton = new JButton("Developers Team");
        JButton faqsButton = new JButton("FAQs");
        JButton visionButton = new JButton("Vision");

        developersButton.addActionListener(e -> showDeveloperInfo());
        faqsButton.addActionListener(e -> showFaqsInfo());
        visionButton.addActionListener(e -> showVisionInfo());

        aboutDialog.add(developersButton);
        aboutDialog.add(faqsButton);
        aboutDialog.add(visionButton);

        aboutDialog.setLocationRelativeTo(dashboardFrame);
        aboutDialog.setVisible(true);
    }
    private void openP2PDialog(JFrame dashboardFrame, ThemeManagerDashboard themeManager) {
        // Create P2P dialog
        JDialog p2pDialog = new JDialog(dashboardFrame, "P2P Chat", true);
        p2pDialog.setSize(400, 200);
        p2pDialog.setLayout(new BorderLayout());

        // Create a label to display the P2P message
        JLabel messageLabel = new JLabel("Welcome to P2P Option", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Apply the theme to the P2P dialog
        themeManager.updateTheme(p2pDialog); // Reuse the updateTheme method to apply the selected theme to the dialog
        messageLabel.setForeground(UIManager.getColor("Button.foreground")); // Apply the correct text color based on the theme

        // Add the label to the dialog
        p2pDialog.add(messageLabel, BorderLayout.CENTER);

        // Show the dialog
        p2pDialog.setLocationRelativeTo(dashboardFrame);  // Center it relative to the main frame
        p2pDialog.setVisible(true);
    }



    private void showDeveloperInfo() {
        JOptionPane.showMessageDialog(null, "Developed by the Messaging App Team.");
    }

    private void showFaqsInfo() {
        JOptionPane.showMessageDialog(null, "FAQs: 1. How to use? 2. Troubleshooting...");
    }

    private void showVisionInfo() {
        JOptionPane.showMessageDialog(null, "Our vision is to create an intuitive messaging platform.");
    }
}



