package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.common.User;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayCard implements IPacket {

    private int index;

    public PacketPlayCard() {
    }

    public PacketPlayCard(int index) {
        this.index = index;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        index = dataInputStream.readInt();
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(3);
        dataOutputStream.writeInt(index);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        User user = server.userManager.getUser(connectId);

        user.room.playCard(user, index);
    }

    @Override
    public void clientHandler(Client client) {
        // nothing
    }
}
