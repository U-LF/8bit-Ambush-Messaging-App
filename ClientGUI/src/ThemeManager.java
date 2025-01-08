import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private boolean isDarkMode;

    public ThemeManager(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
    }

    public void switchTheme(JDialog settingsDialog, JFrame root, JTextArea messageArea, JTextField inputField) {
        String[] options = {"Light Mode", "Dark Mode"};
        int choice = JOptionPane.showOptionDialog(settingsDialog, "Choose Theme", "Switch Theme",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            isDarkMode = false;
            updateTheme((JFrame) SwingUtilities.getRoot(messageArea), messageArea, inputField);
        } else {
            isDarkMode = true;
            updateTheme((JFrame) SwingUtilities.getRoot(messageArea), messageArea, inputField);
        }
    }

    public void updateTheme(JFrame frame, JTextArea messageArea, JTextField inputField) {
        if(isDarkMode) {
            UIManager.put("Panel.background", Color.DARK_GRAY);
            messageArea.setBackground(Color.BLACK);
            inputField.setBackground(Color.BLACK);
            inputField.setForeground(Color.WHITE);
        } else {
            UIManager.put("Panel.background", Color.WHITE);
            messageArea.setBackground(Color.WHITE);
            inputField.setBackground(Color.WHITE);
            inputField.setForeground(Color.BLACK);
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }
}
