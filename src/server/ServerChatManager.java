package server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//Dient dazu die clients zu verwalten, welche mit dem Server verbunden sind
public class ServerChatManager {
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcast(String message) {
        System.out.println(message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}
