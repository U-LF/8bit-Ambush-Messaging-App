import javax.swing.*;
/*import java.io.*;
import java.net.*;

import ClientShared.ConnectionAddress;*/

public class ClientGUI {

    public static void main(String[] args) {
        // Create and show the Dashboard first
        SwingUtilities.invokeLater(() -> {
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.showDashboard();
        });
    }


}