package client;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

public class ChatModel {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread listeningThread;

    /**
     * Stellt eine Verbindung zum Server her und startet sofort den Listener.
     * @param address IP-Adresse oder Hostname
     * @param port Portnummer
     * @param onMessageReceived Callback, der alle empfangenen Nachrichten erhält
     * @throws IOException bei Verbindungsproblemen
     */
    public void connect(String address, int port, Consumer<String> onMessageReceived) throws IOException {
        socket = new Socket(address, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        // Starte sofort den Listener-Thread, damit keine Nachrichten verloren gehen
        listeningThread = new Thread(() -> {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    onMessageReceived.accept(line);
                }
            } catch (IOException e) {
                onMessageReceived.accept("Verbindung zum Server verloren.");
            }
        });
        listeningThread.start();
    }


     //Sendet eine normale Chat-Nachricht an den Server.
    public void sendMessage(String message) {
        writer.println(message);
    }


     //Sendet Login-Daten mit gehashtem Passwort.
    public void sendLogin(String username, String password) {
        String hashed = hashPassword(password);
        writer.println("LOGIN|" + username + "|" + hashed);
    }


     //Sendet Registrierungsdaten mit gehashtem Passwort.
    public void sendRegister(String username, String password) {
        String hashed = hashPassword(password);
        writer.println("REGISTER|" + username + "|" + hashed);
    }


     //Hash-Funktion für Passwörter (SHA-256).

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing fehlgeschlagen", e);
        }
    }


     //Trennt die Verbindung zum Server und schließt alle Streams.
    public void disconnect() throws IOException {
        if (listeningThread != null) listeningThread.interrupt();
        if (socket != null) socket.close();
        if (reader != null) reader.close();
        if (writer != null) writer.close();
    }
}

