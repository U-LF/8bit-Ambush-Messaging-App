// DatabaseServer.java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class DatabaseServer {
    private static final int PORT = 2456;
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private final DatabaseManager dbManager;

    public DatabaseServer() {
        this.dbManager = new DatabaseManager();
    }

    public void start() {
        try {
            DatabaseManager.initializeDatabase();

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("DatabaseServer started on port " + PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    executor.submit(new ClientHandler(clientSocket, dbManager));
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseServer server = new DatabaseServer();
        server.start();
    }
}