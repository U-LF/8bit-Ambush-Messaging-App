package resources;

import resources.Icons;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VisionInfo {

    public static void showVisionInfo(JFrame parentFrame) {
        // Create a JDialog to display the vision background image
        JDialog visionDialog = new JDialog(parentFrame, "Vision", true);
        visionDialog.setSize(700, 600); // Set dialog size
        visionDialog.setLocationRelativeTo(parentFrame); // Center the dialog
        visionDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close the dialog when done

        // Add background image (vision) from Icons package
        JLabel backgroundLabel = new JLabel();
        ImageIcon backgroundIcon = Icons.getIcon("vision.png", 700, 600);  // Load "vision.png" image from the /icons/ folder

        if (backgroundIcon != null) {
            backgroundLabel.setIcon(backgroundIcon);
        } else {
            System.err.println("Background icon not found!");
        }

        backgroundLabel.setBounds(0, 0, 700, 600);  // Set background label bounds

        visionDialog.setLayout(null);  // Use null layout to manually position components

        // Add the background label to the dialog
        visionDialog.add(backgroundLabel);

        // Add key listener for backspace functionality to close the dialog
        visionDialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    visionDialog.dispose();  // Close the dialog on backspace press
                }
            }
        });

        // Make the dialog focusable so it can capture key events
        visionDialog.setFocusable(true);
        visionDialog.setFocusableWindowState(true);

        // Make the dialog visible
        visionDialog.setVisible(true);
    }
}
