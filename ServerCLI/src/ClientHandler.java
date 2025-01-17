import java.io.*;
import java.net.Socket;
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private String clientName;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new DataOutputStream(clientSocket.getOutputStream());

            clientName = inFromClient.readLine();
            System.out.println("Client connected with name: " + clientName);

            broadcastConnectedClientsList();

            outToClient.writeBytes("Welcome " + clientName + "! You are now connected to the chat room.\n");
            broadcastMessage(clientName + " has joined the chat.", this);

            String clientMessage;
            while ((clientMessage = inFromClient.readLine()) != null) {
                System.out.println("Message from " + clientName + ": " + clientMessage);
                broadcastMessage(clientName + ": " + clientMessage, this);
            }
        } catch (IOException e) {
            System.err.println("Connection error with client: " + clientName);
        } finally {
            try {
                System.out.println(clientName + " has left the chat.");
                broadcastMessage(clientName + " has left the chat.", this);
                Server.clients.remove(this);
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        System.out.println("Broadcasting message: " + message);
        synchronized (Server.clients) {
            for (ClientHandler client : Server.clients) {
                if (client != sender) {
                    try {
                        client.outToClient.writeBytes(message + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void broadcastConnectedClientsList() throws IOException {
        synchronized (Server.clients) {
            StringBuilder clientList = new StringBuilder("Currently connected clients: ");
            for (ClientHandler client : Server.clients) {
                clientList.append(client.clientName).append(", ");
            }
            if (clientList.length() > 0) {
                clientList.setLength(clientList.length() - 2);
            }
            for (ClientHandler client : Server.clients) {
                try {
                    client.outToClient.writeBytes(clientList.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
