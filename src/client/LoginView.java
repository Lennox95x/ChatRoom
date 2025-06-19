package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameFieldLogin = new JTextField(15);
    private JPasswordField passwordFieldLogin = new JPasswordField(15);

    private JTextField usernameFieldRegister = new JTextField(15);
    private JPasswordField passwordFieldRegister = new JPasswordField(15);

    private JButton loginButton = new JButton("Anmelden");
    private JButton registerButton = new JButton("Registrieren");

    public LoginView() {
        setTitle("Login / Registrierung");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        loginPanel.add(new JLabel("Benutzername:"));
        loginPanel.add(usernameFieldLogin);
        loginPanel.add(new JLabel("Passwort:"));
        loginPanel.add(passwordFieldLogin);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);

        // Register Panel
        JPanel registerPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        registerPanel.add(new JLabel("Benutzername:"));
        registerPanel.add(usernameFieldRegister);
        registerPanel.add(new JLabel("Passwort:"));
        registerPanel.add(passwordFieldRegister);
        registerPanel.add(new JLabel(""));
        registerPanel.add(registerButton);

        tabs.addTab("Login", loginPanel);
        tabs.addTab("Registrieren", registerPanel);

        add(tabs);
    }

    // Zugriffsmethoden
    public String getLoginUsername() {
        return usernameFieldLogin.getText();
    }

    public String getLoginPassword() {
        return new String(passwordFieldLogin.getPassword());
    }

    public String getRegisterUsername() {
        return usernameFieldRegister.getText();
    }

    public String getRegisterPassword() {
        return new String(passwordFieldRegister.getPassword());
    }

    public void setLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void setRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}

