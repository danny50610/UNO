package club.dannyserver.uno.common;

import org.mindrot.jbcrypt.BCrypt;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class User {

    public int connectId;

    public String username;

    public String passwordHashed;

    public Room room;

    // 手牌
    public List<Card> cards = new ArrayList<>();

    public User(String username, String passwordHashed) {
        this.username = username;
        this.passwordHashed = passwordHashed;
    }

    public boolean login(String password) {
        return BCrypt.checkpw(password, passwordHashed);
    }
}
