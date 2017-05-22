package club.dannyserver.uno.common;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class User {

    public int connectId;

    public String username;

    public String password;

    public Room room;

    // 手牌
    public List<Card> cards = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }
}
