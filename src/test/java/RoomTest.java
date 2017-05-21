import club.dannyserver.uno.common.Card;
import club.dannyserver.uno.common.Room;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoomTest {

    @Test
    public void testGenAllCard() {
        Room room = new Room();

        List<Card> cards = room.genAllCard();

        assertEquals(108, cards.size());
    }

}
