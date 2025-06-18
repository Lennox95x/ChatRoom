package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
//schnittstelle zwischen Model und view
public class ChatController {
    private ChatModel model;
    private ChatView view;

    public ChatController(ChatModel model, ChatView view) { // beide Objekte werden an den Konstruktor übergeben
        this.model = model;
        this.view = view;

        view.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String message = view.getInputText();
                    if (!message.isEmpty()) {
                        model.sendMessage(message);
                        view.clearInput();
                    }
                }
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });

        view.setVisible(true);
    }

    public void start(String ip, int port) { // Aufbau für die Verbindung
        try {
            model.connect(ip, port, msg -> SwingUtilities.invokeLater(() -> view.appendMessage(msg)));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Verbindung fehlgeschlagen: " + e.getMessage());
            System.exit(1);
        }
    }
}