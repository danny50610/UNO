package club.dannyserver.uno.client.form;

import club.dannyserver.uno.client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormLogin {
    private JPanel mainPanel;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JLabel message;

    private Client client;

    public FormLogin(Client client) {
        this.client = client;

        addListener();
    }

    private void addListener() {
        this.registerButton.addActionListener(e -> client.switchToRegisterFrame());
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }
}
