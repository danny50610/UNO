package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.server.Server;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketRegisterResult implements IPacket {
    public String message;

    public PacketRegisterResult() {}

    public PacketRegisterResult(String message) {
        this.message = message;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        this.message = dataInputStream.readUTF();
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(65);
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
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (message.equals("註冊成功，請到登入頁登入")) {
                    client.switchToLoginFrame();
                }
            }
        });
    }
}
