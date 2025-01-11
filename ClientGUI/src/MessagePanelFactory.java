import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MessagePanelFactory {

    public MessagePanelFactory(ThemeManagerDashboard themeManager) {
    }

    public static JPanel createMessagePanel(JTextArea messageArea) {
        JPanel panel = new JPanel(new BorderLayout()) {
            private Image backgroundImage;

            {
                try {
                    URL imageUrl = new URL("https://www.shutterstock.com/image-vector/social-media-sketch-vector-seamless-600nw-1660950727.jpg");
                    backgroundImage = new ImageIcon(imageUrl).getImage();
                } catch (Exception e) {
                    System.err.println("Error loading background image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
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
}
