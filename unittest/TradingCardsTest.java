import org.junit.Test;

import java.util.*;

import java.util.Collections.*;
import org.junit.Assert.*;

import static org.junit.Assert.assertTrue;

public class TradingCardsTest {

    @Test
    public void testInitialSetup_StackSize() throws Exception {
        TradingCards tradingCards = new TradingCards();
        tradingCards.loadCardDescriptions();

        tradingCards.initialSetup();

        assertTrue(tradingCards.stacks.get(1).size() == 14);
        assertTrue(tradingCards.stacks.get(2).size() == 17);
        assertTrue(tradingCards.stacks.get(3).size() == 19);
        assertTrue(tradingCards.stacks.get(4).size() == 17);
        assertTrue(tradingCards.stacks.get(5).size() == 15);
        assertTrue(tradingCards.stacks.get(6).size() == 12);
        assertTrue(tradingCards.stacks.get(7).size() == 12);
        assertTrue(tradingCards.stacks.get(8).size() == 10);
        assertTrue(tradingCards.stacks.get(9).size() == 10);
    }

    @Test
    public void testDrawCards() throws Exception {
        TradingCards tradingCards = new TradingCards();
        tradingCards.loadCardDescriptions();

        tradingCards.initialSetup();

        for(int roundCount=1; roundCount<=20; roundCount++) {
            ArrayList<TradingCardDescription> tradingCardDraw = tradingCards.drawCards(9);

            switch(roundCount) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    assertTrue(tradingCardDraw.size() == 9);
                    break;
                case 11:
                case 12:
                    assertTrue(tradingCardDraw.size() == 7);
                    break;
                case 13:
                case 14:
                    assertTrue(tradingCardDraw.size() == 5);
                    break;
                case 15:
                    assertTrue(tradingCardDraw.size() == 4);
                    break;
                case 16:
                case 17:
                    assertTrue(tradingCardDraw.size() == 3);
                    break;
                case 18:
                case 19:
                    assertTrue(tradingCardDraw.size() == 1);
                    break;
                case 20:
                    assertTrue(tradingCardDraw.size() == 0);
                    break;
            }
        }
    }

    @Test
    public void testReturnCards() throws Exception {
        assertTrue(false);
    }
}