package club.dannyserver.uno.common.packet;


import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketUpdateCardCount implements IPacket {

    private int[] cardCount;

    public PacketUpdateCardCount() {}

    public PacketUpdateCardCount(int[] cardCount) {
        this.cardCount = cardCount;

        assert cardCount.length == 4;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        cardCount = new int[4];
        for (int i = 0; i < 4; i++) {
            cardCount[i] = dataInputStream.readInt();
        }
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(68);
        for (int i = 0; i < 4; i++) {
            dataOutputStream.writeInt(cardCount[i]);
        }
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        // nothing
    }

    @Override
    public void clientHandler(Client client) {
        java.awt.EventQueue.invokeLater(() -> {
            FormGame.PanelGame panelGame = (FormGame.PanelGame) client.getGameFrame().getContentPane();

            panelGame.setCardCount(cardCount);
        });
    }
}
