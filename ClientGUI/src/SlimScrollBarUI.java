import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class SlimScrollBarUI extends BasicScrollBarUI {

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createArrowButton(orientation);
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createArrowButton(orientation);
    }

    private JButton createArrowButton(int orientation) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));  // No button size
        button.setBorder(new EmptyBorder(0, 0, 0, 0));  // No border
        button.setBackground(new Color(240, 240, 240));  // Light background color
        return button;
    }

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(100, 100, 100); // Set thumb color (the draggable part)
        this.trackColor = new Color(220, 220, 220); // Set track color (background of the scrollbar)
        this.thumbHighlightColor = new Color(150, 150, 150); // Color when hovered
        this.thumbDarkShadowColor = new Color(50, 50, 50); // Color for the shadow when hovered
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.setColor(thumbColor);
        g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);  // Rounded thumb
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(trackColor);
        g.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 10, 10); // Rounded track
    }
}
