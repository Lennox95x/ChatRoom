package server;


import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ServerChatManager manager;
    private BufferedReader reader;
    private PrintWriter writer;
    private String name;

    public ClientHandler(Socket socket, ServerChatManager manager) {
        this.socket = socket;
        this.manager = manager;
        this.name = socket.getInetAddress().getHostAddress();
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            manager.broadcast(name + " connected.");
            manager.addClient(this);

            String msg;
            while ((msg = reader.readLine()) != null) {
                manager.broadcast(name + ": " + msg);
            }
        } catch (IOException e) {
            System.out.println("Verbindung mit " + name + " unterbrochen.");
        } finally {
            manager.removeClient(this);
            manager.broadcast(name + " disconnected.");
            close();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private void close() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

