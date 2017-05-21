package club.dannyserver.uno.server;


import club.dannyserver.uno.common.Room;
import club.dannyserver.uno.common.User;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {

    public List<Room> rooms = new ArrayList<>();

    public void addUser(User user) {
        Room room = rooms.get(rooms.size() - 1);

        if (room.isFull()) {
            room = new Room();
            rooms.add(room);
        }

        room.addUser(user);
    }

}
