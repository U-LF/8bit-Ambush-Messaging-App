import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a gradient from cyan to blue (or any other colors you prefer)
        GradientPaint gradient = new GradientPaint(0, 0, new Color(214, 41, 232), 0, height, new Color(0, 128, 255));
        g2d.setPaint(gradient);

        // Apply the gradient stroke for the rounded border
        g2d.setStroke(new BasicStroke(5)); // Adjust the stroke width as necessary
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5); // Adjust the insets as necessary
    }
}
