package club.dannyserver.uno.common;

/**
 *  UNO 卡片
 */
public class Card {

    private final UnoColor color;

    private final UnoRank rank;

    public Card(UnoColor color, UnoRank rank) {
        this.color = color;
        this.rank = rank;
    }

}
