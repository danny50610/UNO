package danny50610.uno.client;


import danny50610.uno.client.form.FormLogin;

import javax.swing.*;

public class Client {

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public Client() {
        //
    }

    public void run() {
        JFrame frame = new JFrame("UNO");
        frame.setContentPane(new FormLogin().getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

}
