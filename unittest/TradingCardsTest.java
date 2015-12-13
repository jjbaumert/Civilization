import org.junit.Test;

import java.util.*;

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
        TradingCards tradingCards = new TradingCards();
        tradingCards.loadCardDescriptions();

        ArrayList<TradingCardDescription> pretendReturn = new ArrayList<>();

        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Ochre"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Iron"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Iron"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Salt"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Grain"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Cloth"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Silver"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Resin"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Gems"));
        pretendReturn.add(tradingCards.tradingCardDescriptions.get("Gold"));

        tradingCards.returnCards(pretendReturn);

        ArrayList<TradingCardDescription> tradingCardDraw = tradingCards.drawCards(9);
        assertTrue(tradingCardDraw.size() == 9);
        assertTrue(tradingCardDraw.get(0).getName().equals("Ochre"));
        assertTrue(tradingCardDraw.get(1).getName().equals("Iron"));
        assertTrue(tradingCardDraw.get(2).getName().equals("Salt"));
        assertTrue(tradingCardDraw.get(3).getName().equals("Grain"));
        assertTrue(tradingCardDraw.get(4).getName().equals("Cloth"));
        assertTrue(tradingCardDraw.get(5).getName().equals("Silver"));
        assertTrue(tradingCardDraw.get(6).getName().equals("Resin"));
        assertTrue(tradingCardDraw.get(7).getName().equals("Gems"));
        assertTrue(tradingCardDraw.get(8).getName().equals("Gold"));
        tradingCardDraw = tradingCards.drawCards(9);
        assertTrue(tradingCardDraw.size() == 1);
        assertTrue(tradingCardDraw.get(0).getName().equals("Iron"));
    }
}
