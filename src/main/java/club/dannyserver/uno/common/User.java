package club.dannyserver.uno.common;

import java.net.Socket;

public class User {

    public  Socket socket;

    public String username;

    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }
}
