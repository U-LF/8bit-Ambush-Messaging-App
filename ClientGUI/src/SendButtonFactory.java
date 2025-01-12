// File: SendButtonFactory.java
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SendButtonFactory {
    public static JButton createSendButton() {
        try {
            URL sendButtonUrl = new URL("https://cdn-icons-png.freepik.com/256/4458/4458496.png?semt=ais_hybrid");
            ImageIcon sendIcon = new ImageIcon(sendButtonUrl);
            Image img = sendIcon.getImage();
            Image resizedImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            sendIcon = new ImageIcon(resizedImg);

            JButton sendButton = new JButton(sendIcon);
            sendButton.setPreferredSize(new Dimension(40, 40));
            sendButton.setContentAreaFilled(false);
            sendButton.setFocusPainted(false);
            return sendButton;
        } catch (Exception e) {
            System.err.println("Error loading send button icon: " + e.getMessage());
            return new JButton("Send");
        }
    }
}
