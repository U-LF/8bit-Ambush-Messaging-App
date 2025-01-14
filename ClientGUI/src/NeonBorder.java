import javax.swing.border.AbstractBorder;
import java.awt.*;

public class NeonBorder extends AbstractBorder {
    private final int radius;

    public NeonBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Neon glow effect for the border
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(204, 51, 255)); // Neon purple color
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Rounded corners

        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5);
    }
}
