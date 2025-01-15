import java.io.*;
import java.net.Socket;

public class BackendService {
    public static boolean login(String username, String password) {
        try (Socket socket = new Socket("localhost", 3456);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String request = String.format("{\"action\":\"login\",\"username\":\"%s\",\"password\":\"%s\"}", username, password);
            writer.write(request);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            return response.contains("\"status\":\"success\"");
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean signup(String username, String displayName, String password) {
        try (Socket socket = new Socket("localhost", 3456);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String request = String.format(
                    "{\"action\":\"signup\",\"username\":\"%s\",\"display_name\":\"%s\",\"password\":\"%s\"}",
                    username, displayName, password);
            writer.write(request);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            return response.contains("\"status\":\"success\"");
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
