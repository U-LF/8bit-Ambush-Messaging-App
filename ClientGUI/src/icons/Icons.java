package resources;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Icons {

    // Define the resource path within the resources folder
    private static final String ICONS_PATH = "/icons/";

    /**
     * Retrieves an icon by file name and resizes it to the specified dimensions.
     *
     * @param fileName The name of the icon file (e.g., "p2p.png").
     * @param width    The desired width of the icon.
     * @param height   The desired height of the icon.
     * @return The resized ImageIcon, or null if the icon cannot be loaded.
     */
    public static ImageIcon getIcon(String fileName, int width, int height) {
        try {
            // Load the icon as a resource from the classpath
            InputStream iconStream = Icons.class.getResourceAsStream(ICONS_PATH + fileName);

            if (iconStream == null) {
                throw new IllegalArgumentException("Icon not found: " + fileName);
            }

            // Load the image icon from the input stream
            ImageIcon icon = new ImageIcon(iconStream.readAllBytes());
            // Resize the icon
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        } catch (IOException e) {
            // Catch IOException and other resource loading exceptions
            System.err.println("Error loading icon: " + e.getMessage());
            return null; // Return null if there's an error
        } catch (IllegalArgumentException e) {
            // Handle the case where the icon file doesn't exist
            System.err.println(e.getMessage());
            return null; // Return null if the icon can't be found
        } catch (Exception e) {
            // Catch any other unexpected errors
            e.printStackTrace();
            return null;
        }
    }
}
