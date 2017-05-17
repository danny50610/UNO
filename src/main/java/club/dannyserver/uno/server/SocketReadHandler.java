package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;
import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public class SocketReadHandler implements Runnable {

    private final Queue<IJob> queue;

    private final Socket socket;

    public SocketReadHandler(Queue<IJob> queue, User user) {
        this.queue = queue;
        this.socket = user.socket;
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

                queue.add(new ReceivePacketHandler(packet));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: dataInputStream close

        // TODO: 斷線處理
        System.out.println("Connect Closed.");
    }

    public static class ReceivePacketHandler implements IJob {

        private IPacket packet;

        public ReceivePacketHandler(IPacket packet) {
            this.packet = packet;
        }

        @Override
        public void run(Server server) {
            packet.serverHandler(server);
        }
    }
}
