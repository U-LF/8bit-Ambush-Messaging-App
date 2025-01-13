
// AuthenticationClientHandler.java
import java.io.*;
import java.net.Socket;

public class AuthenticationClientHandler implements Runnable {
    private final Socket clientSocket;
    private final DatabaseConnection dbConnection;

    public AuthenticationClientHandler(Socket clientSocket, DatabaseConnection dbConnection) {
        this.clientSocket = clientSocket;
        this.dbConnection = dbConnection;
    }

    @Override
    public void run() {
        try (BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String clientRequest;
            while ((clientRequest = clientReader.readLine()) != null) {
                System.out.println("AuthServer received: " + clientRequest);
                String dbResponse = dbConnection.sendRequest(clientRequest);
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
}
