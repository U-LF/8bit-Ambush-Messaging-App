import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ThemeManagerDashboard {

    private String currentTheme = "Light"; // Default theme is Light

    public void switchTheme(JFrame frame) {
        JDialog themeDialog = new JDialog(frame, "Select Theme", true);
        themeDialog.setMinimumSize(new Dimension(500, 400));
        themeDialog.setPreferredSize(new Dimension(700, 600));

        themeDialog.setLayout(new BorderLayout());  // Set BorderLayout for easy South placement
        themeDialog.setLocationRelativeTo(frame);

        // Create JPanel to hold card buttons in 2 rows and 3 columns
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2 rows, 3 columns, 10px gap
        buttonPanel.setBackground(Color.WHITE); // Panel background

        // Add card buttons with gradient colors
        buttonPanel.add(createCardButton("Pink", new Color(214, 41, 232), new Color(255, 105, 180)));
        buttonPanel.add(createCardButton("Green", new Color(0, 195, 0), new Color(0, 255, 0)));
        buttonPanel.add(createCardButton("Dark", Color.DARK_GRAY, Color.BLACK));
        buttonPanel.add(createCardButton("Yellow", new Color(255, 255, 0), new Color(255, 223, 0)));
        buttonPanel.add(createCardButton("Purple", new Color(128, 0, 128), new Color(255, 0, 255)));
        buttonPanel.add(createCardButton("Light", new Color(236, 229, 236), new Color(227, 213, 227))); // Light theme button

        themeDialog.add(buttonPanel, BorderLayout.CENTER);  // Add theme buttons in the center

        // Apply button panel at the bottom (South)
        JPanel applyButtonPanel = new JPanel();
        applyButtonPanel.setBackground(Color.WHITE); // Background color for the panel (can be transparent)
        applyButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Center the Apply button in the panel

        JButton applyButton = createApplyButton(); // Create the Apply button with gradient
        applyButtonPanel.add(applyButton); // Add the apply button to the panel

        themeDialog.add(applyButtonPanel, BorderLayout.SOUTH);  // Place the apply button at the bottom

        themeDialog.setVisible(true);
    }

    private JButton createApplyButton() {
        // Create Apply button with gradient color
        JButton applyButton = new JButton("Apply");
        applyButton.setPreferredSize(new Dimension(150, 60)); // Set a bigger height and width for the button
        applyButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font for better readability
        applyButton.setForeground(Color.WHITE); // Set text color to white

        // Set gradient background for the button (Color.WHITE to Color.CYAN)
        applyButton.setContentAreaFilled(false);
        applyButton.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        applyButton.setFocusPainted(false);
        applyButton.setOpaque(false);

        // Add action listener to apply the theme
        applyButton.addActionListener(e -> {
            updateTheme(applyButton);  // Pass the button (or the frame) to update theme
            // Close the dialog after applying the theme
            ((JDialog) applyButton.getTopLevelAncestor()).dispose();
        });

        // Add mouse listener to give a clickable feel
        applyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                applyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor on hover
            }

            @Override
            public void mousePressed(MouseEvent e) {
                applyButton.setBackground(Color.LIGHT_GRAY);  // Change background color on click
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                applyButton.setBackground(Color.CYAN);  // Reset background when click is released
            }
        });

        // Set a gradient background (Color.WHITE to Color.CYAN)
        applyButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(221, 162, 15), 0, c.getHeight(), new Color(230, 186, 22));
                //GradientPaint gradient = new GradientPaint(0, 0, Color.CYAN, 0, c.getHeight(), Color.CYAN);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30); // Increased roundness
                super.paint(g, c);
            }
        });

        return applyButton;
    }

    private JButton createCardButton(String themeName, Color color1, Color color2) {
        // Create a JButton with custom paint
        JButton cardButton = new GradientButton(themeName, color1, color2);
        cardButton.setForeground(Color.WHITE); // Set text color to white

        // Add a listener to change the theme on click
        cardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentTheme = themeName;
                updateTheme((Component) e.getSource());
            }
        });

        // Add mouse listener to give clickable feel to card button
        cardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentTheme = themeName;
                updateTheme((Component) e.getSource());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cardButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor on hover
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cardButton.setBackground(Color.LIGHT_GRAY);  // Change background color on click
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cardButton.setBackground(color1);  // Reset background when click is released
            }
        });


        return cardButton;
    }

    public void updateTheme(Component component) {
        Color backgroundColor;
        Color foregroundColor;

        // Set colors based on the selected theme
        switch (currentTheme) {
            case "Dark" -> {
                backgroundColor = Color.DARK_GRAY;
                foregroundColor = Color.WHITE;
            }
            case "Green" -> {
                backgroundColor = new Color(0, 195, 0);
                foregroundColor = Color.WHITE;
            }
            case "Pink" -> {
                backgroundColor = new Color(255, 182, 193);
                foregroundColor = Color.WHITE;
            }
            case "Yellow" -> {
                backgroundColor = new Color(255, 255, 0);
                foregroundColor = Color.BLACK;
            }
            case "Purple" -> {
                backgroundColor = new Color(128, 0, 128);
                foregroundColor = Color.WHITE;
            }
            case "Light" -> {
                backgroundColor = Color.WHITE;
                foregroundColor = Color.BLACK;
            }
            default -> {
                backgroundColor = Color.WHITE;
                foregroundColor = Color.BLACK;
            }
        }

        // Update the UI manager settings for the selected theme
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Button.background", backgroundColor);
        UIManager.put("Button.foreground", foregroundColor);
        UIManager.put("TextArea.background", backgroundColor);
        UIManager.put("TextArea.foreground", foregroundColor);
        UIManager.put("TextField.background", backgroundColor);
        UIManager.put("TextField.foreground", foregroundColor);

        // Apply the updated theme to all components (whether JFrame or JDialog)
        SwingUtilities.updateComponentTreeUI(component);
    }
}

// Custom JButton to support gradient backgrounds
class GradientButton extends JButton {
    private final Color color1, color2;
    private final Color hoverColor = new Color(255, 255, 255, 100); // Light hover effect
    private final Color pressedColor = new Color(200, 200, 200); // Dark pressed effect

    public GradientButton(String text, Color color1, Color color2) {
        super(text);
        this.color1 = color1;
        this.color2 = color2;
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gradient);

        // Add a slight effect when hovered or pressed
        if (getModel().isPressed()) {
            g2d.setColor(pressedColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Round corners with pressed effect
        } else if (getModel().isRollover()) {
            g2d.setColor(hoverColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Round corners with hover effect
        } else {
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Normal rounded button
        }

        super.paintComponent(g); // Paint text and other button components
        g2d.dispose();
    }
}
