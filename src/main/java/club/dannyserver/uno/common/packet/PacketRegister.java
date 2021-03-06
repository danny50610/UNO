package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketRegister implements IPacket {

    public String username;

    public String password;

    public PacketRegister() {}

    public PacketRegister(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void readFromStream(DataInputStream dataInputStream) throws IOException {
        username = dataInputStream.readUTF();
        password = dataInputStream.readUTF();
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(2);
        dataOutputStream.writeUTF(username);
        dataOutputStream.writeUTF(password);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        IPacket packet = server.userManager.register(username, password);

        server.sendPacket(connectId, packet);
    }

    @Override
    public void clientHandler(Client client) {

    }
}
