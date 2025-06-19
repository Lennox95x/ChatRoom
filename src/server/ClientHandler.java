package server;


import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ServerChatManager manager;
    private BufferedReader reader;
    private PrintWriter writer;
    private String name;
    private UserManager userManager;

    public ClientHandler(Socket socket, ServerChatManager manager) {
        this.socket = socket;
        this.manager = manager;

        try {
            this.userManager = new UserManager(); // Verbindung zur SQLite-Datenbank
        } catch (SQLException e) {
            System.err.println("Fehler beim Initialisieren des UserManagers: " + e.getMessage());
        }
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // == LOGIN oder REGISTER verarbeiten ==
            String line = reader.readLine();
            if (line == null) {
                writer.println("ERROR: Ungültige Verbindung");
                close();
                return;
            }

            if (line.startsWith("LOGIN|") || line.startsWith("REGISTER|")) {
                String[] parts = line.split("\\|");
                if (parts.length != 3) {
                    writer.println("ERROR: Ungültiges Format");
                    close();
                    return;
                }

                String command = parts[0];
                String username = parts[1];
                String hashedPassword = parts[2];

                boolean success = false;
                if ("LOGIN".equalsIgnoreCase(command)) {
                    success = userManager.login(username, hashedPassword);
                    writer.println(success ? "LOGIN_OK" : "ERROR: Login fehlgeschlagen");
                } else if ("REGISTER".equalsIgnoreCase(command)) {
                    success = userManager.register(username, hashedPassword);
                    writer.println(success ? "REGISTER_OK" : "ERROR: Registrierung fehlgeschlagen");
                }

                if (!success) {
                    close();
                    return;
                }

                this.name = username;
                manager.addClient(this);
                manager.broadcast(name + " ist dem Chat beigetreten.");
            } else {
                writer.println("ERROR: Bitte zuerst einloggen oder registrieren.");
                close();
                return;
            }

            // == Nachrichten empfangen ==
            String msg;
            while ((msg = reader.readLine()) != null) {
                manager.broadcast(name + ": " + msg);
            }

        } catch (IOException e) {
            System.out.println("Verbindung mit " + name + " unterbrochen.");
        } finally {
            manager.removeClient(this);
            if (name != null) {
                manager.broadcast(name + " hat den Chat verlassen.");
            }
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
