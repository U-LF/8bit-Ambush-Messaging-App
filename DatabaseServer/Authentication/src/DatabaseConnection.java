// DatabaseConnection.java
import java.io.*;
import java.net.Socket;

public class DatabaseConnection {
    private static final String DB_SERVER_HOST = "localhost";
    private static final int DB_SERVER_PORT = 2456;

    public String sendRequest(String request) {
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
