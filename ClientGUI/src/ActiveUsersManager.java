import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActiveUsersManager {

    // Method to create the Active Users button
    public JButton createActiveUsersButton() {
        try {
            URL activeUsersUrl = new URL("https://www.voxco.com/wp-content/uploads/2022/03/DAILY-ACTIVE-USERS1.png");
            ImageIcon activeUsersIcon = new ImageIcon(activeUsersUrl);
            Image img = activeUsersIcon.getImage();
            int newSize = 60;
            Image resizedImg = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
            activeUsersIcon = new ImageIcon(resizedImg);

            JButton activeUsersButton = new JButton(activeUsersIcon);
            activeUsersButton.setBorderPainted(false);
            activeUsersButton.setBorder(null);
            activeUsersButton.setContentAreaFilled(false);
            activeUsersButton.setFocusPainted(false);
            activeUsersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showActiveUsersDialog();
                }
            });
            return activeUsersButton;
        } catch (Exception e) {
            System.err.println("Error loading active users icon: " + e.getMessage());
            return new JButton("Active Users");
        }
    }

    // Method to show Active Users dialog
    public void showActiveUsersDialog() {
        // Creating a list of active users
        String[] activeUsers = {"Hafiz Sohaib", "Abdullah", "Khuzaima", "BugFixer"};

        // Displaying the active users in a dialog
        StringBuilder userList = new StringBuilder("Active Users:\n");
        for (String user : activeUsers) {
            userList.append(user).append("\n");
        }

        JOptionPane.showMessageDialog(null, userList.toString(), "Active Users List", JOptionPane.INFORMATION_MESSAGE);
    }
}
