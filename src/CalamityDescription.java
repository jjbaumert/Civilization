import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CalamityDescription extends TradingCardDescription {
    protected boolean tradeable;

    @Override
    public boolean isTradeable() { return tradeable; }

    @Override
    public boolean isCalamity() { return true; }

    @Override
    public void loadTradingCard(Node nodeList) {
        Element cardElements = (Element) nodeList;

        number = Integer.parseInt(cardElements.getAttribute("number"));
        name = cardElements.getAttribute("name");
        maxStack = 1;
        tradeable = cardElements.getAttribute("tradeable").equals("yes");
    }
}
