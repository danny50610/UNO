package danny50610.uno.client;


import danny50610.uno.client.form.FormLogin;

import javax.swing.*;

public class Client {

    public static void main(String[] args) {
        JFrame frame = new JFrame("UNO");
        frame.setContentPane(new FormLogin().getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
