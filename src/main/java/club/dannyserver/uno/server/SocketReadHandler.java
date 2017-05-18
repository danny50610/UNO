package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;
import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketManager;

import java.io.DataInputStream;
import java.io.IOException;

public class SocketReadHandler implements Runnable {

    private final Server server;

    private final User user;

    public SocketReadHandler(Server server, User user) {
        this.server = server;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(user.socket.getInputStream());

            while (true) {
                int packetId = dataInputStream.read();

                if (packetId == -1) {
                    break;
                }

                IPacket packet = PacketManager.getPacket(packetId);
                packet.readFromStream(dataInputStream);

                this.server.addJob(server -> packet.serverHandler(server, user));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: dataInputStream close

        // TODO: 斷線處理
        System.out.println("Connect Closed.");
    }
}
