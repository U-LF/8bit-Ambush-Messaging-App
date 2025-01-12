// DatabaseServer.java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import com.google.gson.*;

public class Main {
    private static final int PORT = 2456;
    private static Connection connection;
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("DatabaseServer started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Add proper connection parameters here
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "ahmed"
            );
        }
        return connection;
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("DatabaseServer received: " + message);
                    JsonObject request = JsonParser.parseString(message).getAsJsonObject();
                    String action = request.get("action").getAsString();
                    String response;

                    switch (action.toLowerCase()) {
                        case "signup":
                            response = handleSignup(request);
                            break;
                        case "login":
                            response = handleLogin(request);
                            break;
                        default:
                            response = createErrorResponse("Invalid action");
                    }

                    System.out.println("DatabaseServer sending: " + response);
                    writer.write(response);
                    writer.newLine();
                    writer.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private String handleSignup(JsonObject request) {
            try {
                String username = request.get("username").getAsString();
                String displayName = request.get("display_name").getAsString();
                String password = request.get("password").getAsString();

                try (Connection conn = getConnection();
                     PreparedStatement checkStmt = conn.prepareStatement("SELECT username FROM authentication WHERE username = ?")) {

                    checkStmt.setString(1, username);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        return createErrorResponse("Username already exists");
                    }

                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO authentication (username, display_name, password) VALUES (?, ?, ?)")) {
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, displayName);
                        insertStmt.setString(3, password); // In production, use password hashing
                        insertStmt.executeUpdate();
                        return createSuccessResponse("Account created successfully");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return createErrorResponse("Signup failed: " + e.getMessage());
            }
        }

        private String handleLogin(JsonObject request) {
            try {
                String username = request.get("username").getAsString();
                String password = request.get("password").getAsString();

                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                             "SELECT * FROM authentication WHERE username = ? AND password = ?")) {

                    stmt.setString(1, username);
                    stmt.setString(2, password); // In production, use password verification

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return createSuccessResponse("Login successful");
                    } else {
                        return createErrorResponse("Invalid username or password");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return createErrorResponse("Login failed: " + e.getMessage());
            }
        }

        private String createSuccessResponse(String message) {
            return String.format("{\"status\":\"success\",\"message\":\"%s\"}", message);
        }

        private String createErrorResponse(String message) {
            return String.format("{\"status\":\"error\",\"message\":\"%s\"}", message);
        }
    }
}

