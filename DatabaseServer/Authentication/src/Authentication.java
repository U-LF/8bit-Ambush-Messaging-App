// AuthenticationServer.java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Authentication {
    private static final int AUTH_PORT = 3456;
    private final ExecutorService executor;
    private final DatabaseConnection dbConnection;

    public Authentication() {
        this.executor = Executors.newCachedThreadPool();
        this.dbConnection = new DatabaseConnection();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(AUTH_PORT)) {
            System.out.println("Authentication server started on port " + AUTH_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                executor.submit(new AuthenticationClientHandler(clientSocket, dbConnection));
            }
        } catch (IOException e) {
            System.err.println("Authentication server error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        Authentication server = new Authentication();
        server.start();
    }
}