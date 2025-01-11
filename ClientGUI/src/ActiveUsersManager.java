import javax.swing.*;
import java.awt.*;
import java.net.URL;
//import ThemeManagerDashboard;


public class ActiveUsersManager {

    private ThemeManagerDashboard themeManager;

    public ActiveUsersManager(ThemeManagerDashboard themeManager) {
        this.themeManager = themeManager;
    }

    public JButton createActiveUsersButton(JFrame frame) {
        try {
            URL activeUsersUrl = new URL("https://www.voxco.com/wp-content/uploads/2022/03/DAILY-ACTIVE-USERS1.png");
            ImageIcon activeUsersIcon = new ImageIcon(activeUsersUrl);
            Image img = activeUsersIcon.getImage();
            int newSize = 60;
            Image resizedImg = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
            activeUsersIcon = new ImageIcon(resizedImg);

            JButton activeUsersButton = new JButton(activeUsersIcon);
            styleButton(activeUsersButton);
            activeUsersButton.addActionListener(event -> showActiveUsersDialog(frame)); // Use 'event' here
            return activeUsersButton;
        } catch (Exception exception) { // Renamed to 'exception'
            System.err.println("Error loading active users icon: " + exception.getMessage());
            JButton activeUsersButton = new JButton("Active Users");
            styleButton(activeUsersButton);
            activeUsersButton.addActionListener(event -> showActiveUsersDialog(frame)); // Use 'event' here
            return activeUsersButton;
        }
    }

    public void showActiveUsersDialog(JFrame frame) {
        String[] activeUsers = {"Hafiz Sohaib", "Abdullah", "Khuzaima", "BugFixer"};
        StringBuilder userList = new StringBuilder("Active Users:\n");
        for (String user : activeUsers) {
            userList.append(user).append("\n");
        }

        JOptionPane.showMessageDialog(frame, userList.toString(), "Active Users List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void styleButton(JButton button) {
        button.setBorderPainted(false);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        themeManager.updateButtonStyle(button);
    }
}
