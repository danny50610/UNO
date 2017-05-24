package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
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

                int reply = JOptionPane.showConfirmDialog(
                        client.getActiveFrame(),
                        "是否還要再玩一局？",
                        "Question",
                        JOptionPane.YES_NO_OPTION
                );

                if (reply == JOptionPane.YES_OPTION) {
                    FormGame.PanelGame panelGame = (FormGame.PanelGame) client.getGameFrame().getContentPane();
                    panelGame.resetGame();

                    // 發送要求重新加入
                    client.sendPacket(new PacketReAddRoom());
                }
                else {
                    System.exit(0);
                }
            }
        });
    }
}
