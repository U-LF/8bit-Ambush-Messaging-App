import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    // Set to hold all connected clients' handlers
    protected static Set<ClientHandler> clients = new HashSet<>();
    static StringBuilder messageBuilder = new StringBuilder();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2323);
        System.out.println("Server started, waiting for clients...");

        Thread serverTalkThread = new Thread(() -> {
            try {
                ServerMsgPrompt();

                while (true) {
                    int input = System.in.read();
                    char charInput = (char) input;

                    if (charInput == '\n') { // Enter key pressed
                        String message = messageBuilder.toString().trim();
                        if (!message.isEmpty()) {
                            ClientHandler.broadcastMessage("Server: " + message, null);
                            messageBuilder.setLength(0); // Reset the message builder
                        }
                        System.out.print("\rServer (type message to send): ");
                    } else {
                        messageBuilder.append(charInput);
                        System.out.print("\rServer (type message to send): " + messageBuilder);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading server input: " + e.getMessage());
            }
        });
        serverTalkThread.start();

        while (true) {
            // Accept new client connections
            Socket clientSocket = serverSocket.accept();
            System.out.println("\nClient connected: " + clientSocket.getInetAddress());
            ServerMsgPrompt("\r");

            // Create a new handler for the client and start a new thread
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    public static void ServerMsgPrompt(String NewLineCharacter){
        System.out.print(NewLineCharacter + "Server (type message to send): " + messageBuilder);
    }

    public static void ServerMsgPrompt(){
        System.out.print("Server (type message to send): " + messageBuilder);
    }
}