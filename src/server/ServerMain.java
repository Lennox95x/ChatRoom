package server;

public class ServerMain {
    public static void main(String[] args) {
        int port = 3141; // oder Port per args[] übergeben
        new ServerController(port);
    }
}
