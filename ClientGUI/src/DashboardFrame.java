import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class DashboardFrame {
    public void showDashboard() {
        // Create the main dashboard frame
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setMinimumSize(new Dimension(500, 400));
        dashboardFrame.setPreferredSize(new Dimension(700, 500));

        // Set the background color to dark blue-grey
        dashboardFrame.getContentPane().setBackground(new Color(173, 216, 230));  // Light blue-grey color

        // Set layout for the frame to BorderLayout
        dashboardFrame.setLayout(new BorderLayout());

        // Initialize ThemeManager
        ThemeManagerDashboard themeManager = new ThemeManagerDashboard();
        themeManager.updateTheme(dashboardFrame); // Apply the initial theme

        // Create buttons with icons and listeners
        JButton connectButton = createStyledCardButton("Connect to ChatRoom",
                "https://w7.pngwing.com/pngs/273/74/png-transparent-online-chat-livechat-chat-room-baycreative-inc-google-icon-blue-text-logo-thumbnail.png", e -> {
                    dashboardFrame.dispose();
                    ClientConnection.connectToServer();
                });

        JButton configButton = createStyledCardButton("Config",
                "https://cdn-icons-png.flaticon.com/512/8718/8718462.png", e -> {
                    dashboardFrame.dispose();
                    openConfigEditor(dashboardFrame);
                });

        JButton aboutButton = createStyledCardButton("About",
                "https://w7.pngwing.com/pngs/257/702/png-transparent-about-us-logo-business-logo-company-brand-service-icon-about-us-hd-miscellaneous-blue-text-thumbnail.png", e -> {
                    openAboutDialog(dashboardFrame);
                });

        JButton p2pButton = createStyledCardButton("P2P",
                "https://png.pngtree.com/png-vector/20220724/ourmid/pngtree-peer-to-peer-icon-p2p-account-switcher-icon-vector-png-image_38118238.png", e -> {
                    openP2PDialog(dashboardFrame, themeManager);  // Open the P2P dialog with theme applied
                });

        // Panel to hold all buttons in a grid format (adjusted for the theme button to span two columns)
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15)); // 3 rows, 2 columns with gaps
        buttonPanel.setOpaque(false);

        // Add buttons to the panel
        buttonPanel.add(p2pButton);
        buttonPanel.add(connectButton);
        buttonPanel.add(configButton);
        buttonPanel.add(aboutButton);


        // Create a separate panel to center the Theme button across the entire row
        JPanel themePanel = new JPanel();
        themePanel.setLayout(new BorderLayout());
        themePanel.setOpaque(false);

        JButton themeButton = createStyledCardButton("Theme",
                "https://play-lh.googleusercontent.com/lJ9E8YGdRgjbpLXzhv7fiwJ6fDDlLVRN0mIlJadhYFeOG1Br6HljeYTG8Qc5mIM-UNjc=w240-h480-rw", e -> {
                    themeManager.switchTheme(dashboardFrame);
                });

        // Add the Theme button to the new themePanel and then add the panel to the buttonPanel
        themePanel.add(themeButton, BorderLayout.CENTER);
        buttonPanel.add(themePanel);

        // Create a top panel with the app name
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(128, 0, 128));  // Purple color for the background
        topPanel.setPreferredSize(new Dimension(topPanel.getPreferredSize().width, 100)); // Increase the height to 100px (adjust as needed)

// Set GridBagLayout to center the label with equal space on all sides
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;  // Center in the grid (single column)
        gbc.gridy = 0;  // Single row
        gbc.insets = new Insets(20, 20, 20, 20);  // Adds equal padding on all sides (top, left, bottom, right)
        gbc.anchor = GridBagConstraints.CENTER;  // Ensure the label is centered within its grid cell

// Create and configure the app name label
        JLabel appNameLabel = new JLabel("811 Msg App");
        appNameLabel.setFont(new Font("Arial", Font.BOLD, 35));
        appNameLabel.setForeground(Color.WHITE);

// Add the label to the top panel using GridBagConstraints
        topPanel.add(appNameLabel, gbc);

// Add top panel and button panel to the frame
        dashboardFrame.add(topPanel, BorderLayout.NORTH);
        dashboardFrame.add(buttonPanel, BorderLayout.CENTER);

        // Dynamic resizing of fonts
        dashboardFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                int newFontSize = Math.max(20, dashboardFrame.getWidth() / 35);
                connectButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
                configButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
                aboutButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
                themeButton.setFont(new Font("Arial", Font.BOLD, newFontSize));
            }
        });

        dashboardFrame.pack();
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setVisible(true);
    }

    private JButton createStyledCardButton(String text, String iconUrl, ActionListener actionListener) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 20));

        // Load icon from URL
        ImageIcon icon = new ImageIcon(getImageFromURL(iconUrl));
        button.setIcon(resizeIcon(icon));  // Resize the icon to fit the button

        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 70, 70));
                button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(40, 40, 40));
                button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            }
        });

        button.addActionListener(actionListener);
        return button;
    }

    private ImageIcon resizeIcon(ImageIcon icon) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);  // Resize to 80x80 pixels
        return new ImageIcon(resizedImg);
    }

    private Image getImageFromURL(String iconUrl) {
        try {
            URL url = new URL(iconUrl);
            return new ImageIcon(url).getImage();
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL: " + iconUrl);
            return null;
        }
    }

    private void openConfigEditor(JFrame dashboardFrame) {
        JFrame configFrame = new JFrame("Edit Config");
        configFrame.setSize(400, 300);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setLayout(new BorderLayout());

        JTextArea configTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(configTextArea);
        configFrame.add(scrollPane, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        configFrame.add(saveButton, BorderLayout.SOUTH);

        try (BufferedReader reader = new BufferedReader(new FileReader("Ip and port.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                configTextArea.append(line + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(configFrame, "Error loading config file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        saveButton.addActionListener(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Ip and port.txt"))) {
                writer.write(configTextArea.getText());
                JOptionPane.showMessageDialog(configFrame, "Config saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                configFrame.dispose();
                dashboardFrame.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(configFrame, "Error saving config file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        configFrame.setLocationRelativeTo(null);
        configFrame.setVisible(true);
    }

    private void openAboutDialog(JFrame dashboardFrame) {
        JDialog aboutDialog = new JDialog(dashboardFrame, "About", true);
        aboutDialog.setSize(300, 200);
        aboutDialog.setLayout(new FlowLayout());

        JButton developersButton = new JButton("Developers Team");
        JButton faqsButton = new JButton("FAQs");
        JButton visionButton = new JButton("Vision");

        developersButton.addActionListener(e -> showDeveloperInfo());
        faqsButton.addActionListener(e -> showFaqsInfo());
        visionButton.addActionListener(e -> showVisionInfo());

        aboutDialog.add(developersButton);
        aboutDialog.add(faqsButton);
        aboutDialog.add(visionButton);

        aboutDialog.setLocationRelativeTo(dashboardFrame);
        aboutDialog.setVisible(true);
    }

    private void openP2PDialog(JFrame dashboardFrame, ThemeManagerDashboard themeManager) {
        // Create P2P dialog
        JDialog p2pDialog = new JDialog(dashboardFrame, "P2P Chat", true);
        p2pDialog.setSize(400, 200);
        p2pDialog.setLayout(new BorderLayout());

        // Create a label to display the P2P message
        JLabel messageLabel = new JLabel("Welcome to P2P Option", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Apply the theme to the P2P dialog
        themeManager.updateTheme(p2pDialog); // Reuse the updateTheme method to apply the selected theme to the dialog
        messageLabel.setForeground(UIManager.getColor("Button.foreground")); // Apply the correct
        p2pDialog.add(messageLabel, BorderLayout.CENTER);

        // Show the dialog
        p2pDialog.setLocationRelativeTo(dashboardFrame);  // Center it relative to the main frame
        p2pDialog.setVisible(true);
    }

    private void showDeveloperInfo() {
        JOptionPane.showMessageDialog(null, "Developed by the Messaging App Team.");
    }

    private void showFaqsInfo() {
        JOptionPane.showMessageDialog(null, "FAQs: 1. How to use? 2. Troubleshooting...");
    }

    private void showVisionInfo() {
        JOptionPane.showMessageDialog(null, "Our vision is to create an intuitive messaging platform.");
    }
}
