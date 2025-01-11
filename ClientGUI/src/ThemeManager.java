import javax.swing.*;
import java.awt.*;

class ThemeManagerDashboard {
    private String currentTheme = "Light"; // Default theme is Light

    public void switchTheme(JFrame frame) {
        JDialog themeDialog = new JDialog(frame, "Select Theme", true);
        themeDialog.setSize(300, 200);
        themeDialog.setLayout(new FlowLayout());
        themeDialog.setLocationRelativeTo(frame);

        JRadioButton lightTheme = new JRadioButton("Light");
        JRadioButton darkTheme = new JRadioButton("Dark");
        JRadioButton greenTheme = new JRadioButton("Green");
        JRadioButton pinkTheme = new JRadioButton("Pink");

        ButtonGroup themeGroup = new ButtonGroup();
        themeGroup.add(lightTheme);
        themeGroup.add(darkTheme);
        themeGroup.add(greenTheme);
        themeGroup.add(pinkTheme);

        switch (currentTheme) {
            case "Dark" -> darkTheme.setSelected(true);
            case "Green" -> greenTheme.setSelected(true);
            case "Pink" -> pinkTheme.setSelected(true);
            default -> lightTheme.setSelected(true);
        }

        JButton okButton = new JButton("Apply");
        okButton.addActionListener(e -> {
            if (lightTheme.isSelected()) currentTheme = "Light";
            else if (darkTheme.isSelected()) currentTheme = "Dark";
            else if (greenTheme.isSelected()) currentTheme = "Green";
            else if (pinkTheme.isSelected()) currentTheme = "Pink";

            updateTheme(frame);
            themeDialog.dispose();
        });

        themeDialog.add(lightTheme);
        themeDialog.add(darkTheme);
        themeDialog.add(greenTheme);
        themeDialog.add(pinkTheme);
        themeDialog.add(okButton);

        themeDialog.setVisible(true);
    }

    public void updateTheme(JFrame frame) {
        Color backgroundColor;
        Color foregroundColor;

        // Set colors based on the selected theme
        switch (currentTheme) {
            case "Dark" -> {
                backgroundColor = Color.DARK_GRAY;
                foregroundColor = Color.WHITE;  // White text for Dark theme
            }
            case "Green" -> {
                backgroundColor = new Color(0, 195, 0); // Dark green
                foregroundColor = Color.WHITE;  // White text for Green theme
            }
            case "Pink" -> {
                backgroundColor = new Color(255, 182, 193); // Light pink
                foregroundColor = Color.WHITE;  // White text for Pink theme
            }
            default -> { // Light theme
                backgroundColor = Color.WHITE;
                foregroundColor = Color.BLACK;  // Black text for Light theme
            }
        }

        // Update the UI manager settings for the selected theme
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Button.background", backgroundColor);
        UIManager.put("Button.foreground", foregroundColor);
        UIManager.put("TextArea.background", backgroundColor);
        UIManager.put("TextArea.foreground", foregroundColor);
        UIManager.put("TextField.background", backgroundColor);
        UIManager.put("TextField.foreground", foregroundColor);

        // Apply the updated theme to all components
        SwingUtilities.updateComponentTreeUI(frame);
    }
    public void updateButtonStyle(JButton button) {
        button.setBackground(UIManager.getColor("Button.background"));
        button.setForeground(UIManager.getColor("Button.foreground"));
    }

}