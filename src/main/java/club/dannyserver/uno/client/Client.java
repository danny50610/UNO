package club.dannyserver.uno.client;


import club.dannyserver.uno.client.form.FormLogin;

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
    }

    private final Socket socket;

    public Client(String ip, int port) throws Exception {
        this.socket = new Socket(ip, port);
    }

    public void run() {
        JFrame frame = new JFrame("UNO Login");
        frame.setContentPane(new FormLogin().getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

}
