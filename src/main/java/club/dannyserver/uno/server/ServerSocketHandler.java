package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

public class ServerSocketHandler implements Runnable {

    private static int connectId = 1;

    private final Server server;

    private final ServerSocket serverSocket;

    public ServerSocketHandler(Server server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = this.serverSocket.accept();
                server.addJob(server -> {
                    System.out.println("AcceptHandler: " + socket);

                    SocketReadHandler socketReadHandler = new SocketReadHandler(server, socket, connectId++);
                    new Thread(socketReadHandler, "SocketReadHandlerThread : " + socket).start();
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

}
