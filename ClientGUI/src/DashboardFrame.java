import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.Border;
import resources.Icons;
import java.awt.geom.RoundRectangle2D;
import dialogs.AboutDialog;




public class DashboardFrame {
    private boolean isDarkTheme = false; // To track the current theme state

    public void showDashboard() {
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setMinimumSize(new Dimension(700, 600));
        dashboardFrame.setPreferredSize(new Dimension(900, 700));

        // Remove the title bar and set the shape to rounded corners
        //dashboardFrame.setUndecorated(true); // Removes the default title bar
        //dashboardFrame.setShape(new RoundRectangle2D.Double(0, 0, dashboardFrame.getWidth(), dashboardFrame.getHeight(), 20, 20)); // Rounded corners with radius 40

       // dashboardFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Optional: Removes any window decoration (like shadow)



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
        JButton p2pButton = createStyledCardButton("P2P", Icons.getIcon("p2p.png", 80, 80), e -> openP2PDialog(dashboardFrame));
        JButton connectButton = createStyledCardButton("Connect to ChatRoom", Icons.getIcon("chatroom.png", 80, 80), e -> {
            dashboardFrame.dispose();
            ClientConnection.connectToServer(dashboardFrame); // Placeholder for server connection logic
        });
        JButton configButton = createStyledCardButton("Config", Icons.getIcon("config.png", 80, 80), e -> openConfigEditor(dashboardFrame));

        JButton aboutButton = createStyledCardButton("About", Icons.getIcon("about.png", 80, 80), e -> AboutDialog.openAboutDialog(dashboardFrame));
// Add a Theme button
        JButton themeButton = createStyledCardButton("Theme", Icons.getIcon("theme.png", 80, 80), e -> {
            ThemeManagerDashboard themeManager = new ThemeManagerDashboard();
            themeManager.switchTheme(dashboardFrame); // Launch the theme switcher dialog
        });

        JButton logoutButton = createStyledCardButton("Logout", Icons.getIcon("logout.png", 80, 80), e -> System.exit(0));

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



        JLabel appNameLabel = new JLabel("Dashboard");
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

    private JButton createStyledCardButton(String text, ImageIcon icon, ActionListener actionListener) {
        JButton button = new JButton("<html><center>" + text + "<br>Some words about</center></html>");
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));

        // Set the icon if it's provided
        if (icon != null) {
            button.setIcon(icon);
        }

        // Apply rounded corners initially
        button.setBorder(new RoundedBorder(150)); // Rounded corners (adjust as needed)

        // Hover effect: Apply neon glow border when mouse enters
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(new NeonBorder(120)); // Neon border on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(new RoundedBorder(150)); // Restore rounded border when mouse exits
            }
        });

        button.addActionListener(actionListener);
        return button;
    }


    // Custom border for rounded corners
    /*static class RoundedBorder implements Border {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            // Change the border color to a solid RED
            g2d.setColor(Color.WHITE); // Change this to your preferred color
            g2d.setStroke(new BasicStroke(5)); // Set the thickness of the border
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Draw the rounded border
            g2d.dispose();
        }
    }*/

    // Custom border for neon glow effect
    /*static class NeonBorder implements Border {
        private final int radius;

        NeonBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 8, radius + 5, radius + 8, radius + 5); // Slightly bigger inset for the neon border
        }

        @Override
        public boolean isBorderOpaque() {
            return false; // Allow transparency
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            //g2d.setColor(Color.CYAN); // Cyan color for the neon glow
            g2d.setColor(new Color(214, 41, 232, 255)); // Purple neon glow

            g2d.setStroke(new BasicStroke(5)); // Set thickness for the glow effect
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Draw the neon border
            g2d.dispose();
        }
    }*/


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

    /*private void openAboutDialog(JFrame dashboardFrame) {
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
    }*/

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







