package client;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
 //Schnittstelle zwischen View und Model
public class ChatController {
    private final ChatModel model;
    private final ChatView view;
     private static ChatController instance;

    public ChatController(ChatModel model, ChatView view) { //Beide Objekte werden an den Controller übergeben damit der Controller die verbindung verwalten kann
        this.model = model;
        this.view = view;
        setupListeners();
        view.setVisible(true);
    }



     public static void setInstance(ChatController controller) {
         instance = controller;
     }

     public static void forwardMessage(String msg) {
         if (instance != null && instance.view != null) {
             instance.view.appendMessage(msg);
         }
     }

    public void setupListeners() {
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
    }
}
