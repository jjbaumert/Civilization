import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CivilizationCards extends XMLDataFile {
    protected Map<String, CivilizationCard> cards;

    CivilizationCards() throws ParserConfigurationException, SAXException, IOException {
        super("CivilizationCards.xml");
        cards = new HashMap<>();
    }

    public void loadCardDescriptions() {
        NodeList cardDescriptions = getResourceItems("card");

        for(int nodeIndex=0; nodeIndex<cardDescriptions.getLength(); nodeIndex++) {
            Node cardNode = cardDescriptions.item(nodeIndex);

            CivilizationCard civilizationCard = new CivilizationCard();
            civilizationCard.loadCivilizationCard(cardNode);
            cards.put(civilizationCard.getName(), civilizationCard);

            System.out.println(civilizationCard);
        }
    }

    public int getCost(String cardName, Map<String, CivilizationCard> cards) {
        return 0;
    }

    public boolean checkPrerequisites(String cardName, Map<String, CivilizationCard> cards) {
        return false;
    }

    public CivilizationCard getCard(String cardName) {
        if(cards.containsKey(cardName)) {
            return cards.get(cardName);
        }
        return null;
    }
}
