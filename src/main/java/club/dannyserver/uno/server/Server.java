package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

    private final BlockingQueue<IJob> queue = new LinkedBlockingQueue<>();

    public Server(int port) throws Exception {
        this.serverSocket = new ServerSocket(port);
    }

    public void run() throws Exception {
        ServerSocketHandler serverSocketHandler = new ServerSocketHandler(this, this.serverSocket);

        new Thread(serverSocketHandler, "ServerSocketHandlerThread").start();

        System.out.println("Server started...");
        while (true) {
            IJob job = queue.take();
            System.out.println("Run a runnable");
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

    public void login(String username, String password) {

    }



}
