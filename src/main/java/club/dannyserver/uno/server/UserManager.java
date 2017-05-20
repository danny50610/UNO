package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;
import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketLoginResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {

    private Map<Integer, User> id2User = new HashMap<>();

    private Map<Integer, Integer> connectId2Id = new HashMap<>();

    public UserManager(String userFilename) {
        // load user
        try {
            List<String> lines = Files.readAllLines(Paths.get(userFilename), StandardCharsets.UTF_8);

            for (String line : lines) {
                // id,username,password
                String[] token = line.split(",");

                User user = new User(token[1], token[2]);
                id2User.put(Integer.valueOf(token[0]), user);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public IPacket login(String username, String password) {
        int userId = findUserId(username);
        User user = id2User.get(userId);
        if (user == null) {
            return new PacketLoginResult("不存在此使用者");
        }

        if (user.login(password)) {
            return new PacketLoginResult("登入成功");
        }
        else {
            return new PacketLoginResult("密碼錯誤");
        }
    }

    private int findUserId(String username) {
        for (Map.Entry<Integer, User> entry : id2User.entrySet()) {
            if (entry.getValue().username.equals(username)) {
                return entry.getKey();
            }
        }

        return -1;
    }

}
