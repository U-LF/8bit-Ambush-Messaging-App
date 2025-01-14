package resources;

import resources.Icons;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class FAQsInfo {

    public static void showFaqsInfo(JFrame parentFrame) {
        // Create the FAQ content
        String faqs = "<html><body>"
                + "<h2><b>1. How to use the chat application?</b></h2>"
                + "<p>To start using the chat app, simply launch it and enter a username. You can either start a private P2P chat or join a chatroom.</p>"
                + "<h2><b>2. How to send messages in a chatroom?</b></h2>"
                + "<p>Once you're in a chatroom, type your message in the text box at the bottom and press Enter or click the Send button.</p>"
                + "<h2><b>3. How to start a private conversation?</b></h2>"
                + "<p>Click on a contact's name from your list and start typing your message to begin a P2P chat.</p>"
                + "<h2><b>4. Can I change my username?</b></h2>"
                + "<p>Yes, you can change your username from the settings menu in the app.</p>"
                + "<h2><b>5. How do I join a chatroom?</b></h2>"
                + "<p>Click on the ‘Join Room’ button and select a chatroom from the list of available rooms.</p>"
                + "<h2><b>6. How can I leave a chatroom?</b></h2>"
                + "<p>To leave a chatroom, click the ‘Leave Room’ button on the top of the chat window.</p>"
                + "<h2><b>7. Is there a file sharing feature?</b></h2>"
                + "<p>Yes, you can send files in both P2P and chatroom conversations by clicking the attachment icon.</p>"
                + "<h2><b>8. How do I troubleshoot connectivity issues?</b></h2>"
                + "<p>Check your internet connection and ensure the server is running. If issues persist, restart the app or contact support.</p>"
                + "</body></html>";

        // Create the FAQ dialog
        JDialog faqDialog = new JDialog(parentFrame, "FAQs", true);
        faqDialog.setSize(600, 500);
        faqDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        faqDialog.setLocationRelativeTo(parentFrame);

        // Create a custom JPanel with a background image
        JPanel faqPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = Icons.getIcon("faqbackground.png", getWidth(), getHeight());
                if (backgroundIcon != null) {
                    g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        faqPanel.setLayout(new BorderLayout());

        // Create a JTextPane for displaying FAQs
        JTextPane faqTextPane = new JTextPane();
        faqTextPane.setContentType("text/html");
        faqTextPane.setEditable(false);
        faqTextPane.setText(faqs);
        faqTextPane.setOpaque(false); // Transparent background
        faqTextPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create a JScrollPane and ensure correct scrolling direction
        JScrollPane scrollPane = new JScrollPane(faqTextPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(580, 400));
        scrollPane.getViewport().setOpaque(false); // Make viewport transparent
        scrollPane.setOpaque(false);

        // Customize scrollbar appearance
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(236, 236, 241);
                this.trackColor = new Color(220, 220, 220);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }

            private JButton createInvisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        // Add the scroll pane to the panel
        faqPanel.add(scrollPane, BorderLayout.CENTER);

        // Create a back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(234, 234, 239));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> faqDialog.dispose()); // Close dialog on click

        // Add the back button to the panel
        faqPanel.add(backButton, BorderLayout.SOUTH);

        // Add the panel to the dialog
        faqDialog.add(faqPanel);
        faqDialog.setVisible(true);
    }
}


/*package resources;

import resources.Icons;

import javax.swing.*;
        import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class FAQsInfo {

    public static void showFaqsInfo(JFrame parentFrame) {
        // Create the FAQ content
        String faqs = "<html><body>"
                + "<h2><b>1. How to use the chat application?</b></h2>"
                + "<p>To start using the chat app, simply launch it and enter a username. You can either start a private P2P chat or join a chatroom.</p>"
                + "<h2><b>2. How to send messages in a chatroom?</b></h2>"
                + "<p>Once you're in a chatroom, type your message in the text box at the bottom and press Enter or click the Send button.</p>"
                + "<h2><b>3. How to start a private conversation?</b></h2>"
                + "<p>Click on a contact's name from your list and start typing your message to begin a P2P chat.</p>"
                + "<h2><b>4. Can I change my username?</b></h2>"
                + "<p>Yes, you can change your username from the settings menu in the app.</p>"
                + "<h2><b>5. How do I join a chatroom?</b></h2>"
                + "<p>Click on the ‘Join Room’ button and select a chatroom from the list of available rooms.</p>"
                + "<h2><b>6. How can I leave a chatroom?</b></h2>"
                + "<p>To leave a chatroom, click the ‘Leave Room’ button on the top of the chat window.</p>"
                + "<h2><b>7. Is there a file sharing feature?</b></h2>"
                + "<p>Yes, you can send files in both P2P and chatroom conversations by clicking the attachment icon.</p>"
                + "<h2><b>8. How do I troubleshoot connectivity issues?</b></h2>"
                + "<p>Check your internet connection and ensure the server is running. If issues persist, restart the app or contact support.</p>"
                + "</body></html>";

        // Create the FAQ dialog
        JDialog faqDialog = new JDialog(parentFrame, "FAQs", true);
        faqDialog.setSize(600, 500);
        faqDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        faqDialog.setLocationRelativeTo(parentFrame);

        // Create a custom JPanel with gradient background
        JPanel faqPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, Color.CYAN, getWidth(), getHeight(), Color.BLUE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        faqPanel.setLayout(new BorderLayout());

        // Create a JTextPane for displaying FAQs
        JTextPane faqTextPane = new JTextPane();
        faqTextPane.setContentType("text/html");
        faqTextPane.setEditable(false);
        faqTextPane.setText(faqs);
        faqTextPane.setOpaque(false); // Transparent background
        faqTextPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create a custom JScrollPane
        JScrollPane scrollPane = new JScrollPane(faqTextPane);
        scrollPane.setPreferredSize(new Dimension(580, 400));
        scrollPane.getViewport().setOpaque(false); // Make viewport transparent
        scrollPane.setOpaque(false);

        // Customize scroll bar appearance
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 100, 255);
                this.trackColor = new Color(220, 220, 220);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }

            private JButton createInvisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        // Add the scroll pane to the panel
        faqPanel.add(scrollPane, BorderLayout.CENTER);

        // Create a back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(200, 200, 255));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> faqDialog.dispose()); // Close dialog on click

        // Add the back button to the panel
        faqPanel.add(backButton, BorderLayout.SOUTH);

        // Add the panel to the dialog
        faqDialog.add(faqPanel);
        faqDialog.setVisible(true);
    }
}*/
