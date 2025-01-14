import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

public class DashboardFrame {
    private boolean isDarkTheme = false; // To track the current theme state

    public void showDashboard() {
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setMinimumSize(new Dimension(700, 600));
        dashboardFrame.setPreferredSize(new Dimension(900, 700));

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color gradientStart = new Color(113, 54, 252); // Purple
                Color gradientEnd = new Color(178, 75, 216);   // Light Purple
                GradientPaint gradientPaint = new GradientPaint(0, 0, gradientStart, 0, getHeight(), gradientEnd);
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new BorderLayout());

        // Create buttons with proper layout management and dynamic resizing
        JButton p2pButton = createStyledCardButton("P2P", "https://cdn-icons-png.freepik.com/256/12283/12283727.png?uid=R93866617&ga=GA1.1.1948482591.1701710188&semt=ais_hybrid", e -> openP2PDialog(dashboardFrame));
        JButton connectButton = createStyledCardButton("Connect to ChatRoom", "https://cdn-icons-png.freepik.com/256/12283/12283727.png?uid=R93866617&ga=GA1.1.1948482591.1701710188&semt=ais_hybrid", e -> {
            dashboardFrame.dispose();
            ClientConnection.connectToServer(); // Placeholder for server connection logic
        });
        JButton configButton = createStyledCardButton("Config", "https://cdn-icons-png.freepik.com/256/12283/12283727.png?uid=R93866617&ga=GA1.1.1948482591.1701710188&semt=ais_hybrid", e -> openConfigEditor(dashboardFrame));
        JButton aboutButton = createStyledCardButton("About", "https://cdn-icons-png.freepik.com/256/12283/12283727.png?uid=R93866617&ga=GA1.1.1948482591.1701710188&semt=ais_hybrid", e -> openAboutDialog(dashboardFrame));

        // Add a Theme button
        JButton themeButton = createStyledCardButton("Theme", "https://cdn-icons-png.freepik.com/256/12283/12283727.png?uid=R93866617&ga=GA1.1.1948482591.1701710188&semt=ais_hybrid", e -> {
            ThemeManagerDashboard themeManager = new ThemeManagerDashboard();
            themeManager.switchTheme(dashboardFrame); // Launch the theme switcher dialog
        });

        JButton logoutButton = createStyledCardButton("Logout", "https://cdn-icons-png.freepik.com/256/12283/12283727.png?uid=R93866617&ga=GA1.1.1948482591.1701710188&semt=ais_hybrid", e -> System.exit(0));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(p2pButton);
        buttonPanel.add(connectButton);
        buttonPanel.add(configButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(themeButton);
        buttonPanel.add(logoutButton);

        JPanel topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color gradientStart = new Color(178, 75, 216);
                Color gradientEnd = new Color(113, 54, 252);
                GradientPaint gradientPaint = new GradientPaint(0, 0, gradientStart, 0, getHeight(), gradientEnd);
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel appNameLabel = new JLabel("811 Msg App");
        appNameLabel.setFont(new Font("Calibri Light (Headings)", Font.BOLD, 24));
        appNameLabel.setForeground(Color.WHITE);
        topPanel.add(appNameLabel);

        gradientPanel.add(topPanel, BorderLayout.NORTH);
        gradientPanel.add(buttonPanel, BorderLayout.CENTER);

        dashboardFrame.setContentPane(gradientPanel);
        dashboardFrame.pack();
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setVisible(true);

        // Initial theme setup
        ThemeManagerDashboard themeManager = new ThemeManagerDashboard();
        themeManager.updateTheme(dashboardFrame); // Apply initial theme
    }

    private JButton createStyledCardButton(String text, String iconUrl, ActionListener actionListener) {
        JButton button = new JButton("<html><center>" + text + "<br>Some words about</center></html>");
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));

        try {
            ImageIcon icon = new ImageIcon(new URL(iconUrl));
            Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Apply rounded corners to the button
        button.setBorder(new RoundedBorder(30)); // Rounded corners (adjust as needed)

        // Add gradient fill to match background
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g;
                Color gradientStart = new Color(113, 54, 252); // Purple
                Color gradientEnd = new Color(178, 75, 216);   // Light Purple
                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, 0, button.getHeight(), gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 30, 30); // Rounded corners
                super.paint(g, c); // Paint text and icon
            }
        });

        // Hover effect with neon glow border
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(new NeonBorder(30)); // Neon glow on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(new RoundedBorder(30)); // Retain rounded corners and remove glow
            }
        });

        button.addActionListener(actionListener);
        return button;
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
        aboutDialog.setSize(500, 400);
        aboutDialog.setLayout(new FlowLayout());

        JButton developersButton = new JButton("Developers Team");
        JButton faqsButton = new JButton("FAQs");
        JButton visionButton = new JButton("Vision");

        developersButton.addActionListener(e -> DeveloperInfo.showDeveloperInfo());
        faqsButton.addActionListener(e -> FAQsInfo.showFaqsInfo());
        visionButton.addActionListener(e -> VisionInfo.showVisionInfo());

        aboutDialog.add(developersButton);
        aboutDialog.add(faqsButton);
        aboutDialog.add(visionButton);

        aboutDialog.setLocationRelativeTo(dashboardFrame);
        aboutDialog.setVisible(true);
    }

    private void openP2PDialog(JFrame dashboardFrame) {
        JDialog p2pDialog = new JDialog(dashboardFrame, "P2P Chat", true);
        p2pDialog.setSize(400, 200);
        p2pDialog.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("Welcome to P2P Option", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        p2pDialog.add(messageLabel, BorderLayout.CENTER);

        p2pDialog.setLocationRelativeTo(dashboardFrame);
        p2pDialog.setVisible(true);
    }
}

