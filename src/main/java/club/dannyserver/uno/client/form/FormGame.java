package club.dannyserver.uno.client.form;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.common.Card;
import club.dannyserver.uno.common.UnoColor;
import club.dannyserver.uno.common.packet.PacketPlayCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FormGame {
    private final Client client;

    private JPanel mainPanel;

    public FormGame(Client client) {
        this.client = client;
        $$$setupUI$$$();

    }

    public PanelGame getMainPanel() {
        return (PanelGame) this.mainPanel;
    }

    private void createUIComponents() {
        mainPanel = new PanelGame(client);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel.setRequestFocusEnabled(false);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    public static class PanelGame extends JPanel implements MouseListener {

        private static final double SCALE = 0.3;

        private static final int CARD_WIDTH = (int) (Card.WIDTH * SCALE);

        private static final int CARD_HEIGHT = (int) (Card.HEIGHT * SCALE);

        private static final int CENTER_SIZE = 600 - 2 * CARD_HEIGHT;

        // 下, 左, 上, 右
        private static final int[] startXs = new int[]{
                CARD_HEIGHT,
                CARD_HEIGHT,
                CARD_HEIGHT + CENTER_SIZE,
                CARD_HEIGHT + CENTER_SIZE,
        };

        // 下, 左, 上, 右
        private static final int[] startYs = new int[]{
                CARD_HEIGHT + CENTER_SIZE,
                CARD_HEIGHT,
                CARD_HEIGHT,
                CARD_HEIGHT + CENTER_SIZE
        };

        // 表示玩家的 index
        private int userIndex;

        // 儲存所有玩家個別的牌數
        private int[] cardCount = new int[4];

        // 位在中央的牌
        private Card centerCard;

        // 玩家手上的牌
        private List<Card> cards = new ArrayList<>();

        private String[] usernames = new String[4];

        // 表示換誰出牌
        private int userTurnIndex = -1;

        private int turnVector = 1;

        private boolean isGameStart = false;

        private final Client client;

        private BufferedImage image;

        PanelGame(Client client) {
            this.client = client;

            // FIXME: 設定是要 600 x 600，但是要用591才會正確
            this.setPreferredSize(new Dimension(591, 591));

            try {
                image = ImageIO.read(getClass().getClassLoader().getResource("uno_cards_deck.png"));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            this.addMouseListener(this);
        }

        public void resetGame() {
            userIndex = 0;
            cardCount = new int[4];
            centerCard = null;
            usernames = new String[4];
            cards.clear();
            userTurnIndex = -1;
            turnVector = 1;
            isGameStart = false;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Center card
            if (centerCard != null) {
                if (pickColor != null) {
                    Graphics gC = g.create();
                    gC.setColor(getColor(pickColor));
                    gC.fillRect(300 - 50, 300 - 75, 100, 150);
                }


                drawCard((Graphics2D) g.create(),
                        CARD_HEIGHT + (CENTER_SIZE - CARD_WIDTH) / 2,
                        CARD_HEIGHT + (CENTER_SIZE - CARD_HEIGHT) / 2,
                        centerCard
                );
            }

            drawAllCard((Graphics2D) g.create(), startXs[0], startYs[0]);
            for (int i = 1; i < 4; i++) {
                int index = (userIndex + i) % 4;
                drawAllBackCard((Graphics2D) g.create(), cardCount[index], startXs[i], startYs[i], Math.toRadians(i * 90));
            }

            drawAllUsername((Graphics2D) g.create());

            if (isPickColorMode) {
                Graphics gC = g.create();
                gC.drawRect(300 - 150, 375, 300, 75);

                UnoColor[] colors = new UnoColor[]{UnoColor.RED, UnoColor.GREEN, UnoColor.BLUE, UnoColor.YELLOW};

                for (int i = 0; i < 4; i++) {
                    gC.setColor(getColor(colors[i]));
                    gC.fillRect(300 - 150 + (int) (300 / 4.0 * i) + 1, 375 + 1, (int) (300 / 4.0) - 1, 75 - 1);
                }
            }

            g.drawRect(CARD_HEIGHT, CARD_HEIGHT + CENTER_SIZE, CENTER_SIZE, CARD_HEIGHT);
            g.drawRect(0, CARD_HEIGHT, CARD_HEIGHT, CENTER_SIZE);
            g.drawRect(CARD_HEIGHT, 0, CENTER_SIZE, CARD_HEIGHT);
            g.drawRect(CARD_HEIGHT + CENTER_SIZE, CARD_HEIGHT, CARD_HEIGHT, CENTER_SIZE);
        }

        private void drawAllBackCard(Graphics2D g, int count, int startX, int startY, double radians) {
            double dw = CARD_WIDTH;
            int totalCardWidth = count * CARD_WIDTH;
            if (totalCardWidth > CENTER_SIZE) {
                dw = (CENTER_SIZE - CARD_WIDTH) / (double) (count - 1);
            }

            g.rotate(radians, startX, startY);
            for (int i = 0; i < count; i++) {
                if (totalCardWidth > CENTER_SIZE) {
                    drawBackCard(g, (int) (startX + i * dw), startY);
                }
                else {
                    drawBackCard(g, (int) (startX + i * dw + (CENTER_SIZE - totalCardWidth) / 2), startY);
                }
            }
        }

        private void drawBackCard(Graphics2D g, int x, int y) {
            int sourceX1 = Card.getCardBackCol() * Card.WIDTH;
            int sourceY1 = Card.getCardBackRow() * Card.HEIGHT;
            int sourceX2 = (Card.getCardBackCol() + 1) * Card.WIDTH;
            int sourceY2 = (Card.getCardBackRow() + 1) * Card.HEIGHT;

            g.drawImage(image,
                    x,
                    y,
                    x + CARD_WIDTH,
                    y + CARD_HEIGHT,
                    sourceX1,
                    sourceY1,
                    sourceX2,
                    sourceY2,
                    this
            );
        }

        private void drawAllCard(Graphics2D g, int startX, int startY) {
            double dw = CARD_WIDTH;
            int totalCardWidth = cards.size() * CARD_WIDTH;
            if (totalCardWidth > CENTER_SIZE) {
                dw = (CENTER_SIZE - CARD_WIDTH) / (double) (cards.size() - 1);
            }

            for (int i = 0; i < cards.size(); i++) {
                if (totalCardWidth > CENTER_SIZE) {
                    drawCard(g, (int) (startX + i * dw), startY, cards.get(i));
                }
                else {
                    drawCard(g, (int) (startX + i * dw + (CENTER_SIZE - totalCardWidth) / 2), startY, cards.get(i));
                }
            }
        }

        private void drawCard(Graphics2D g, int x, int y, Card card) {
            int r = card.getCardRow();
            int c = card.getCardCol();

            int sourceX1 = c * Card.WIDTH;
            int sourceY1 = r * Card.HEIGHT;
            int sourceX2 = (c + 1) * Card.WIDTH;
            int sourceY2 = (r + 1) * Card.HEIGHT;

            g.drawImage(image,
                    x,
                    y,
                    x + CARD_WIDTH,
                    y + CARD_HEIGHT,
                    sourceX1,
                    sourceY1,
                    sourceX2,
                    sourceY2,
                    this
            );
        }

        private void drawAllUsername(Graphics2D g) {
            g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
            for (int i = 0; i < 4; i++) {
                Graphics2D tempG = (Graphics2D) g.create();
                tempG.rotate(Math.toRadians(i * 90), 300, 300);

                int index = (i + userIndex) % 4;

                if (index == userTurnIndex) {
                    tempG.setColor(Color.RED);
                }
                else {
                    tempG.setColor(Color.BLACK);
                }

                String text = usernames[index];
                if (text == null) {
                    text = "";
                }
                int width = tempG.getFontMetrics().stringWidth(text);
                tempG.drawString(text, 300 - (width / 2), 475);
            }
        }

        public void setUserIndex(int index) {
            this.userIndex = index;
        }

        public void setUsernames(String[] usernames) {
            this.usernames = usernames;

            this.repaint();
        }

        public void setCardCount(int[] cardCount) {
            this.cardCount = cardCount;

            this.repaint();
        }

        public void setCards(List<Card> cards) {
            this.cards = cards;

            this.repaint();
        }

        public void setRoomInfo(Card centerCard, int userTurnIndex, int turnVector, UnoColor color) {
            this.centerCard = centerCard;
            this.userTurnIndex = userTurnIndex;
            this.turnVector = turnVector;
            this.pickColor = color;

            this.repaint();
        }

        private Color getColor(UnoColor color) {
            if (color == UnoColor.RED) {
                return Color.RED;
            }
            else if (color == UnoColor.BLUE) {
                return Color.BLUE;
            }
            else if (color == UnoColor.GREEN) {
                return Color.GREEN;
            }
            else if (color == UnoColor.YELLOW) {
                return Color.YELLOW;
            }

            return Color.WHITE;
        }

        public void startGame() {
            this.isGameStart = true;
        }

        private Boolean isPickColorMode = false;

        private UnoColor pickColor = null;

        private int selectIndex = -1;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isGameStart && userIndex == userTurnIndex) {
                int x = e.getX();
                int y = e.getY();

                if (isPickColorMode) {
                    if (150 <= x && x <= 450 && 375 <= y && y <= 450) {
                        int pickColorIndex = (x - 150) / (300 / 4);
                        pickColor = UnoColor.values()[pickColorIndex];

                        isPickColorMode = false;
                        this.repaint();
                        client.sendPacket(new PacketPlayCard(selectIndex, pickColor));
                    }
                }
                else {
                    if (CARD_HEIGHT + CENTER_SIZE <= y && y <= 600) {
                        if (CARD_HEIGHT <= x && x <= 600 - CARD_HEIGHT) {
                            updateSelectIndex(x, y);

                            if (selectIndex != -1) {
                                Card card = cards.get(selectIndex);

                                if (card.getColor() == UnoColor.BLACK) {
                                    isPickColorMode = true;
                                    this.repaint();
                                    return;
                                }
                                pickColor = null;

                                client.sendPacket(new PacketPlayCard(selectIndex, pickColor));
                            }
                        }
                    }
                }
            }
        }

        private void updateSelectIndex(int x, int y) {
            selectIndex = -1;
            int count = cards.size();

            if (count != 0) {
                int totalWidth = count * CARD_WIDTH;
                x -= CARD_HEIGHT;

                if (totalWidth < CENTER_SIZE) {
                    x -= (CENTER_SIZE - totalWidth) / 2;
                    if (x < 0) {
                        selectIndex = -1;
                    }
                    else {
                        selectIndex = x / CARD_WIDTH;
                        if (selectIndex >= count) {
                            selectIndex = -1;
                        }
                    }
                }
                else {
                    if (x > CENTER_SIZE - CARD_WIDTH) {
                        selectIndex = count - 1;
                    }
                    else {
                        int width = (CENTER_SIZE - CARD_WIDTH) / (count - 1);
                        selectIndex = x / width;
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // Nothing
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Nothing
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // Nothing
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Nothing
        }
    }
}
