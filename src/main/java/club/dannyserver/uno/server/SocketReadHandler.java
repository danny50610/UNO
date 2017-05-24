package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;
import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class SocketReadHandler implements Runnable {

    private final Server server;

    private final Socket socket;

    private final int connectId;

    public SocketReadHandler(Server server, Socket socket, int connectId) {
        this.server = server;
        this.socket = socket;
        this.connectId = connectId;

        server.addSocket(connectId, socket);
    }

    @Override
    public void run() {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());

            while (true) {
                int packetId = dataInputStream.read();

                if (packetId == -1) {
                    break;
                }

                IPacket packet = PacketManager.getPacket(packetId);
                packet.readFromStream(dataInputStream);

                this.server.addJob(server -> packet.serverHandler(server, connectId));
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage() + ". " + socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 斷線處理
        this.server.addJob(server -> {
            User user = server.userManager.getUser(connectId);

            if (user != null) {
                server.roomManager.handleUserLeave(user);
            }
        });
    }
}
