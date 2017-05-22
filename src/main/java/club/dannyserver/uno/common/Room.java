package club.dannyserver.uno.common;

import club.dannyserver.uno.common.packet.IPacket;
import club.dannyserver.uno.common.packet.PacketUpdateRoomUsername;
import club.dannyserver.uno.common.packet.PacketUserIndex;
import club.dannyserver.uno.server.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Room {
    public static final int MAX_USER = 4;

    private int userCount = 0;

    private User[] users = new User[MAX_USER];

    public boolean isFull() {
        return userCount >= MAX_USER;
    }

    public void addUser(User user) {
        user.room = this;

        // Send userIndex to user
        server.sendPacket(user.connectId, new PacketUserIndex(userCount));

        users[userCount++] = user;

        // Send username list to all user
        String[] username = new String[userCount];
        for (int i = 0; i < userCount; i++) {
            username[i] = users[i].username;
        }

        IPacket packet = new PacketUpdateRoomUsername(username);
        for (int i = 0; i < userCount; i++) {
            server.sendPacket(users[i].connectId, packet);
        }

        if (this.isFull()) {
            initGame();
        }
    }

    private void initGame() {
        userTurnIndex = 0;
        turnVector = 1;
        cards = genAllCard();

        for (int i = 0; i < MAX_USER; i++) {
            users[i].cards.clear();
        }

        // 發牌 (每人7張)
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < MAX_USER; i++) {
                users[i].cards.add(cards.remove(0));
            }
        }

        // TODO: centerCard 決定 centerCard (跳過特別牌)

        // TODO: 更新所有玩家手牌張數

        // TODO: 更新個別玩家手上的手牌

        // TODO: 通知 Game start

        // TODO: 發送 Room Info (centerCard, userTurnIndex, turnVector)
    }

    private Server server;

    public void setServer(Server server) {
        this.server = server;
    }

    private int userTurnIndex = 0;

    private int turnVector = 1;

    private List<Card> cards;

    /**
     * 產生完整的卡堆，總共 108 張，並隨機打亂
     *
     * @return
     */
    public List<Card> genAllCard() {
        List<Card> result = new ArrayList<>();

        for (UnoColor color : new UnoColor[]{UnoColor.RED, UnoColor.YELLOW, UnoColor.GREEN, UnoColor.BLUE}) {
            result.add(new Card(color, UnoRank.ZERO));

            for (UnoRank rank : UnoRank.values()) {
                if (rank == UnoRank.ZERO) continue;
                if (rank == UnoRank.WILD) continue;
                if (rank == UnoRank.WILD_DRAW_FOUR) continue;

                result.add(new Card(color, rank));
                result.add(new Card(color, rank));
            }
        }

        for (int i = 0; i < 4; i++) {
            result.add(new Card(UnoColor.BLACK, UnoRank.WILD));
            result.add(new Card(UnoColor.BLACK, UnoRank.WILD_DRAW_FOUR));
        }

        Collections.shuffle(result);

        return result;
    }
}
