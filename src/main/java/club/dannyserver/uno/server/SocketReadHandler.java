package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;
import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketReadHandler implements Runnable {

    private final Server server;

    private final Socket socket;

    public SocketReadHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            while (true) {
                int packetId = dataInputStream.read();

                if (packetId == -1) {
                    break;
                }

                IPacket packet = PacketManager.getPacket(packetId);
                packet.readFromStream(dataInputStream);

                this.server.addJob(server -> packet.serverHandler(server));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: dataInputStream close

        // TODO: 斷線處理
        System.out.println("Connect Closed.");
    }
}
