// File: SettingsManager.java
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SettingsManager {

    // Method to create the settings button
    public JButton createSettingsButton(ClientGUIFrame clientGUIFrame) {
        try {
            URL settingsUrl = new URL("https://cdn-icons-png.flaticon.com/512/3524/3524659.png");
            ImageIcon settingsIcon = new ImageIcon(settingsUrl);
            Image img = settingsIcon.getImage();
            int newSize = 50;
            Image resizedImg = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
            settingsIcon = new ImageIcon(resizedImg);

            JButton settingsButton = new JButton(settingsIcon);
            settingsButton.setPreferredSize(new Dimension(newSize, newSize));
            settingsButton.setContentAreaFilled(false);
            settingsButton.setFocusPainted(false);
            settingsButton.setBorderPainted(false);
            settingsButton.addActionListener(e -> openSettingsDialog(clientGUIFrame));
            return settingsButton;
        } catch (Exception e) {
            e.printStackTrace();
            return new JButton("Settings");
        }
    }

    // Method to open the settings dialog
    private void openSettingsDialog(ClientGUIFrame clientGUIFrame) {
        JDialog settingsDialog = new JDialog((Frame) null, "Settings", true);
        settingsDialog.setSize(300, 200);
        settingsDialog.setLayout(new GridLayout(3, 1));

        JButton usernameButton = new JButton("Change Username");
        usernameButton.addActionListener(e -> clientGUIFrame.changeUsername(settingsDialog));
        settingsDialog.add(usernameButton);

        JButton themeButton = new JButton("Switch Theme");
        themeButton.addActionListener(e -> {
            // Call the switchTheme method from ThemeManager
            clientGUIFrame.getThemeManager().switchTheme(settingsDialog,
                    (JFrame) SwingUtilities.getRoot(clientGUIFrame.getMessageArea()),
                    clientGUIFrame.getMessageArea(), clientGUIFrame.getInputField());
        });
        settingsDialog.add(themeButton);

        settingsDialog.setLocationRelativeTo(null);
        settingsDialog.setVisible(true);
    }
}
