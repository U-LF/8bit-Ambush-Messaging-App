package resources; // Make sure this matches your folder structure (if it's in resources package)

import javax.swing.*;
import java.awt.*;

public class VisionInfo {
    public static void showVisionInfo() {
        // Create a custom JPanel with a gradient background
        JPanel visionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create a gradient color (from blue to light gray)
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, Color.BLUE, 0, getHeight(), Color.LIGHT_GRAY);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        visionPanel.setLayout(new BoxLayout(visionPanel, BoxLayout.Y_AXIS));

        // Text for the vision message
        String visionMessage = "<html><h2>Our Vision</h2>"
                + "<p><strong>Our vision is to create a next-generation messaging platform "
                + "that empowers users to communicate seamlessly and securely. Our app supports:</strong></p>"
                + "<ul>"
                + "<li><strong>P2P (Peer-to-Peer) Communication:</strong> Ensuring privacy and fast communication between users without intermediaries.</li>"
                + "<li><strong>Chatrooms:</strong> Create group chatrooms for dynamic, organized conversations with multiple participants.</li>"
                + "<li><strong>Customization:</strong> Customize your experience with personalized themes, profiles, and notification settings.</li>"
                + "</ul>"
                + "<p>This platform is designed to provide a secure, reliable, and user-friendly experience for both personal and group communication needs.</p>"
                + "</html>";

        // Create a JTextArea for displaying the vision with the customized message
        JTextArea visionTextArea = new JTextArea(visionMessage);
        visionTextArea.setEditable(false);
        visionTextArea.setBackground(new Color(240, 240, 240, 180)); // Transparent background for text area
        visionTextArea.setFont(new Font("Times New Roman", Font.PLAIN, 16)); // Times New Roman, 16px
        visionTextArea.setLineWrap(true);
        visionTextArea.setWrapStyleWord(true);
        visionTextArea.setOpaque(false); // To ensure the gradient background shows through

        JScrollPane scrollPane = new JScrollPane(visionTextArea);
        scrollPane.setPreferredSize(new Dimension(580, 400)); // ScrollPane size

        // Add components to the panel
        visionPanel.add(scrollPane);

        // Create a JDialog for displaying the vision
        JDialog visionDialog = new JDialog();
        visionDialog.setTitle("Vision");
        visionDialog.setSize(600, 500); // Set dialog size
        visionDialog.setLocationRelativeTo(null); // Center the dialog
        visionDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close the dialog when done
        visionDialog.add(visionPanel);
        visionDialog.setVisible(true); // Make the dialog visible
    }
}
