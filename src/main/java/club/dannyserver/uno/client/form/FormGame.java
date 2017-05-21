package club.dannyserver.uno.client.form;

import club.dannyserver.uno.client.Client;

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

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    private void createUIComponents() {
        mainPanel = new PanelGame(client);
    }

    static class PanelGame extends JPanel {

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
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D gCard = (Graphics2D)g.create();
            gCard.scale(0.25, 0.25);
            gCard.drawImage(image, 0, 0, this);

//            System.out.println(getPreferredSize());
//
//            g.drawString("BLAH", 20, 20);
//            g.drawRect(0, 0, 600, 600);
        }
    }
}
