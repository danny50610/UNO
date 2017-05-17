package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

public class ServerSocketHandler implements Runnable {

    private final Queue<IJob> queue;

    private final ServerSocket serverSocket;

    public ServerSocketHandler(Queue<IJob> queue, ServerSocket serverSocket) {
        this.queue = queue;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = this.serverSocket.accept();
                queue.add(new AcceptHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class AcceptHandler implements IJob {

        private Socket socket;

        public AcceptHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run(Server server) {
            System.out.println("AcceptHandler: " + socket);
            server.addUser(socket);
        }
    }
}
