package club.dannyserver.uno.client.form;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.common.Card;
import club.dannyserver.uno.common.UnoColor;
import club.dannyserver.uno.common.UnoRank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class FormGame {
    private final Client client;

    private JPanel mainPanel;

    public FormGame(Client client) {
        this.client = client;
    }

    public PanelGame getMainPanel() {
        return (PanelGame) this.mainPanel;
    }

    private void createUIComponents() {
        mainPanel = new PanelGame(client);
    }

    public static class PanelGame extends JPanel {

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
        private Card[] cards;

        private String[] usernames = new String[4];

        // 表示換誰出牌
        private int userTurnIndex = -1;

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

            // FIXME: Test code
//            this.userIndex = 1;
            //this.userTurnIndex = 0;

//            this.usernames[0] = "danny";
//            this.usernames[1] = "illya1";
//            this.usernames[2] = "asdjkasjadlk";
//            this.usernames[3] = "illya2514";

            this.cardCount[0] = 5;
            this.cardCount[1] = 7;
            this.cardCount[2] = 3;
            this.cardCount[3] = 15;

            this.centerCard = new Card(UnoColor.BLACK, UnoRank.WILD_DRAW_FOUR);

            this.cards = new Card[]{
                    new Card(UnoColor.BLACK, UnoRank.WILD_DRAW_FOUR),
                    new Card(UnoColor.BLUE, UnoRank.ZERO),
                    new Card(UnoColor.GREEN, UnoRank.FOUR),
                    new Card(UnoColor.YELLOW, UnoRank.FIVE),
                    new Card(UnoColor.YELLOW, UnoRank.DRAW_TWO),
                    new Card(UnoColor.RED, UnoRank.REVERSE),
                    new Card(UnoColor.GREEN, UnoRank.ONE),
                    new Card(UnoColor.GREEN, UnoRank.THREE),
                    new Card(UnoColor.GREEN, UnoRank.DRAW_TWO),
                    new Card(UnoColor.BLACK, UnoRank.WILD),
            };
            // FIXME: Test code end
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Center card
            drawCard((Graphics2D) g.create(),
                    CARD_HEIGHT + (CENTER_SIZE - CARD_WIDTH) / 2,
                    CARD_HEIGHT + (CENTER_SIZE - CARD_HEIGHT) / 2,
                    centerCard
            );

            drawAllCard((Graphics2D) g.create(), startXs[0], startYs[0]);
            for (int i = 1; i < 4; i++) {
                int index = (userIndex + i) % 4;
                drawAllBackCard((Graphics2D) g.create(), cardCount[index], startXs[i], startYs[i], Math.toRadians(i * 90));
            }

            drawAllUsername((Graphics2D) g.create());

            // FIXME: debug line
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
            int totalCardWidth = cards.length * CARD_WIDTH;
            if (totalCardWidth > CENTER_SIZE) {
                dw = (CENTER_SIZE - CARD_WIDTH) / (double) (cards.length - 1);
            }

            for (int i = 0; i < cards.length; i++) {
                if (totalCardWidth > CENTER_SIZE) {
                    drawCard(g, (int) (startX + i * dw), startY, cards[i]);
                }
                else {
                    drawCard(g, (int) (startX + i * dw + (CENTER_SIZE - totalCardWidth) / 2), startY, cards[i]);
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
    }
}
