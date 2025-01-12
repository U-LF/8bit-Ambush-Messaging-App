// AuthenticationServer.java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int AUTH_PORT = 3456;
    private static final String DB_SERVER_HOST = "localhost";
    private static final int DB_SERVER_PORT = 2456;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(AUTH_PORT)) {
            System.out.println("Authentication server started on port " + AUTH_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Authentication server error: " + e.getMessage());
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                String clientRequest;
                while ((clientRequest = clientReader.readLine()) != null) {
                    System.out.println("AuthServer received: " + clientRequest);
                    String dbResponse = forwardToDatabaseServer(clientRequest);
                    System.out.println("AuthServer sending: " + dbResponse);

                    clientWriter.write(dbResponse);
                    clientWriter.newLine();
                    clientWriter.flush();
                }
            } catch (IOException e) {
                System.err.println("Client handler error: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private String forwardToDatabaseServer(String request) {
            try (Socket dbSocket = new Socket(DB_SERVER_HOST, DB_SERVER_PORT);
                 BufferedWriter dbWriter = new BufferedWriter(new OutputStreamWriter(dbSocket.getOutputStream()));
                 BufferedReader dbReader = new BufferedReader(new InputStreamReader(dbSocket.getInputStream()))) {

                dbWriter.write(request);
                dbWriter.newLine();
                dbWriter.flush();

                return dbReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"message\":\"Database server connection failed\"}";
            }
        }
    }
}
