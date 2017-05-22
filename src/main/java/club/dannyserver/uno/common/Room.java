package club.dannyserver.uno.common;

import club.dannyserver.uno.common.packet.*;
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
                users[i].cards.add(getNextCard());
            }
        }

        initCenterCard();

        updateCardCount();

        // 更新個別玩家手上的手牌
        for (int i = 0; i < MAX_USER; i++) {
            server.sendPacket(users[i].connectId, new PacketUpdateCard(users[i].cards));
        }

        updateRoomInfo();

        // 通知 Game start
        IPacket packet = new PacketGameStart();
        for (int i = 0; i < MAX_USER; i++) {
            server.sendPacket(users[i].connectId, packet);
        }
    }

    /**
     * 更新所有玩家手牌張數
     */
    private void updateCardCount() {
        int[] cardCount = new int[MAX_USER];
        for (int i = 0; i < MAX_USER; i++) {
            cardCount[i] = users[i].cards.size();
        }
        IPacket packet = new PacketUpdateCardCount(cardCount);
        for (int i = 0; i < MAX_USER; i++) {
            server.sendPacket(users[i].connectId, packet);
        }
    }

    /**
     * 發送 Room Info (centerCard, userTurnIndex, turnVector)
     */
    private void updateRoomInfo() {
        IPacket packet = new PacketUpdateRoomInfo(centerCard, userTurnIndex, turnVector);
        for (int i = 0; i < MAX_USER; i++) {
            server.sendPacket(users[i].connectId, packet);
        }
    }

    private void initCenterCard() {
        while (true) {
            centerCard = getNextCard();

            // 跳過特別牌
            if (!centerCard.isSpecial()) {
                break;
            }
        }
    }

    private Server server;

    public void setServer(Server server) {
        this.server = server;
    }

    private int userTurnIndex = 0;

    private int turnVector = 1;

    private int drawTwoCount = 0;

    private int wildDrawFourCount = 0;

    private Card centerCard;

    // 如果玩家出黑色的，所選擇的
    private UnoColor color;

    private List<Card> cards;

    public Card getNextCard() {
        if (cards.isEmpty()) {
            cards = genAllCard();
        }

        return cards.remove(0);
    }

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

    public void playCard(User user, int cardIndex) {
        int userIndex = -1;
        for (int i = 0; i < MAX_USER; i++) {
            if (users[i].username.equals(user.username)) {
                userIndex = i;
                break;
            }
        }

        // 此玩家不在此房
        if (userIndex == -1) return;

        // 不是此玩家的回合
        if (userIndex != userTurnIndex) return;

        // 牌不存在
        if (!(0 <= cardIndex && cardIndex < user.cards.size())) return;

        Card card = user.cards.get(cardIndex);

        // 是否能出牌
        if (!centerCard.canPlayedBy(card, color)) return;

        // 如果 +2 效果未消失，則只能出 +2
        if (drawTwoCount > 0) {
            if (card.getRank() != UnoRank.DRAW_TWO) return;
        }

        // 如果 +4 效果未消失，則只能出 +4
        if (wildDrawFourCount > 0) {
            if (card.getRank() != UnoRank.WILD_DRAW_FOUR) return;
        }

        // 出牌
        user.cards.remove(cardIndex);
        centerCard = card;

        // TODO: 勝利檢查 & Room 更新

        if (card.getColor() == UnoColor.BLACK) {
            // TODO: update color
            //this.color
        }

        boolean canNextUserPlay = true;

        // 發動牌的效果
        if (centerCard.getRank() == UnoRank.REVERSE) {
            turnVector *= -1;
        }
        else if (centerCard.getRank() == UnoRank.SKIP) {
            canNextUserPlay = false;
        }
        else if (centerCard.getRank() == UnoRank.WILD) {
            // nothing
        }
        else if (centerCard.getRank() == UnoRank.DRAW_TWO) {
            drawTwoCount += 1;
        }
        else if (centerCard.getRank() == UnoRank.WILD_DRAW_FOUR) {
            wildDrawFourCount += 1;
        }

        // 換下一位玩家
        userTurnIndex += turnVector;

        if (!canNextUserPlay) {
            userTurnIndex += turnVector;
        }

        userTurnIndex = (userTurnIndex + 4) % 4;

        User nextUser = users[userTurnIndex];

        // 抽排 (如果需要的話)
        if (drawTwoCount > 0) {
            // 檢查下一位玩家是否能出 +2
            boolean canPlayDrawTwo = false;
            for (Card localCard : nextUser.cards) {
                if (localCard.getRank() == UnoRank.DRAW_TWO) {
                    canPlayDrawTwo = true;
                }
            }

            if (!canPlayDrawTwo) {
                // 不能出 +2 ，抽牌並換下一位
                for (int i = 0; i < drawTwoCount * 2; i++) {
                    nextUser.cards.add(getNextCard());
                }
                drawTwoCount = 0;

                userTurnIndex += turnVector;
                userTurnIndex = (userTurnIndex + 4) % 4;
            }
        }

        if (wildDrawFourCount > 0) {
            // 檢查下一位玩家是否能出 +4
            boolean canPlayWildDrawFour = false;
            for (Card localCard : nextUser.cards) {
                if (localCard.getRank() == UnoRank.WILD_DRAW_FOUR) {
                    canPlayWildDrawFour = true;
                }
            }

            if (!canPlayWildDrawFour) {
                // 不能出 +4 ，抽牌並換下一位
                for (int i = 0; i < wildDrawFourCount * 4; i++) {
                    nextUser.cards.add(getNextCard());
                }
                wildDrawFourCount = 0;

                userTurnIndex += turnVector;
                userTurnIndex = (userTurnIndex + 4) % 4;
            }
        }

        // 檢查下一位玩家 (或是下下一位)
        nextUser = users[userTurnIndex];

        boolean canPlay = false;
        for (Card localCard : nextUser.cards) {
            if (centerCard.canPlayedBy(localCard, color)) {
                canPlay = true;
            }
        }

        if (!canPlay) {
            Card newCard = getNextCard();

            nextUser.cards.add(newCard);

            // 檢查新的牌是否能出，不能跳下一位
            if (!centerCard.canPlayedBy(newCard, color)) {
                userTurnIndex += turnVector;
                userTurnIndex = (userTurnIndex + 4) % 4;
            }
        }

        // 更新資訊
        updateCardCount();

        // 更新個別玩家手上的手牌
        for (int i = 0; i < MAX_USER; i++) {
            server.sendPacket(users[i].connectId, new PacketUpdateCard(users[i].cards));
        }

        updateRoomInfo();
    }
}
