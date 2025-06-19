package client;

import javax.swing.*;

public class LoginController {
    private final LoginView loginView;
    private final ChatModel model;
    private final String serverIp;
    private final int serverPort;

    public LoginController(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.model = new ChatModel();
        this.loginView = new LoginView();

        loginView.setLoginListener(e -> handleLogin());
        loginView.setRegisterListener(e -> handleRegister());

        loginView.setVisible(true);
    }

    private void handleLogin() {
        String username = loginView.getLoginUsername();
        String password = loginView.getLoginPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Bitte Benutzername und Passwort eingeben.");
            return;
        }

        try {
            model.connect(serverIp, serverPort, msg -> {
                if (msg.equalsIgnoreCase("LOGIN_OK")) {
                    openChat(username);
                } else if (msg.startsWith("ERROR")) {
                    loginView.showMessage("Login fehlgeschlagen: " + msg);
                } else {
                    // nach Login weitere Nachrichten an ChatView senden
                }
            });
            model.sendLogin(username, password);
        } catch (Exception ex) {
            loginView.showMessage("Verbindung fehlgeschlagen: " + ex.getMessage());
        }
    }

    private void handleRegister() {
        String username = loginView.getRegisterUsername();
        String password = loginView.getRegisterPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Bitte Benutzername und Passwort eingeben.");
            return;
        }

        try {
            model.connect(serverIp, serverPort, msg -> {
                if (msg.equalsIgnoreCase("REGISTER_OK")) {
                    openChat(username);
                } else if (msg.startsWith("ERROR")) {
                    loginView.showMessage("Registrierung fehlgeschlagen: " + msg);
                }
            });
            model.sendRegister(username, password);
        } catch (Exception ex) {
            loginView.showMessage("Verbindung fehlgeschlagen: " + ex.getMessage());
        }
    }

    private void openChat(String username) {
        SwingUtilities.invokeLater(() -> {
            ChatView chatView = new ChatView();
            ChatController chatController = new ChatController(model, chatView);
            chatView.appendMessage("Willkommen " + username + "!\n");
            loginView.dispose();
        });
    }
}


