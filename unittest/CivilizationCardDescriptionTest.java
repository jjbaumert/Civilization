import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CivilizationCardDescriptionTest {
    CivilizationCards cards;

    @Before
    public void setUp() throws Exception {
        cards = new CivilizationCards();
        cards.loadCardDescriptions();
    }

    @Test
    public void testGetCost_CraftProgression() throws Exception {
        CivilizationCardDescription card;
        Set<CivilizationCardDescription> currentCards = new HashSet<>();
        CivilizationCardDescription pottery;

        pottery = cards.getCard("Pottery");
        assertTrue(pottery.getCost()==45);
        card = cards.getCard("Cloth Making");
        currentCards.add(card);
        assertTrue(pottery.getCost(currentCards)==35);
        card = cards.getCard("Metalworking");
        currentCards.add(card);
        assertTrue(pottery.getCost(currentCards)==25);
        card = cards.getCard("Agriculture");
        currentCards.add(card);
        assertTrue(pottery.getCost(currentCards)==15);
        card = cards.getCard("Engineering");
        currentCards.add(card);
        assertTrue(pottery.getCost(currentCards)==5);
        card = cards.getCard("Roadbuilding");
        currentCards.add(card);
        assertTrue(pottery.getCost(currentCards)==0);
    }

    @Test
    public void testGetCost_MultiType() throws Exception {
        CivilizationCardDescription card;
        Set<CivilizationCardDescription> currentCards = new HashSet<>();
        CivilizationCardDescription engineering;

        engineering = cards.getCard("Engineering");

        assertTrue(engineering.getCost()==140);
        card = cards.getCard("Cloth Making");
        currentCards.add(card);
        assertTrue(engineering.getCost(currentCards)==130);
        card = cards.getCard("Astronomy");
        currentCards.add(card);
        assertTrue(engineering.getCost(currentCards)==110);
        card = cards.getCard("Mysticism");
        currentCards.add(card);
        assertTrue(engineering.getCost(currentCards)==110);
    }

    @Test
    public void testGetCost_CardSpecific() throws Exception {
        CivilizationCardDescription card;
        Set<CivilizationCardDescription> currentCards = new HashSet<>();
        CivilizationCardDescription democracy;

        democracy = cards.getCard("Democracy");

        assertTrue(democracy.getCost()==200);
        card = cards.getCard("Astronomy");
        currentCards.add(card);
        assertTrue(democracy.getCost(currentCards)==200);
        card = cards.getCard("Pottery");
        currentCards.add(card);
        assertTrue(democracy.getCost(currentCards)==190);
        card = cards.getCard("Literacy");
        currentCards.add(card);
        assertTrue(democracy.getCost(currentCards)==165);
    }

    @Test
    public void testCheckPrerequisites_Empty() throws Exception {
        Set<CivilizationCardDescription> currentCards = new HashSet<>();
        CivilizationCardDescription pottery;

        pottery = cards.getCard("Pottery");
        assertTrue(pottery.checkPrerequisites(currentCards));
    }

    @Test
    public void testCheckPrerequisites_None() throws Exception {
        CivilizationCardDescription card;
        Set<CivilizationCardDescription> currentCards = new HashSet<>();
        CivilizationCardDescription pottery;

        pottery = cards.getCard("Pottery");
        card = cards.getCard("Coinage");
        currentCards.add(card);
        assertTrue(pottery.checkPrerequisites(currentCards));
    }

    @Test
    public void testCheckPrerequisites_HaveIt() throws Exception {
        CivilizationCardDescription card;
        Set<CivilizationCardDescription> currentCards = new HashSet<>();
        CivilizationCardDescription democracy;

        democracy = cards.getCard("Democracy");
        card = cards.getCard("Law");
        currentCards.add(card);
        assertTrue(democracy.checkPrerequisites(currentCards));
    }

    @Test
    public void testCheckPrerequisites_DoNotHaveIt() throws Exception {
        CivilizationCardDescription card;
        Set<CivilizationCardDescription> currentCards = new HashSet<>();
        CivilizationCardDescription democracy;

        democracy = cards.getCard("Democracy");
        card = cards.getCard("Pottery");
        currentCards.add(card);
        assertFalse(democracy.checkPrerequisites(currentCards));
    }

}