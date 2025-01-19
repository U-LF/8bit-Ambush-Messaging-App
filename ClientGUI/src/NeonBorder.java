import java.awt.*;
import javax.swing.border.*;

public class NeonBorder extends AbstractBorder {
    private int radius;

    public NeonBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a gradient from cyan to blue for the neon effect
        GradientPaint gradient = new GradientPaint(0, 0, new Color(162, 69, 196), 0, height, new Color(0, 255, 13));
        g2d.setPaint(gradient);

        g2d.setStroke(new BasicStroke(3)); // Set thickness for the gradient border
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5); // Adjust insets for the neon effect
    }
}