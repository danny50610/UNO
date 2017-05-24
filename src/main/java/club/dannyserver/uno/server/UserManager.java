package club.dannyserver.uno.server;

import club.dannyserver.uno.common.User;
import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketLoginResult;
import club.dannyserver.uno.common.packet.PacketRegisterResult;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {

    private static int USER_ID = 1;

    private String userFilename;

    private Map<Integer, User> id2User = new HashMap<>();

    private Map<Integer, Integer> connectId2Id = new HashMap<>();

    public UserManager(String userFilename) {
        this.userFilename = userFilename;

        loadUserList();
    }

    public IPacket login(int connectId, String username, String password) {
        int userId = findUserId(username);
        User user = id2User.get(userId);
        if (user == null) {
            return new PacketLoginResult("不存在此使用者");
        }

        if (user.login(password)) {
            connectId2Id.put(connectId, userId);

            return new PacketLoginResult("登入成功");
        }
        else {
            return new PacketLoginResult("密碼錯誤");
        }
    }

    public IPacket register(String username, String password) {
        int userId = findUserId(username);
        User user = id2User.get(userId);
        if (user != null) {
            return new PacketRegisterResult(username + "已被使用");
        }

        user = new User(username, BCrypt.hashpw(password, BCrypt.gensalt()));
        id2User.put(USER_ID++, user);

        saveUserList();

        return new PacketRegisterResult("註冊成功，請回登入頁登入");
    }

    public User getUser(int connectId) {
        return id2User.get(connectId2Id.get(connectId));
    }

    private int findUserId(String username) {
        for (Map.Entry<Integer, User> entry : id2User.entrySet()) {
            if (entry.getValue().username.equals(username)) {
                return entry.getKey();
            }
        }

        return -1;
    }

    private void loadUserList() {
        File f = new File(userFilename);
        if (!f.exists() || f.isDirectory()) {
            System.out.println(userFilename + " 不存在. 跳過玩家資料讀取");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(userFilename), StandardCharsets.UTF_8);

            for (String line : lines) {
                // username,passwordHashed
                String[] token = line.split(",");

                User user = new User(token[0], token[1]);
                id2User.put(USER_ID++, user);
            }

            this.userFilename = userFilename;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void saveUserList() {
        List<String> lines = new ArrayList<>();
        for (User user : id2User.values()) {
            lines.add(String.format("%s,%s", user.username, user.passwordHashed));
        }

        Path file = Paths.get(userFilename);
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
