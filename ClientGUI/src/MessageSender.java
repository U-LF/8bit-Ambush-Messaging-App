import javax.swing.*;
import java.io.*;
import java.awt.*;

public class MessageSender {
    public static void sendMessage(DataOutputStream outToServer, JTextField inputField, JTextArea messageArea, String username) {
        try {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                outToServer.writeBytes(username + ": " + inputField.getText() + "\n");
                outToServer.flush();
                appendMessage(messageArea, username + ": " + message);  // Append message in messageArea
                inputField.setText("");  // Clear input field after sending
            }
        } catch (IOException e) {
            appendMessage(messageArea, "Error sending message: " + e.getMessage());  // Append error if any
        }
    }

    private static void appendMessage(JTextArea messageArea, String message) {
        SwingUtilities.invokeLater(() -> {
            // Set the font for the message area if needed
            messageArea.setFont(new Font("Calibri (Body)", Font.BOLD, 14));  // 16px size and Muli Bold
            messageArea.setForeground(Color.WHITE);  // Set the text color to white
            messageArea.append(message + "\n");  // Append message
        });
    }
}
