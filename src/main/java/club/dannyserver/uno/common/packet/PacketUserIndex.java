package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketUserIndex implements IPacket {

    private int userIndex;

    public PacketUserIndex() {}

    public PacketUserIndex(int userIndex) {
        this.userIndex = userIndex;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        userIndex = dataInputStream.readInt();
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(66);
        dataOutputStream.writeInt(userIndex);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        // Nothing
    }

    @Override
    public void clientHandler(Client client) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormGame.PanelGame panelGame = (FormGame.PanelGame) client.getGameFrame().getContentPane();

                panelGame.setUserIndex(userIndex);
            }
        });
    }
}
