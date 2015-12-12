import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CivilizationCardDescription {
    private String name;
    private int cost;
    private String description;

    private Map<String, Integer> credits;
    private Set<String> prerequisites;
    private Set<String> types;

    CivilizationCardDescription() {
        credits = new HashMap<>();
        prerequisites = new HashSet<>();
        types = new HashSet<>();
    }

    private void loadName(Element cardNode) {
        name = cardNode.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
    }

    private void loadDescription(Element cardNode) {
        description = cardNode.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
    }

    private void loadCost(Element cardNode) {
        cost = Integer.parseInt(cardNode.getElementsByTagName("cost").item(0).getChildNodes().item(0).getNodeValue());
    }

    private void loadCredits(Element cardNode) {
        NodeList creditNodes = cardNode.getElementsByTagName("credit");

        for(int x=0; x<creditNodes.getLength(); x++) {
            Element creditElement = (Element) creditNodes.item(x);

            int cost = Integer.parseInt(creditElement.getAttribute("value"));
            String name = creditElement.getChildNodes().item(0).getNodeValue();

            credits.put(name,cost);
        }
    }

    private void loadPrerequisites(Element cardNode) {
        NodeList prerequisite = cardNode.getElementsByTagName("prerequisite");

        for(int x=0; x<prerequisite.getLength(); x++) {
            Element creditElement = (Element) prerequisite.item(x);

            String name = creditElement.getChildNodes().item(0).getNodeValue();

            prerequisites.add(name);
        }
    }

    private void loadType(Element cardNode) {
        NodeList typeNodes = cardNode.getElementsByTagName("type");

        for(int x=0; x<typeNodes.getLength(); x++) {
            Element creditElement = (Element) typeNodes.item(x);

            String name = creditElement.getChildNodes().item(0).getNodeValue();

            types.add(name);
        }
    }

    public void loadCivilizationCard(Node cardNode) {
        Element cardElements = (Element) cardNode;

        loadName(cardElements);
        loadDescription(cardElements);
        loadCost(cardElements);
        loadCredits(cardElements);
        loadPrerequisites(cardElements);
        loadType(cardElements);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public int getCost(Set<CivilizationCardDescription> cards) {
        int startingCost = getCost();

        for(CivilizationCardDescription card: cards) {
            startingCost -= card.getCredit(card.getName());

            for(String type: types) {
                startingCost -= card.getCredit(type);
            }
        }

        if(startingCost<0) {
            startingCost = 0;
        }

        return startingCost;
    }

    public int getCredit(String creditName) {
        if(credits.containsKey(creditName)) {
            return credits.get(creditName);
        }
        return 0;
    }

    public boolean checkPrerequisites(Map<String, CivilizationCardDescription> cards) {
        return false;
    }

    public boolean isReligion() { return types.contains("Religion"); }
    public boolean isCivic() { return types.contains("Civic"); }
    public boolean isScience() { return types.contains("Science"); }
    public boolean isCraft() { return types.contains("Craft"); }
    public boolean isArt() { return types.contains("Arts"); }

    Set<String> getPrerequisites() {
        return prerequisites;
    }
}
