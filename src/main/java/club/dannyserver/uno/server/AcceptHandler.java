package club.dannyserver.uno.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

public class AcceptHandler implements Runnable {

    private final Queue<Runnable> queue;

    private final ServerSocket serverSocket;

    public AcceptHandler(Queue<Runnable> queue, ServerSocket serverSocket) {
        this.queue = queue;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = this.serverSocket.accept();
                queue.add(() -> System.out.println(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
