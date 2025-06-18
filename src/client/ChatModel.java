package client;
import java.io.*;                      // Für Input/Output-Streams
import java.net.Socket;               // Für die TCP-Verbindung
import java.util.function.Consumer;   // Für Callback-Funktion zur Nachrichtenverarbeitung

//Ist für die Verwaltung der Kommunikation zuständig
public class ChatModel {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread listeningThread;  //Der Client benötigt mehrere Threads damit Gui und Nachrichten schreiben und erhalten möglich sind ohne Wartezeiten zu erzeugen

    //Methode um Client mit Server zu verbinden
    public void connect(String address, int port, Consumer<String> onMessageReceived) throws IOException {
        socket = new Socket(address, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

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

    public void sendMessage(String message) {
        writer.println(message);
    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
        if (reader != null) reader.close();
        if (writer != null) writer.close();
    }
}
