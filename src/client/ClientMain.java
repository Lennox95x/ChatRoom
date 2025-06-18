package client;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String ip = JOptionPane.showInputDialog("IP-Adresse des Servers:");
            if (ip == null || ip.isEmpty()) return;

            int port = 3141; // oder von Nutzer eingeben lassen

            ChatModel model = new ChatModel();
            ChatView view = new ChatView();
            ChatController controller = new ChatController(model, view);
            controller.start(ip, port);
        });
    }
}
