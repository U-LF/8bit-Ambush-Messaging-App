import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;

public class AuthenticationClient {
    private static final String SERVER_URI = "ws://localhost:8766";
    private WebSocket webSocket;
    private boolean running = true;

    public void connect() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            webSocket = client.newWebSocketBuilder()
                    .buildAsync(URI.create(SERVER_URI), new WebSocket.Listener() {
                        StringBuilder messageBuffer = new StringBuilder();

                        @Override
                        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                            messageBuffer.append(data);
                            if (last) {
                                handleMessage(messageBuffer.toString());
                                messageBuffer.setLength(0);
                            }
                            return WebSocket.Listener.super.onText(webSocket, data, last);
                        }

                        @Override
                        public void onOpen(WebSocket webSocket) {
                            System.out.println("Connected to server");
                        }

                        @Override
                        public void onError(WebSocket webSocket, Throwable error) {
                            System.out.println("Connection error: " + error.getMessage());
                            running = false;
                        }

                        @Override
                        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                            System.out.println("Connection closed: " + reason);
                            running = false;
                            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
                        }
                    }).join();
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
            running = false;
        }
    }

    private void handleMessage(String message) {
        try {
            if (message.contains("login_response")) {
                String status = extractJsonField(message, "status");
                if ("match".equals(status)) {
                    System.out.println("✅ Login successful!");
                } else if ("unmatched".equals(status)) {
                    System.out.println("❌ Login failed!");
                }
            } else if (message.contains("signup_response")) {
                String status = extractJsonField(message, "status");
                if ("success".equals(status)) {
                    System.out.println("✅ Signup successful!");
                } else {
                    System.out.println("❌ Signup failed!");
                }
            } else if (message.contains("error")) {
                System.out.println("❌ Error: " + extractJsonField(message, "message"));
            }
        } catch (Exception e) {
            System.out.println("Error processing response: " + e.getMessage());
        }
    }

    private String extractJsonField(String json, String field) {
        String search = "\"" + field + "\":\"";
        int startIndex = json.indexOf(search);
        if (startIndex == -1) return null;
        startIndex += search.length();
        int endIndex = json.indexOf("\"", startIndex);
        return endIndex == -1 ? null : json.substring(startIndex, endIndex);
    }

    public void sendMessage(String message) {
        try {
            webSocket.sendText(message, true);
            Thread.sleep(100); // Small delay for response
        } catch (Exception e) {
            System.out.println("Send error: " + e.getMessage());
        }
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String request = String.format("{\"type\":\"login\",\"username\":\"%s\",\"password\":\"%s\"}", 
            username, password);
        sendMessage(request);
    }

    public void signup() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("Display name: ");
        String displayName = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String request = String.format(
            "{\"type\":\"signup\",\"username\":\"%s\",\"display_name\":\"%s\",\"password\":\"%s\"}", 
            username, displayName, password);
        sendMessage(request);
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.println("\n1. Login\n2. Sign Up\n3. Exit");
            System.out.print("Choice: ");
            switch (scanner.nextLine()) {
                case "1":
                login();
                break;
            case "2":
                signup();
                break;
            case "3":
                System.out.println("Goodbye!");
                running = false;
                webSocket.abort();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        AuthenticationClient client = new AuthenticationClient();
        client.connect();
        if (client.running) {
            client.showMenu();
        }
    }
}