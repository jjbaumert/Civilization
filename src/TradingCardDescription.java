import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TradingCardDescription {
    protected int number;
    protected String name;
    protected int maxStack;

    public int getNumber() { return number; }
    public String getName() { return name; }
    public int getMaxStack() { return maxStack; }
    public boolean isTradeable() { return true; }
    public boolean isCalamity() { return false; }

    public void loadTradingCard(Node nodeList) {
        Element cardElements = (Element) nodeList;

        number = Integer.parseInt(cardElements.getAttribute("number"));
        name = cardElements.getAttribute("name");
        maxStack = Integer.parseInt(cardElements.getAttribute("max"));
    }

    @Override
    public String toString() {
        return getName() + "(" + getNumber() + ")";
    }
}
