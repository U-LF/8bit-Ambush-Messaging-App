import javax.swing.*;
/*import java.io.*;
import java.net.*;

import ClientShared.ConnectionAddress;*/

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


// number 2 ClientGUI
/*public class ClientGUI {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            // Launch console-based application
            ConsoleApp.run();
        } else {
            // Launch GUI-based application
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
            });
        }
    }
}*/


// number 1 ClientGUI
/*public class ClientGUI {

    public static void main(String[] args) {
        // Create and show the Dashboard first
        SwingUtilities.invokeLater(() -> {
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.showDashboard();
        });
    }
}*/