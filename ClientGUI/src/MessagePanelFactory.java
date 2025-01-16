/*import javax.swing.*;
import java.awt.*;
import resources.Icons;

public class MessagePanelFactory {

    public MessagePanelFactory(ThemeManagerDashboard themeManager) {
    }

    public static JPanel createMessagePanel(JTextArea messageArea) {
        JPanel panel = new JPanel(new BorderLayout()) {
            private Image backgroundImage;

            {
                try {
                    // Load the image from the icons folder using your Icons class
                    ImageIcon backgroundIcon = Icons.getIcon("chatArea_Background.png", 700, 600);  // Use the image name and resize as needed
                    if (backgroundIcon != null) {
                        backgroundImage = backgroundIcon.getImage();
                    } else {
                        System.err.println("Error loading background image: chatArea_Background.png");
                    }
                } catch (Exception e) {
                    System.err.println("Error loading background image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Draw the image scaled to fit the panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}*/

import javax.swing.*;
import java.awt.*;
import resources.Icons;  // Make sure to import your Icons class

public class MessagePanelFactory {

    public MessagePanelFactory(ThemeManagerDashboard themeManager) {
    }

    public static JPanel createMessagePanel(JTextArea messageArea) {
        JPanel panel = new JPanel(new BorderLayout()) {
            private Image backgroundImage;

            {
                try {
                    // Load the image from the icons folder using your Icons class
                    ImageIcon backgroundIcon = Icons.getIcon("chatArea_Background.png", 700, 600);  // Use the image name and resize as needed
                    if (backgroundIcon != null) {
                        backgroundImage = backgroundIcon.getImage();
                    } else {
                        System.err.println("Error loading background image: chatArea_Background.png");
                    }
                } catch (Exception e) {
                    System.err.println("Error loading background image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Draw the image scaled to fit the panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Set the background color of the panel to black
        panel.setBackground(Color.BLACK);

        // Set the JTextArea properties
        messageArea.setEditable(false);
        messageArea.setOpaque(true);  // Make the text area opaque
        messageArea.setBackground(Color.BLACK);  // Set background color to black
        messageArea.setForeground(Color.WHITE);  // Set text color to white

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}

