// File: MessageSender.java
import javax.swing.*;
import java.io.*;

public class MessageSender {
    public static void sendMessage(DataOutputStream outToServer, JTextField inputField, JTextArea messageArea, String username) {
        try {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                outToServer.writeBytes(message + "\n");
                outToServer.flush();
                appendMessage(messageArea, username + ": " + message);
                inputField.setText("");
            }
        } catch (IOException e) {
            appendMessage(messageArea, "Error sending message: " + e.getMessage());
        }
    }

    private static void appendMessage(JTextArea messageArea, String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }
}
