import javax.swing.*;
import java.awt.*;

public class MessageAppender {
    private final JTextArea messageArea;

    public MessageAppender(JTextArea messageArea) {
        this.messageArea = messageArea;
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                messageArea.append(message + "\n");
                // Automatically scroll to the bottom
                messageArea.setCaretPosition(messageArea.getDocument().getLength());
            } catch (Exception e) {
                System.err.println("Error appending message: " + e.getMessage());
            }
        });
    }

    public void appendMessage(String message, Color color, Font font) {
        SwingUtilities.invokeLater(() -> {
            try {
                messageArea.setForeground(color);
                messageArea.setFont(font);
                messageArea.append(message + "\n");
                messageArea.setCaretPosition(messageArea.getDocument().getLength());
            } catch (Exception e) {
                System.err.println("Error appending message with style: " + e.getMessage());
            }
        });
    }
}
