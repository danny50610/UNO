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

    public UnoColor getColor() {
        return color;
    }

    public UnoRank getRank() {
        return rank;
    }

    public boolean isSpecial() {
        return rank == UnoRank.SKIP ||
                rank == UnoRank.DRAW_TWO ||
                rank == UnoRank.REVERSE ||
                rank == UnoRank.WILD ||
                rank == UnoRank.WILD_DRAW_FOUR;
    }

    public static int getCardBackRow() {
        return 0;
    }

    public static int getCardBackCol() {
        return 15;
    }

    public int getCardRow() {
        return color.getRow();
    }

    public int getCardCol() {
        return rank.getCol();
    }
}
