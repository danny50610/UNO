package club.dannyserver.uno.common;

import java.net.Socket;

public class User {

    public final Socket socket;

    private String username;

    private String password;

    public User(Socket socket) {
        this.socket = socket;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }
}
