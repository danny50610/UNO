package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.common.UnoColor;
import club.dannyserver.uno.common.User;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayCard implements IPacket {

    private int index;

    private UnoColor pickColor;

    public PacketPlayCard() {
    }

    public PacketPlayCard(int index, UnoColor pickColor) {
        this.index = index;
        this.pickColor = pickColor;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        index = dataInputStream.readInt();

        int colorIndex = dataInputStream.readInt();
        if (colorIndex != -1) {
            this.pickColor = UnoColor.values()[colorIndex];
        }
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(3);
        dataOutputStream.writeInt(index);
        if (pickColor == null) {
            dataOutputStream.writeInt(-1);
        }
        else {
            dataOutputStream.writeInt(pickColor.ordinal());
        }
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        User user = server.userManager.getUser(connectId);

        user.room.playCard(user, index, pickColor);
    }

    @Override
    public void clientHandler(Client client) {
        // nothing
    }
}
