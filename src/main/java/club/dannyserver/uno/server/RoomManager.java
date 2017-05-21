package club.dannyserver.uno.server;


import club.dannyserver.uno.common.Room;
import club.dannyserver.uno.common.User;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {

    public List<Room> rooms = new ArrayList<>();

    public void addUser(User user) {
        Room room = getLastRoom();

        room.addUser(user);
    }

    private Room getLastRoom() {
        Room result;

        if (rooms.isEmpty()) {
            result = new Room();
            rooms.add(result);
            return result;
        }

        result = rooms.get(rooms.size() - 1);

        if (result.isFull()) {
            result = new Room();
            rooms.add(result);
        }

        return result;
    }

}
