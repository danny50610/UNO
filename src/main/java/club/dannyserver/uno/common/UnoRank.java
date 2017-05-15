package club.dannyserver.uno.common;

/**
 * 表示 UNO 卡片的功能
 */
public enum UnoRank {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,

    SKIP,          // 禁止，(跳過下一個玩家)
    DRAW_TWO,      // +2
    REVERSE,       // 迴轉
    WILD,          // 指定顏色
    WILD_DRAW_FOUR // +4
}
