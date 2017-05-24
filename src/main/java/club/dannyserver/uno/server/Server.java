package club.dannyserver.uno.server;

import club.dannyserver.uno.common.packet.IPacket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server(25560);
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                server.close();
            }
        }
    }

    private final SSLServerSocket serverSocket;

    private final BlockingQueue<IJob> queue = new LinkedBlockingQueue<>();

    public final UserManager userManager;

    public final RoomManager roomManager;

    public Map<Integer, Socket> connectId2Socket = new HashMap<>();

    public Server(int port) throws Exception {
        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        this.serverSocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(port);

        Files.createDirectories(Paths.get("data"));

        userManager = new UserManager("data/user.txt");
        roomManager = new RoomManager();
        roomManager.setServer(this);
    }

    public void run() throws Exception {
        ServerSocketHandler serverSocketHandler = new ServerSocketHandler(this, this.serverSocket);

        new Thread(serverSocketHandler, "ServerSocketHandlerThread").start();

        System.out.println("Server started...");
        while (true) {
            IJob job = queue.take();
            job.run(this);
        }
    }

    public void close() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addJob(IJob job) {
        this.queue.add(job);
    }

    public void addSocket(int connectId, Socket socket) {
        connectId2Socket.put(connectId, socket);
    }

    public void sendPacket(int connectId, IPacket packet) {
        Socket socket = connectId2Socket.get(connectId);

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            packet.writeToStream(dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
