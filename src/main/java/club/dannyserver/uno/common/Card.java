package club.dannyserver.uno.common;

/**
 * UNO 卡片
 */
public class Card {

    public static final int WIDTH = 240;

    public static final int HEIGHT = 360;

    private final UnoColor color;

    private final UnoRank rank;

    public Card(UnoColor color, UnoRank rank) {
        this.color = color;
        this.rank = rank;
    }

    public static int getCardBackRow() {
        return 0;
    }

    public static int getCardBackCol() {
        return 15;
    }
}
