// Client.java
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class DatabaseClient {
    private static final String AUTH_SERVER_HOST = "localhost";
    private static final int AUTH_SERVER_PORT = 3456;

    public static void main(String[] args) {
        try (Socket socket = new Socket(AUTH_SERVER_HOST, AUTH_SERVER_PORT);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Authentication server");
            String choice;

            do {
                System.out.println("\n1. Login");
                System.out.println("2. Signup");
                System.out.println("3. Exit");
                System.out.print("Choice: ");
                choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        System.out.println("Login function: "+ handleLogin(writer, reader, scanner));
                        break;
                    case "2":
                        handleSignup(writer, reader, scanner);
                        break;
                    case "3":
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } while (!"3".equals(choice));

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    } //main end

    private static boolean handleLogin(BufferedWriter writer, BufferedReader reader, Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String request = String.format(
                "{\"action\":\"login\",\"username\":\"%s\",\"password\":\"%s\"}",
                username, password
        );

        System.out.println("Sending login request...");
        writer.write(request);
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        System.out.println("Server response: " + response);

        // Manually parse the response string
        if (response.contains("\"status\":\"success\"") && response.contains("\"message\":\"Login successful\"")) {
            return true;
        }

        return false;
    }


    private static boolean handleSignup(BufferedWriter writer, BufferedReader reader, Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Display name: ");
        String displayName = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String request = String.format(
                "{\"action\":\"signup\",\"username\":\"%s\",\"display_name\":\"%s\",\"password\":\"%s\"}",
                username, displayName, password
        );

        System.out.println("Sending signup request...");
        writer.write(request);
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        System.out.println("Server response: " + response);

        if (response.contains("\"status\":\"success\"") && response.contains("\"message\":\"Account created successfully\"")) {
            return true;
        }

        return false;
    }
}