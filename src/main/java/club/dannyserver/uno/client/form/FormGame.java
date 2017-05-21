package club.dannyserver.uno.client.form;

import club.dannyserver.uno.client.Client;
import club.dannyserver.uno.common.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    static class PanelGame extends JPanel {

        private static final double SCALE = 0.3;

        private static final int CARD_WIDTH = (int) (Card.WIDTH * SCALE);

        private static final int CARD_HEIGHT = (int) (Card.HEIGHT * SCALE);

        private static final int CENTER_SIZE = 600 - 2 * CARD_HEIGHT;

        private int userIndex;

        private int[] cardCount = new int[4];

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
            this.userIndex = 0;

            this.cardCount[0] = 5;
            this.cardCount[1] = 7;
            this.cardCount[2] = 3;
            this.cardCount[3] = 15;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            //Graphics2D gCard = (Graphics2D)g;
//            gCard.scale(SCALE, SCALE);

            // left


            drawAllBackCard((Graphics2D) g.create(), 3, CARD_HEIGHT, CARD_HEIGHT + CENTER_SIZE, Math.toRadians(0));
            drawAllBackCard((Graphics2D) g.create(), 6, CARD_HEIGHT, CARD_HEIGHT, Math.toRadians(90));
            drawAllBackCard((Graphics2D) g.create(), 16, CARD_HEIGHT + CENTER_SIZE, CARD_HEIGHT, Math.toRadians(180));
            drawAllBackCard((Graphics2D) g.create(), 10, CARD_HEIGHT + CENTER_SIZE, CARD_HEIGHT + CENTER_SIZE, Math.toRadians(270));

            //gCard.rotate(Math.toRadians(45));
            //gCard.drawImage(image, 0, 0, this);
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

        public void setUserIndex(int index) {
            this.userIndex = index;
        }
    }
}
