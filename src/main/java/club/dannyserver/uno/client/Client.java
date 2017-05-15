package club.dannyserver.uno.client;


import club.dannyserver.uno.client.form.FormLogin;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        // Test
        try {
            Socket socket = new Socket("127.0.0.1", 25560);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Client client = new Client();
        client.run();
    }

    public Client() {
        //
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
