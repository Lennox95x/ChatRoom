package client;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String ip = JOptionPane.showInputDialog("IP-Adresse des Servers:");
            if (ip == null || ip.isEmpty()) return;

            int port = 3141; // Optional auch abfragbar

            new LoginController(ip, port);
        });
    }
}

