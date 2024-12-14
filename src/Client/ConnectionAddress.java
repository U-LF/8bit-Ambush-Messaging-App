package Client;

import java.io.*;

public class ConnectionAddress {
    private String IpAddress;
    private int Port;
    private File file;

    public ConnectionAddress(String FileName) {
        file = new File(FileName);

        if (file.exists()) {
            readFromFile();
        } else {
            createDefaultFile();
        }
    }

    private void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read the IP address
            IpAddress = reader.readLine();
            if (IpAddress == null) {
                System.out.println("Missing IP address in the file. Using default value.");
                IpAddress = "0.0.0.0";
            }

            // Read the port
            String portString = reader.readLine();
            if (portString == null) {
                System.out.println("Missing port in the file. Using default value.");
                Port = 1234;
            } else {
                try {
                    Port = Integer.parseInt(portString);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port number format. Using default value.");
                    Port = 1234;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }

    private void createDefaultFile() {
        try (FileWriter writer = new FileWriter(file)) {
            // Create the file with default values
            writer.write("0.0.0.0\n");
            writer.write("1234\n");
            System.out.println("File created with default values: " + file.getName());

            // Update the fields with default values
            IpAddress = "0.0.0.0";
            Port = 1234;
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public int getPort() {
        return Port;
    }
}
