package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.common.User;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketReAddRoom implements IPacket {

    public PacketReAddRoom() {
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        // nothing
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(4);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        User user = server.userManager.getUser(connectId);

        if (user.room == null) {
            server.roomManager.addUser(user);
        }
    }

    @Override
    public void clientHandler(Client client) {
        // nothing
    }
}
