package club.dannyserver.uno.common.packet;


import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.server.Server;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 發送 User 離線通知
 */
public class PacketUserLeave implements IPacket {

    private String username;

    public PacketUserLeave() {
    }

    public PacketUserLeave(String username) {
        this.username = username;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        username = dataInputStream.readUTF();
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(73);
        dataOutputStream.writeUTF(username);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        // nothing
    }

    @Override
    public void clientHandler(Client client) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(
                        client.getActiveFrame(),
                        username + " 已斷線.",
                        "通知",
                        JOptionPane.INFORMATION_MESSAGE
                );

                FormGame.PanelGame panelGame = (FormGame.PanelGame) client.getGameFrame().getContentPane();
                panelGame.resetGame();

                // 發送要求重新加入
                client.sendPacket(new PacketReAddRoom());
            }
        });
    }
}
