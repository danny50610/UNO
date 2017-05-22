package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.common.Card;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketUpdateCard implements IPacket {

    private List<Card> cards;

    public PacketUpdateCard() {
    }

    public PacketUpdateCard(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        cards = new ArrayList<>();

        int count = dataInputStream.readByte();
        for (int i = 0; i < count; i++) {
            cards.add(PacketHelper.readCard(dataInputStream));
        }
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(69);
        dataOutputStream.writeByte(cards.size());

        for (int i = 0; i < cards.size(); i++) {
            PacketHelper.writeCard(dataOutputStream, cards.get(i));
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

            panelGame.setCards(cards);
        });
    }
}
