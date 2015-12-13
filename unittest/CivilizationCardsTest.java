import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CivilizationCardsTest {
    CivilizationCards civilizationCards;
    Set<CivilizationCardDescription> cardsInHand;
    Set<CivilizationCardDescription> cardsToDraw;

    @Before
    public void setUp() throws Exception {
        civilizationCards = new CivilizationCards();

        cardsInHand = new HashSet<>();
        cardsToDraw = new HashSet<>();

        civilizationCards.loadCardDescriptions();
    }

    @Test
    public void testGetCostOfDraw_SimpleDrawEmptyHand() throws Exception {
        cardsToDraw.add(civilizationCards.getCard("Pottery"));

        assertTrue(civilizationCards.getCostOfDraw(cardsInHand, cardsToDraw) == 45);
    }

    @Test
    public void testGetCostOfDraw_MultiDrawEmptyHand() throws Exception {
        cardsToDraw.add(civilizationCards.getCard("Pottery"));
        cardsToDraw.add(civilizationCards.getCard("Mysticism"));

        assertTrue(civilizationCards.getCostOfDraw(cardsInHand, cardsToDraw)==95);
    }

    @Test
    public void testGetCostOfDraw_SingleCredit() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));

        cardsToDraw.add(civilizationCards.getCard("Agriculture"));

        assertTrue(civilizationCards.getCostOfDraw(cardsInHand, cardsToDraw)==100);
    }

    @Test
    public void testGetCostOfDraw_MultiCredit() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));
        cardsInHand.add(civilizationCards.getCard("Metalworking"));

        cardsToDraw.add(civilizationCards.getCard("Agriculture"));

        assertTrue(civilizationCards.getCostOfDraw(cardsInHand, cardsToDraw)==90);
    }

    @Test
    public void testGetCostOfDraw_SingleCard_MultiCredit() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));

        cardsToDraw.add(civilizationCards.getCard("Metalworking"));
        cardsToDraw.add(civilizationCards.getCard("Agriculture"));

        assertTrue(civilizationCards.getCostOfDraw(cardsInHand, cardsToDraw)==170);
    }

    @Test
    public void testGetCostOfDraw_MultiCard_NoCredit() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));
        cardsInHand.add(civilizationCards.getCard("Metalworking"));

        cardsToDraw.add(civilizationCards.getCard("Mysticism"));

        assertTrue(civilizationCards.getCostOfDraw(cardsInHand, cardsToDraw)==50);
    }

    @Test
    public void testMeetsPrerequisites_EmptyHandNoPrerequisite() throws Exception {
        cardsToDraw.add(civilizationCards.getCard("Pottery"));

        assertTrue(civilizationCards.meetsPrerequisites(cardsInHand, cardsToDraw));
    }

    @Test
    public void testMeetsPrerequisites_EmptyHandDoesNotHavePrerequisite() throws Exception {
        cardsToDraw.add(civilizationCards.getCard("Democracy"));

        assertFalse(civilizationCards.meetsPrerequisites(cardsInHand, cardsToDraw));
    }

    @Test
    public void testMeetsPrerequisites_CardsInHandNoPrerequisite() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));
        cardsInHand.add(civilizationCards.getCard("Metalworking"));

        cardsToDraw.add(civilizationCards.getCard("Agriculture"));

        assertTrue(civilizationCards.meetsPrerequisites(cardsInHand, cardsToDraw));
    }

    @Test
    public void testMeetsPrerequisites_CardsInHandMissingPrerequisite() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));
        cardsInHand.add(civilizationCards.getCard("Metalworking"));

        cardsToDraw.add(civilizationCards.getCard("Democracy"));

        assertFalse(civilizationCards.meetsPrerequisites(cardsInHand, cardsToDraw));
    }

    @Test
    public void testMeetsPrerequisites_CardsInHandHasPrerequisite() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));
        cardsInHand.add(civilizationCards.getCard("Law"));

        cardsToDraw.add(civilizationCards.getCard("Democracy"));

        assertTrue(civilizationCards.meetsPrerequisites(cardsInHand, cardsToDraw));
    }

    @Test
    public void testMeetsPrerequisites_CardsInHandHasMultiPrerequisite() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));
        cardsInHand.add(civilizationCards.getCard("Law"));

        cardsToDraw.add(civilizationCards.getCard("Democracy"));
        cardsToDraw.add(civilizationCards.getCard("Philosophy"));

        assertTrue(civilizationCards.meetsPrerequisites(cardsInHand, cardsToDraw));
    }

    @Test
    public void testMeetsPrerequisites_CardsInHandHasOneMissingMultiPrerequisite() throws Exception {
        cardsInHand.add(civilizationCards.getCard("Pottery"));
        cardsInHand.add(civilizationCards.getCard("Law"));

        cardsToDraw.add(civilizationCards.getCard("Democracy"));
        cardsToDraw.add(civilizationCards.getCard("Theology"));

        assertFalse(civilizationCards.meetsPrerequisites(cardsInHand, cardsToDraw));
    }
}