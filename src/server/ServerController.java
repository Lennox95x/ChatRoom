package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Zum Starten des Managers und des Handlers
public class ServerController {
    private ServerChatManager manager;
    private ServerSocket serverSocket;

    public ServerController(int port) {
        manager = new ServerChatManager();

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Chat-Server läuft auf Port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, manager);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Starten des Servers: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

