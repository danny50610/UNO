package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.server.Server;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketWinResult implements IPacket {

    private String message;

    public PacketWinResult() {
    }

    public PacketWinResult(String message) {
        this.message = message;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        this.message = dataInputStream.readUTF();
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(72);
        dataOutputStream.writeUTF(message);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        // Nothing
    }

    @Override
    public void clientHandler(Client client) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(
                        client.getActiveFrame(),
                        message,
                        "通知",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }
}
