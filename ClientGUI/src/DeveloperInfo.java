package resources;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DeveloperInfo {

    public static void showDeveloperInfo() {
        JDialog developerDialog = new JDialog((JFrame) null, "Developer Profiles", true);
        developerDialog.setMinimumSize(new Dimension(850, 600)); // Set dialog size
        developerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        developerDialog.setLayout(new BorderLayout());

        // Main panel for all content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setPreferredSize(new Dimension(820, 900)); // Adjusted preferred height to fit cards

        // Gradient background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 144, 255), 0, getHeight(), new Color(72, 61, 139));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, 820, 900);

        // Developer profiles
        addDeveloperCard(backgroundPanel, "akram.png", "Name: Muhammad Abdullah Akram\nStudent Id: F2022065066\nRole: Project Manager\nSub: Backend Development for client side\nSemester: 5th\nDegree: Software Engineering", 50, 50);
        addDeveloperCard(backgroundPanel, "abdullah.png", "Name: Muhammad Abdullah Raja\nStudent Id: F2022065066\nRole: Responsible for Front-End\nSub: Developed Front-End and event listeners\nSemester: 5th\nDegree: Software Engineering", 50, 220);
        addDeveloperCard(backgroundPanel, "khuzaima.png", "Name: Muhammad Khuzaima Iqbal\nStudent Id: F2022065075\nRole: Responsible for Front-End\nSub: Developed Front-End\nSemester: 5th\nDegree: Software Engineering", 50, 390);
        addDeveloperCard(backgroundPanel, "ahmed.png", "Name: Ahmed Khan\nStudent Id: F2022065075\nRole: Database Administrator\nSub: Developed Database Server and Authentication Server\nSemester: 5th\nDegree: Software Engineering", 50, 560);
        addDeveloperCard(backgroundPanel, "gulshan.png", "Name: Gulshan Aslam\nStudent Id: F2022065209\nRole: Requirement Analyst\nSub: Documentation\nSemester: 5th\nDegree: Software Engineering", 50, 730);

        // Add background panel to content panel
        contentPanel.add(backgroundPanel);

        // Copyright notice (in a separate panel)
        JPanel copyrightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 144, 255), 0, getHeight(), new Color(72, 61, 139));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        copyrightPanel.setLayout(new BorderLayout());
        copyrightPanel.setBackground(new Color(72, 61, 139)); // Dark background for copyright
        copyrightPanel.setBorder(null); // Remove border

        JLabel copyrightLabel = new JLabel("© 2025 All Rights Reserved", SwingConstants.CENTER);
        copyrightLabel.setForeground(Color.WHITE);
        copyrightLabel.setFont(new Font("Arial", Font.BOLD, 14));
        copyrightPanel.add(copyrightLabel, BorderLayout.CENTER);

        // Add content panel to a scrollable JScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Increase scroll speed

        // Customize scrollbar to make it slimmer
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(128, 128, 128); // Dark gray
                trackColor = new Color(220, 220, 220); // Light gray
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Slim scrollbar width

        // Add scroll pane to the dialog
        developerDialog.add(scrollPane, BorderLayout.CENTER);

        // Add copyright panel at the bottom
        developerDialog.add(copyrightPanel, BorderLayout.SOUTH);

        // Add Backspace key listener
        developerDialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    developerDialog.dispose(); // Close the dialog when Backspace is pressed
                }
            }
        });

        // Make dialog focusable for key events
        developerDialog.setFocusable(true);
        developerDialog.setFocusableWindowState(true);

        // Show dialog
        developerDialog.setLocationRelativeTo(null);
        developerDialog.setVisible(true);
    }

    private static void addDeveloperCard(JPanel parentPanel, String imageName, String info, int x, int y) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(x, y, 720, 160); // Increased card height for more content space
        cardPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(192, 192, 192), 2));

        // Profile picture
        JLabel pictureLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(DeveloperInfo.class.getResource("/icons/" + imageName));
            Image scaledImage = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Larger image size
            pictureLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            pictureLabel.setText("Image not found");
            pictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        pictureLabel.setBounds(10, 20, 120, 120); // Adjusted for larger images

        // Developer information
        JTextArea infoArea = new JTextArea(info);
        infoArea.setBounds(140, 20, 560, 120); // Adjusted to fit all text
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoArea.setBackground(new Color(255, 255, 255, 0)); // Transparent background
        infoArea.setForeground(Color.DARK_GRAY);

        // Add components to card panel
        cardPanel.add(pictureLabel);
        cardPanel.add(infoArea);

        // Add card panel to parent panel
        parentPanel.add(cardPanel);
    }
}
