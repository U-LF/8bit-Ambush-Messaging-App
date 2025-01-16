import javax.swing.*;

public class MessageAppender {
    private final JTextArea messageArea;

    public MessageAppender(JTextArea messageArea) {
        this.messageArea = messageArea;
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }
}

