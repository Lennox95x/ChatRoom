package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class ChatView extends JFrame {
    private JTextArea chatArea = new JTextArea();
    private JTextField inputField = new JTextField();

    public ChatView() {
        super("Chat Client");
        chatArea.setEditable(false); //Die Anzeige für den Text soll nur gelesen werden können.
        chatArea.setBorder(BorderFactory.createTitledBorder("Chat"));
        inputField.setBorder(BorderFactory.createTitledBorder("Nachricht eingeben"));

        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void addKeyListener(KeyListener listener) {
        inputField.addKeyListener(listener);        //Für das Abschicken über enter taste
    }

    public String getInputText() {
        return inputField.getText();    //Auslesen des Eingegebenen textes
    }

    public void clearInput() {
        inputField.setText(""); //Nach senden das Textfeld Leeren
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }
}

