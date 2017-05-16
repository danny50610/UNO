package club.dannyserver.uno.client;


import club.dannyserver.uno.client.form.FormLogin;
import club.dannyserver.uno.client.form.FormRegister;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client("127.0.0.1", 25560);
            client.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (client != null) {
                client.close();
            }
        }
    }

    private final Socket socket;

    private JFrame activeFrame;

    private JFrame loginFrame;

    private JFrame registerFrame;

    public Client(String ip, int port) throws Exception {
        this.socket = new Socket(ip, port);

        initLoginFrame();
        initRegisterFrame();
    }

    private void initLoginFrame() {
        loginFrame = new JFrame("UNO Login");
        loginFrame.setContentPane(new FormLogin(this).getMainPanel());
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        loginFrame.pack();
    }

    private void initRegisterFrame() {
        registerFrame = new JFrame("UNO Register");
        registerFrame.setContentPane(new FormRegister(this).getMainPanel());
        registerFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        registerFrame.setResizable(false);
        registerFrame.pack();
    }

    public void run() {
        this.activeFrame = loginFrame;
        this.activeFrame.setVisible(true);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToLoginFrame() {
        activeFrame.setVisible(false);
        this.activeFrame = loginFrame;
        this.activeFrame.setVisible(true);
    }

    public void switchToRegisterFrame() {
        activeFrame.setVisible(false);
        this.activeFrame = registerFrame;
        this.activeFrame.setVisible(true);
    }

}
