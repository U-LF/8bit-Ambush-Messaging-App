// ClientHandler.java
import java.io.*;
import java.net.Socket;
import com.google.gson.*;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final DatabaseManager dbManager;

    public ClientHandler(Socket clientSocket, DatabaseManager dbManager) {
        this.clientSocket = clientSocket;
        this.dbManager = dbManager;
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
                        String username = request.get("username").getAsString();
                        String displayName = request.get("display_name").getAsString();
                        String password = request.get("password").getAsString();
                        String result = dbManager.handleSignup(username, displayName, password);
                        response = createResponse(result);
                        break;
                    case "login":
                        username = request.get("username").getAsString();
                        password = request.get("password").getAsString();
                        result = dbManager.handleLogin(username, password);
                        response = createResponse(result);
                        break;
                    default:
                        response = createResponse("error:Invalid action");
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

    private String createResponse(String result) {
        String[] parts = result.split(":", 2);
        String status = parts[0];
        String message = parts[1];
        return String.format("{\"status\":\"%s\",\"message\":\"%s\"}", status, message);
    }
}
