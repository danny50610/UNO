package club.dannyserver.uno.common.packet;


import club.dannyserver.uno.common.Card;
import club.dannyserver.uno.common.UnoColor;
import club.dannyserver.uno.common.UnoRank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketHelper {

    public static Card readCard(DataInputStream dataInputStream) throws IOException {
        int color = dataInputStream.readByte();
        int rank = dataInputStream.readByte();

        return new Card(UnoColor.values()[color], UnoRank.values()[rank]);
    }

    public static void writeCard(DataOutputStream dataOutputStream, Card card) throws IOException {
        dataOutputStream.writeByte(card.getColor().ordinal());
        dataOutputStream.writeByte(card.getRank().ordinal());
    }
}
