import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TradingCards extends XMLDataFile {
    Map<String,TradingCard> tradingCardDescriptions;
    Map<String,TradingCard> calamityDescriptions;

    Map<Integer, Vector<TradingCard>> stacks;

    TradingCards() throws IOException, SAXException, ParserConfigurationException {
        super("TradingCard.xml");
        tradingCardDescriptions = new HashMap<>();
        calamityDescriptions = new HashMap<>();

    }

    public void loadCardDescriptions() {
        NodeList cardDescriptions = getResourceItems("card");

        for (int nodeIndex = 0; nodeIndex < cardDescriptions.getLength(); nodeIndex++) {
            Node cardNode = cardDescriptions.item(nodeIndex);

            TradingCard tradingCard = new TradingCard();
            tradingCard.loadTradingCard(cardNode);
            tradingCardDescriptions.put(tradingCard.getName(), tradingCard);
        }

        cardDescriptions = getResourceItems("calamity");

        for (int nodeIndex = 0; nodeIndex < cardDescriptions.getLength(); nodeIndex++) {
            Node cardNode = cardDescriptions.item(nodeIndex);

            TradingCard tradingCard = new Calamity();
            tradingCard.loadTradingCard(cardNode);
            calamityDescriptions.put(tradingCard.getName(), tradingCard);
        }
    }
}
