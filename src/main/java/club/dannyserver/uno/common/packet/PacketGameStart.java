package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketGameStart implements IPacket {

    public PacketGameStart() {
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        // nothing
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(71);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        // nothing
    }

    @Override
    public void clientHandler(Client client) {
        java.awt.EventQueue.invokeLater(() -> {
            FormGame.PanelGame panelGame = (FormGame.PanelGame) client.getGameFrame().getContentPane();

            panelGame.startGame();
        });
    }
}
