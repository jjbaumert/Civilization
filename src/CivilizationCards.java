import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CivilizationCards extends XMLDataFile {
    protected Map<String, CivilizationCardDescription> cards;

    CivilizationCards() throws ParserConfigurationException, SAXException, IOException {
        super("CivilizationCards.xml");
        cards = new HashMap<>();
    }

    public void loadCardDescriptions() {
        NodeList cardDescriptions = getResourceItems("card");

        for(int nodeIndex=0; nodeIndex<cardDescriptions.getLength(); nodeIndex++) {
            Node cardNode = cardDescriptions.item(nodeIndex);

            CivilizationCardDescription civilizationCard = new CivilizationCardDescription();
            civilizationCard.loadCivilizationCard(cardNode);
            cards.put(civilizationCard.getName(), civilizationCard);
        }
    }

    public CivilizationCardDescription getCard(String cardName) {
        if(cards.containsKey(cardName)) {
            return cards.get(cardName);
        }
        return null;
    }

    public int getCostOfDraw(Set<CivilizationCardDescription> currentCards, Set<CivilizationCardDescription> cardDraw) {
        int cost = 0;

        for(CivilizationCardDescription card: cardDraw) {
            cost += card.getCost(currentCards);
        }

        return cost;
    }

    public boolean meetsPrerequisites(Set<CivilizationCardDescription> currentCards, Set<CivilizationCardDescription> cardDraw) {
        for(CivilizationCardDescription card: cardDraw) {
            if(!card.checkPrerequisites(currentCards)) {
                return false;
            }
        }
        return true;
    }

    public boolean isAvailable(CivilizationCardDescription card) {
        return true; // standard rules no limit to cards. No need to track.
    }

    public boolean areAvailable(Set<CivilizationCardDescription> cardsToDraw) {
        return true; // standard rules no limit to cards. No need to track.
    }

    public void draw(Set<CivilizationCardDescription> cardToDraw) {
        // standard rules no limit to cards. No need to track.
    }
}