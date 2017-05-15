package club.dannyserver.uno.server;

import java.io.IOException;
import java.net.ServerSocket;
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

    private final ServerSocket serverSocket;

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    public Server(int port) throws Exception {
        this.serverSocket = new ServerSocket(port);
    }

    public void run() throws Exception {
        AcceptHandler acceptHandler = new AcceptHandler(queue, this.serverSocket);

        Thread acceptThread = new Thread(acceptHandler, "AcceptThread");
        acceptThread.start();

        while (true) {
            Runnable runnable = queue.take();
            System.out.println("Get a runnable");
            runnable.run();
        }
    }

    public void close() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
