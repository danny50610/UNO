package club.dannyserver.uno.client;


import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.client.form.FormLogin;
import club.dannyserver.uno.client.form.FormRegister;
import club.dannyserver.uno.common.packet.IPacket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;

public class Client {

    public static void main(String[] args) {
        String serverIp = "uno.dannyserver.club";
        if (args.length > 0) {
            serverIp = args[0];
        }

        Client client = null;
        try {
            client = new Client(serverIp, 25560);
            client.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final SSLSocket socket;
    private final DataOutputStream dataOutputStream;

    private JFrame activeFrame;

    private JFrame loginFrame;

    private JFrame registerFrame;

    private JFrame gameFrame;

    public JFrame getActiveFrame() {
        return this.activeFrame;
    }

    public JFrame getGameFrame() {
        return gameFrame;
    }

    public Client(String ip, int port) throws Exception {
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = (SSLSocket) sslsocketfactory.createSocket(ip, port);
        this.socket.setEnabledCipherSuites(this.socket.getSupportedCipherSuites());

        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        ClientSocketReadHandler clientSocketReadHandler = new ClientSocketReadHandler(this, socket);
        new Thread(clientSocketReadHandler, "ClientSocketReadHandler").start();

        initLoginFrame();
        initRegisterFrame();
        initGameFrame();
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

    private void initGameFrame() {
        gameFrame = new JFrame("UNO");
        gameFrame.setContentPane(new FormGame(this).getMainPanel());
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
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

    public void switchToGameFrame() {
        activeFrame.setVisible(false);
        this.activeFrame = gameFrame;
        this.activeFrame.setVisible(true);
    }

    public void sendPacket(IPacket packet) {
        try {
            packet.writeToStream(dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
