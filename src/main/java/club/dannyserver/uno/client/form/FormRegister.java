package club.dannyserver.uno.client.form;

import club.dannyserver.uno.client.Client;

import javax.swing.*;


public class FormRegister {
    private JPanel mainPanel;
    private JButton backButton;
    private JButton registerButton;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JPasswordField passwordAgainField;

    private Client client;

    public FormRegister(Client client) {
        this.client = client;

        addListener();
    }

    private void addListener() {
        this.backButton.addActionListener(e -> client.switchToLoginFrame());

        this.registerButton.addActionListener(e -> {
            String password = String.valueOf(passwordField.getPassword());
            String passwordAgain = String.valueOf(passwordAgainField.getPassword());

            if (password.equals(passwordAgain)) {
                client.sendRegister(usernameTextField.getText(), password);
            }
            else {
                JOptionPane.showMessageDialog(
                        client.getActiveFrame(),
                        "兩次密碼不一樣，請重新輸入",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }
}
