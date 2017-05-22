
import club.dannyserver.uno.common.Card;
import club.dannyserver.uno.common.UnoColor;
import club.dannyserver.uno.common.UnoRank;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CardTest {

    @Test
    public void testCardPlayed() {
        assertTrue(new Card(UnoColor.YELLOW, UnoRank.ZERO).canPlayedBy(new Card(UnoColor.YELLOW, UnoRank.EIGHT), null));
        assertTrue(new Card(UnoColor.YELLOW, UnoRank.ZERO).canPlayedBy(new Card(UnoColor.RED, UnoRank.ZERO), null));

        assertFalse(new Card(UnoColor.YELLOW, UnoRank.ZERO).canPlayedBy(new Card(UnoColor.RED, UnoRank.EIGHT), null));

        assertTrue(new Card(UnoColor.BLACK, UnoRank.WILD).canPlayedBy(new Card(UnoColor.RED, UnoRank.EIGHT), UnoColor.RED));
        assertFalse(new Card(UnoColor.BLACK, UnoRank.WILD).canPlayedBy(new Card(UnoColor.RED, UnoRank.EIGHT), UnoColor.BLUE));
    }
}
