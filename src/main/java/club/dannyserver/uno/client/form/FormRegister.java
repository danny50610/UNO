package club.dannyserver.uno.client.form;

import club.dannyserver.uno.client.Client;

import javax.swing.*;


public class FormRegister {
    private JPanel mainPanel;
    private JButton backButton;
    private JButton registerButton;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;

    private Client client;

    public FormRegister(Client client) {
        this.client = client;

        addListener();
    }

    private void addListener() {
        this.backButton.addActionListener(e -> client.switchToLoginFrame());
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }
}
