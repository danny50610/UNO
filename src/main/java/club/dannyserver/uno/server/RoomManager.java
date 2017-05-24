package club.dannyserver.uno.server;


import club.dannyserver.uno.common.Room;
import club.dannyserver.uno.common.User;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {

    private Server server;

    public void setServer(Server server) {
        this.server = server;
    }

    public List<Room> rooms = new ArrayList<>();

    public void addUser(User user) {
        Room room = getLastRoom();

        room.addUser(user);
    }

    public void handleUserLeave(User user) {
        if (user.room == null) return;

        user.room.handleUserLeave(user);
    }

    private Room getLastRoom() {
        Room result;

        if (rooms.isEmpty()) {
            result = new Room();
            result.setServer(server);
            rooms.add(result);
            return result;
        }

        result = rooms.get(rooms.size() - 1);

        if (result.isFull()) {
            result = new Room();
            result.setServer(server);
            rooms.add(result);
        }

        return result;
    }

}
