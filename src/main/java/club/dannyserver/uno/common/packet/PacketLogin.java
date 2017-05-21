package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketLogin implements IPacket {

    public String username;

    public String password;

    public PacketLogin() {}

    public PacketLogin(String username, String password) {
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
        dataOutputStream.writeByte(1);
        dataOutputStream.writeUTF(username);
        dataOutputStream.writeUTF(password);
    }

    @Override
    public void serverHandler(Server server, int connectId) {
        PacketLoginResult packet = (PacketLoginResult) server.userManager.login(connectId, username, password);

        server.sendPacket(connectId, packet);

        // Add User into Room
        if (packet.message.equals("登入成功")) {
            server.roomManager.addUser(server.userManager.getUser(connectId));
        }
    }

    @Override
    public void clientHandler(Client client) {
        // nothing
    }

}
