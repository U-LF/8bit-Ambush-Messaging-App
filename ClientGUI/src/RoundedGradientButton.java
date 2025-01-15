package dialogs;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class RoundedGradientButton extends JButton {
    private final Color startColor;
    private final Color endColor;
    private static Font muliBoldFont;

    // Static block to load the custom font
    static {
        try {
            InputStream fontStream = RoundedGradientButton.class.getResourceAsStream("/fonts/Muli-Bold.ttf");
            if (fontStream != null) {
                muliBoldFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(18f);
            } else {
                muliBoldFont = new Font("Arial", Font.BOLD, 18); // Fallback font
            }
        } catch (Exception e) {
            muliBoldFont = new Font("Arial", Font.BOLD, 18); // Fallback font in case of an error
            e.printStackTrace();
        }
    }

    /**
     * Constructor for the RoundedGradientButton
     *
     * @param text       The button's text
     * @param startColor The starting color for the gradient
     * @param endColor   The ending color for the gradient
     */
    public RoundedGradientButton(String text, Color startColor, Color endColor) {
        super(text);
        this.startColor = startColor;
        this.endColor = endColor;
        initializeButtonProperties();
    }

    /**
     * Initializes the button's properties
     */
    private void initializeButtonProperties() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(muliBoldFont); // Set the custom or fallback font
        setForeground(Color.WHITE); // Set text color
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand pointer
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create and apply the gradient paint
        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
        g2d.setPaint(gradientPaint);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded rectangle

        // Draw the button's text in the center
        FontMetrics fm = g.getFontMetrics();
        String text = getText();
        int textX = (getWidth() - fm.stringWidth(text)) / 2;
        int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2d.setColor(getForeground());
        g2d.drawString(text, textX, textY);

        g2d.dispose(); // Clean up
        super.paintComponent(g);
    }
}
