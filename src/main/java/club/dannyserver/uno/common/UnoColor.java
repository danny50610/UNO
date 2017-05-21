package club.dannyserver.uno.common;

/**
 * 表示 UNO 卡片的顏色
 */
public enum UnoColor {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    BLACK;

    private int[] rowTable = new int[] {0, 2, 3, 1, 0};

    public int getRow() {
        return rowTable[ordinal()];
    }
}
