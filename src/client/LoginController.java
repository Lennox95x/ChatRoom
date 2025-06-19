package client;

import javax.swing.*;

public class LoginController {
    private final LoginView loginView;
    private final ChatModel model;
    private final String serverIp;
    private final int serverPort;
    private boolean authenticated = false;

    public LoginController(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.model = new ChatModel();
        this.loginView = new LoginView();

        // Listener für Login und Registrierung setzen
        loginView.setLoginListener(e -> handleLogin());
        loginView.setRegisterListener(e -> handleRegister());

        // Verbindung zum Server herstellen + globaler Listener
        try {
            model.connect(serverIp, serverPort, this::handleServerMessage);
        } catch (Exception ex) {
            loginView.showMessage("Verbindung fehlgeschlagen: " + ex.getMessage());
        }

        loginView.setVisible(true);
    }

    private void handleLogin() {
        String username = loginView.getLoginUsername();
        String password = loginView.getLoginPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Bitte Benutzername und Passwort eingeben.");
            return;
        }

        model.sendLogin(username, password);
    }

    private void handleRegister() {
        String username = loginView.getRegisterUsername();
        String password = loginView.getRegisterPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Bitte Benutzername und Passwort eingeben.");
            return;
        }

        model.sendRegister(username, password);
    }

    private void handleServerMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            if (msg.equalsIgnoreCase("LOGIN_OK") || msg.equalsIgnoreCase("REGISTER_OK")) {
                if (!authenticated) {
                    authenticated = true; // nur einmal
                    openChat();
                }
            } else if (msg.startsWith("ERROR")) {
                loginView.showMessage(msg);
            } else if (authenticated) {
                // reguläre Chat-Nachrichten nach Login
                ChatController.forwardMessage(msg);
            }
        });
    }

    private void openChat() {
        ChatView chatView = new ChatView();
        ChatController chatController = new ChatController(model, chatView);
        ChatController.setInstance(chatController); // zur Weiterleitung von Nachrichten
        chatView.appendMessage("Willkommen im Chat!\n");
        loginView.dispose();
    }
}



