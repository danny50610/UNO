package club.dannyserver.uno.common;

public class Room {
    public static final int MAX_USER = 4;

    private int userCount = 0;

    private User[] users = new User[MAX_USER];

    public boolean isFull() {
        return userCount < MAX_USER;
    }

    public void addUser(User user) {
        users[userCount++] = user;

        user.room = this;
    }
}
