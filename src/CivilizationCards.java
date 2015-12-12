import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

            System.out.println(civilizationCard);
        }
    }

    public int getCost(String cardName, Map<String, CivilizationCardDescription> cards) {
        return 0;
    }

    public boolean checkPrerequisites(String cardName, Map<String, CivilizationCardDescription> cards) {
        return false;
    }

    public CivilizationCardDescription getCard(String cardName) {
        if(cards.containsKey(cardName)) {
            return cards.get(cardName);
        }
        return null;
    }
}