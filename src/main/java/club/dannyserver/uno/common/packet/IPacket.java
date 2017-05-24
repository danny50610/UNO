package club.dannyserver.uno.common.packet;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IPacket {
    public void readFromStream(DataInputStream dataInputStream) throws IOException;

    public void writeToStream(DataOutputStream dataOutputStream) throws IOException;

    public void serverHandler(Server server, int connectId);

    public void clientHandler(Client client);
}
