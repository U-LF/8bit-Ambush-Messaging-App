package dialogs;
import dialogs.RoundedGradientButton;

import resources.DeveloperInfo;
import resources.FAQsInfo;
import resources.Icons;
import resources.VisionInfo;

import javax.swing.*;
import java.awt.*;

public class AboutDialog {

    public static void openAboutDialog(JFrame parentFrame) {
        JDialog aboutDialog = new JDialog(parentFrame, "About", true);
        aboutDialog.setMinimumSize(new Dimension(700, 600)); // Adjusted dialog size
        aboutDialog.setPreferredSize(new Dimension(700, 600)); // Adjusted dialog size
        aboutDialog.setLayout(null); // Use manual positioning

        // Set background
        JLabel backgroundLabel = new JLabel();
        ImageIcon backgroundIcon = Icons.getIcon("aboutpic.png", 700, 500);
        if (backgroundIcon != null) {
            backgroundLabel.setIcon(backgroundIcon);
        }
        backgroundLabel.setBounds(0, 0, 700, 500);

        // Create buttons
        RoundedGradientButton developersButton = new RoundedGradientButton("Developers Team", Color.BLUE, Color.CYAN);
        RoundedGradientButton faqsButton = new RoundedGradientButton("FAQs", Color.GREEN, Color.YELLOW);
        RoundedGradientButton visionButton = new RoundedGradientButton("Vision", Color.MAGENTA, Color.PINK);

        // Set button bounds
        developersButton.setBounds(250, 150, 200, 50);
        faqsButton.setBounds(250, 220, 200, 50);
        visionButton.setBounds(250, 290, 200, 50);

        // Add action listeners
        developersButton.addActionListener(e -> DeveloperInfo.showDeveloperInfo());
        faqsButton.addActionListener(e -> FAQsInfo.showFaqsInfo(parentFrame));
        visionButton.addActionListener(e -> VisionInfo.showVisionInfo());

        // Add components to the dialog
        aboutDialog.add(developersButton);
        aboutDialog.add(faqsButton);
        aboutDialog.add(visionButton);
        aboutDialog.add(backgroundLabel);

        aboutDialog.setLocationRelativeTo(parentFrame);
        aboutDialog.setVisible(true);
    }
}