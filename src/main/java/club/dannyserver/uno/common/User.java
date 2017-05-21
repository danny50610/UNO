package club.dannyserver.uno.common;

import java.net.Socket;

public class User {

    public int connectId;

    public String username;

    public String password;

    public Room room;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }
}
