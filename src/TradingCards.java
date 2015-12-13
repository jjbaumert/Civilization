import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class TradingCards extends XMLDataFile {
    final int MAX_CARD_STACK = 9;

    Map<String, TradingCardDescription> tradingCardDescriptions;
    Map<String, TradingCardDescription> calamityDescriptions;

    Map<Integer, ArrayList<TradingCardDescription>> tradingCardsByValue;
    Map<Integer, ArrayList<TradingCardDescription>> calamitiesByValue;

    Map<Integer, ArrayList<TradingCardDescription>> stacks;

    TradingCards() throws IOException, SAXException, ParserConfigurationException {
        super("TradingCard.xml");
        tradingCardDescriptions = new HashMap<>();
        calamityDescriptions = new HashMap<>();

        stacks = new HashMap<>();

        tradingCardsByValue = new HashMap<>();
        calamitiesByValue = new HashMap<>();

        for(int x=1; x<=MAX_CARD_STACK; x++) {
            stacks.put(x, new ArrayList<>());
            tradingCardsByValue.put(x, new ArrayList<>());
            calamitiesByValue.put(x, new ArrayList<>());
        }
    }

    public void loadCardDescriptions() {
        NodeList cardDescriptions = getResourceItems("card");

        for (int nodeIndex = 0; nodeIndex < cardDescriptions.getLength(); nodeIndex++) {
            Node cardNode = cardDescriptions.item(nodeIndex);

            TradingCardDescription tradingCard = new TradingCardDescription();
            tradingCard.loadTradingCard(cardNode);
            tradingCardDescriptions.put(tradingCard.getName(), tradingCard);
            tradingCardsByValue.get(tradingCard.getNumber()).add(tradingCard);
        }

        cardDescriptions = getResourceItems("calamity");

        for (int nodeIndex = 0; nodeIndex < cardDescriptions.getLength(); nodeIndex++) {
            Node cardNode = cardDescriptions.item(nodeIndex);

            TradingCardDescription tradingCard = new CalamityDescription();
            tradingCard.loadTradingCard(cardNode);
            calamityDescriptions.put(tradingCard.getName(), tradingCard);
            calamitiesByValue.get(tradingCard.getNumber()).add(tradingCard);
        }
    }

    public void initialSetup() {
        for(int stackValue=1;stackValue<=MAX_CARD_STACK;stackValue++) {
            ArrayList<TradingCardDescription> cardStack = stacks.get(stackValue);
            cardStack.clear();

            // add trading cards

            for(TradingCardDescription card: tradingCardsByValue.get(stackValue)) {
                for(int x=0; x<card.getMaxStack(); x++) {
                    cardStack.add(card);
                }
            }

            // add tradeable calamities

            for(TradingCardDescription calamity: calamitiesByValue.get(stackValue)) {
                if(calamity.isTradeable()) {
                    cardStack.add(calamity);
                }
            }

            // mix and add the non-tradeable calamities

            Collections.shuffle(cardStack);

            for(TradingCardDescription calamity: calamitiesByValue.get(stackValue)) {
                if(!calamity.isTradeable()) {
                    cardStack.add(calamity);
                }
            }
        }
    }

    public ArrayList<TradingCardDescription> drawCards(int maxCard) {
        ArrayList<TradingCardDescription> cardDraw = new ArrayList<>();

        for(int stackIndex=1; stackIndex<=maxCard; stackIndex++) {
            ArrayList<TradingCardDescription> stack = stacks.get(stackIndex);

            if(stack.size()>0) {
                TradingCardDescription card = stack.get(0);
                stack.remove(0);
                cardDraw.add(card);
            }
        }

        return cardDraw;
    }

    public void returnCards(ArrayList<TradingCardDescription> cardList) {
        Collections.shuffle(cardList);

        for(TradingCardDescription card: cardList) {
            ArrayList<TradingCardDescription> stack = stacks.get(card.getNumber());
            stack.add(card);
        }
    }
}
