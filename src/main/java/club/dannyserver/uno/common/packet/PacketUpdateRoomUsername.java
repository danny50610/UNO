package club.dannyserver.uno.common.packet;


import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketUpdateRoomUsername implements IPacket {

    private String[] username;

    public PacketUpdateRoomUsername() {}

    public PacketUpdateRoomUsername(String[] username) {
        this.username = username;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        int count = dataInputStream.readByte();
        username = new String[4];
        for (int i = 0; i < count; i++) {
            username[i] = dataInputStream.readUTF();
        }
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(67);
        dataOutputStream.writeByte(username.length);
        for (int i = 0; i < username.length; i++) {
            dataOutputStream.writeUTF(username[i]);
        }
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        // nothing
    }

    @Override
    public void clientHandler(Client client) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormGame.PanelGame panelGame = (FormGame.PanelGame) client.getGameFrame().getContentPane();

                panelGame.setUsernames(username);
            }
        });
    }
}
