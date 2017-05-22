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

    /**
     * 是否可以被覆蓋
     * @param card
     * @param color
     * @return
     */
    public boolean canPlayedBy(Card card, UnoColor color) {
        boolean isSameRank = this.rank == card.rank;

        UnoColor thisColor = this.color;
        if (thisColor == UnoColor.BLACK) {
            thisColor = color;
        }

        boolean isColorPass = false;
        if (card.color == UnoColor.BLACK) {
            isColorPass = true;
        }
        else {
            isColorPass = (thisColor == card.color);
        }

        return isColorPass || isSameRank;
    }
}
