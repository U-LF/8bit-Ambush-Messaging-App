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
        JButton connectButton = createStyledCardButton("Connect to ChatRoom", "https://cdn-icons-png.freepik.com/256/11982/11982647.png", e -> {
            dashboardFrame.setVisible(false); // Hide the dashboard
            ClientConnection.connectToServer(dashboardFrame); // Pass the dashboard instance
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
        // Config editor implementation
    }

    private void openAboutDialog(JFrame dashboardFrame) {
        // About dialog implementation
    }

    private void openP2PDialog(JFrame dashboardFrame) {
        // P2P dialog implementation
    }
}
