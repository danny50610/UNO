package club.dannyserver.uno.client;

import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketManager;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientSocketReadHandler implements Runnable {

    private final Client client;

    private final Socket socket;

    public ClientSocketReadHandler(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
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

                packet.clientHandler(client);
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
        java.awt.EventQueue.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    client.getActiveFrame(),
                    "Server 已斷線.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE
            );
            System.exit(-1);
        });
    }
}
