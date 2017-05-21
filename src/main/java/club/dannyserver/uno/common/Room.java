package club.dannyserver.uno.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    public static final int MAX_USER = 4;

    private int userCount = 0;

    private User[] users = new User[MAX_USER];

    public boolean isFull() {
        return userCount < MAX_USER;
    }

    public void addUser(User user) {
        user.room = this;
        users[userCount++] = user;

        // TODO: Send userIndex to user

        // TODO: Send notification (userIndex & username) to other user

    }

    /**
     * 產生完整的卡堆，總共 108 張，並隨機打亂
     * @return
     */
    public List<Card> genAllCard() {
        List<Card> result = new ArrayList<>();

        for (UnoColor color : new UnoColor[] {UnoColor.RED, UnoColor.YELLOW, UnoColor.GREEN, UnoColor.BLUE}) {
            result.add(new Card(color, UnoRank.ZERO));

            for (UnoRank rank: UnoRank.values()) {
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
