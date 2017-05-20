package club.dannyserver.uno.client;


import club.dannyserver.uno.client.form.FormLogin;
import club.dannyserver.uno.client.form.FormRegister;
import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketLogin;
import club.dannyserver.uno.common.packet.PacketRegister;

import javax.swing.*;
import java.io.DataOutputStream;
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
    private final DataOutputStream dataOutputStream;

    private JFrame activeFrame;

    public JFrame loginFrame;

    private JFrame registerFrame;

    public Client(String ip, int port) throws Exception {
        this.socket = new Socket(ip, port);
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        ClientSocketReadHandler clientSocketReadHandler = new ClientSocketReadHandler(this, socket);
        new Thread(clientSocketReadHandler, "ClientSocketReadHandler").start();

        initLoginFrame();
        initRegisterFrame();
    }

    private void initLoginFrame() {
        loginFrame = new JFrame("UNO Login");
        loginFrame.setContentPane(new FormLogin(this).getMainPanel());
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
    }

    private void initRegisterFrame() {
        registerFrame = new JFrame("UNO Register");
        registerFrame.setContentPane(new FormRegister(this).getMainPanel());
        registerFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        registerFrame.setResizable(false);
        registerFrame.pack();
        registerFrame.setLocationRelativeTo(null);
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

    public void sendLogin(String username, String password) {
        IPacket packet = new PacketLogin(username, password);

        try {
            packet.writeToStream(dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRegister(String username, String password) {
        IPacket packet = new PacketRegister(username, password);

        try {
            packet.writeToStream(dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
