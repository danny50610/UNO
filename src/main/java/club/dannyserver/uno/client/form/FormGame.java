package club.dannyserver.uno.client.form;

import club.dannyserver.uno.client.Client;

import javax.swing.*;
import java.awt.*;

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

        PanelGame(Client client) {
            this.client = client;

            // FIXME: 這樣設定會是 600 x 600，但是要用591才會正確
            this.setPreferredSize(new Dimension(591, 591));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

//            System.out.println(getPreferredSize());
//
//            g.drawString("BLAH", 20, 20);
//            g.drawRect(0, 0, 600, 600);
        }
    }
}
