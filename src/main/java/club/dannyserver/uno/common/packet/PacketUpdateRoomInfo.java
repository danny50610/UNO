package club.dannyserver.uno.common.packet;


import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.client.form.FormGame;
import club.dannyserver.uno.common.Card;
import club.dannyserver.uno.common.UnoColor;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketUpdateRoomInfo implements IPacket {

    private Card centerCard;
    private int userTurnIndex;
    private int turnVector;
    private UnoColor color;

    public PacketUpdateRoomInfo() {
    }

    public PacketUpdateRoomInfo(Card centerCard, int userTurnIndex, int turnVector, UnoColor color) {
        this.centerCard = centerCard;
        this.userTurnIndex = userTurnIndex;
        this.turnVector = turnVector;
        this.color = color;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        centerCard = PacketHelper.readCard(dataInputStream);
        userTurnIndex = dataInputStream.readInt();
        turnVector = dataInputStream.readInt();

        int colorIndex = dataInputStream.readInt();
        if (colorIndex != -1) {
            this.color = UnoColor.values()[colorIndex];
        }
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(70);
        PacketHelper.writeCard(dataOutputStream, centerCard);
        dataOutputStream.writeInt(userTurnIndex);
        dataOutputStream.writeInt(turnVector);
        if (color == null) {
            dataOutputStream.writeInt(-1);
        }
        else {
            dataOutputStream.writeInt(color.ordinal());
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

            panelGame.setRoomInfo(centerCard, userTurnIndex, turnVector, color);
        });
    }
}
