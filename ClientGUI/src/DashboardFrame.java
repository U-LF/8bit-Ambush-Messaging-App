import javax.swing.*;
import java.awt.*;
import java.io.*;

public class DashboardFrame {
    public void showDashboard() {
        // Create the main dashboard frame
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setSize(400, 200);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel for the buttons with GridBagLayout to center them
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Center the buttons both horizontally and vertically
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding around the buttons

        // Create the "Connect to Server" button
        JButton connectButton = new JButton("Connect to Server");
        connectButton.addActionListener(e -> {
            // Close the dashboard and connect to the server
            dashboardFrame.dispose();
            ClientGUI.connectToServer();
        });

        // Create the "Config" button
        JButton configButton = new JButton("Config");
        configButton.addActionListener(e -> {
            // Close the dashboard and open the config editor
            dashboardFrame.dispose();
            openConfigEditor(dashboardFrame);  // Pass the dashboard frame to return later
        });

        // Add buttons to the panel with GridBagLayout
        buttonPanel.add(connectButton, gbc);
        gbc.gridy++; // Move to the next row for the second button
        buttonPanel.add(configButton, gbc);

        // Add the button panel to the frame
        dashboardFrame.add(buttonPanel, BorderLayout.CENTER);

        // Show the dashboard frame
        dashboardFrame.setLocationRelativeTo(null);  // Center the window on the screen
        dashboardFrame.setVisible(true);
    }

    // Method to open the config editor and return to dashboard after editing
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

        // Load the current content of the config file
        try (BufferedReader reader = new BufferedReader(new FileReader("Ip and port.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                configTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(configFrame, "Error loading config file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Save the edited content back to the file
        saveButton.addActionListener(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Ip and port.txt"))) {
                writer.write(configTextArea.getText());
                JOptionPane.showMessageDialog(configFrame, "Config saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(configFrame, "Error saving config file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the config frame and return to the dashboard
            configFrame.dispose();
            dashboardFrame.setVisible(true);  // Show the dashboard frame again
        });

        configFrame.setLocationRelativeTo(null);
        configFrame.setVisible(true);
    }
}