package club.dannyserver.uno.common.packet;


import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.common.Card;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketUpdateRoomInfo implements IPacket {

    private Card centerCard;
    private int userTurnIndex;
    private int turnVector;

    public PacketUpdateRoomInfo() {
    }

    public PacketUpdateRoomInfo(Card centerCard, int userTurnIndex, int turnVector) {
        this.centerCard = centerCard;
        this.userTurnIndex = userTurnIndex;
        this.turnVector = turnVector;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        centerCard = PacketHelper.readCard(dataInputStream);
        userTurnIndex = dataInputStream.readInt();
        turnVector = dataInputStream.readInt();
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(70);
        PacketHelper.writeCard(dataOutputStream, centerCard);
        dataOutputStream.writeInt(userTurnIndex);
        dataOutputStream.writeInt(turnVector);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        // nothing
    }

    @Override
    public void clientHandler(Client client) {
        java.awt.EventQueue.invokeLater(() -> {
            FormGame.PanelGame panelGame = (FormGame.PanelGame) client.getGameFrame().getContentPane();

            panelGame.setRoomInfo(centerCard, userTurnIndex, turnVector);
        });
    }
}
