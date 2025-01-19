import javax.swing.*;

// number 3 ClientGUI
public class ClientGUI {

    public static void main(String[] args) {
        // Create and show the Login GUI
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}