package club.dannyserver.uno.client;

import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSocketReadHandler implements Runnable {

    private final Client client;

    private final Socket socket;

    public ClientSocketReadHandler(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dataInputStream;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());

            while (true) {
                int packetId = dataInputStream.read();

                if (packetId == -1) {
                    break;
                }

                IPacket packet = PacketManager.getPacket(packetId);
                packet.readFromStream(dataInputStream);

                packet.clientHandler(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: dataInputStream close

        // TODO: 斷線處理
        System.out.println("Connect Closed.");
    }
}
