package club.dannyserver.uno.common;

import java.net.Socket;

public class User {

    public final int id;

    public final Socket socket;

    private String name;

    public User(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }
}
