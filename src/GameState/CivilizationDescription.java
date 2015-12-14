package GameState;

import javafx.scene.paint.Color;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class CivilizationDescription {
    private Color color;
    private String name;

    int stoneAge;
    int earlyBronzeAge;
    int lateBronzeAge;
    int earlyIronAge;
    int lateIronAge;

    private Map<Integer, Integer> valueToExit;
    private Set<String> startingTerritories;

    public CivilizationDescription() {
        valueToExit = new HashMap<>();
        startingTerritories = new HashSet<>();
    }

    public Color getColor() { return color; }
    public String getName() { return name; }
    public Set<String> getStartingTerritories() { return startingTerritories; }

    public int getStoneAgeStart() { return stoneAge; }
    public int getEarlyBronzeAgeStart() { return earlyBronzeAge; }
    public int getLateBronzeAgeStart() { return lateBronzeAge; }
    public int getEarlyIronAgeStart() { return earlyIronAge; }
    public int getLateIronAgeSTart() { return lateIronAge; }

    public int getValueToExit(int stage) {
        if(valueToExit.containsKey(stage)) {
            return valueToExit.get(stage);
        }
        return 0;
    }

    public void loadCivilization(Node nodeList) {
        Element civilizationElements = (Element) nodeList;

        name = civilizationElements.getAttribute("name");
        color = Color.web(civilizationElements.getAttribute("color"));

        Node ages = civilizationElements.getElementsByTagName("age").item(0);

        loadAges(ages);
        loadNeeded(ages);

        Node startingTerritories = civilizationElements.getElementsByTagName("starting").item(0);

        loadStartingTerritories(startingTerritories);
    }

    private void loadAges(Node ages) {
        stoneAge = 1;
        earlyBronzeAge = loadAge(ages,"earlybronze");
        lateBronzeAge = loadAge(ages,"latebronze");
        earlyIronAge = loadAge(ages,"earlyiron");
        lateIronAge = loadAge(ages,"lateiron");
    }

    private int loadAge(Node ages, String ageName) {
        Element agesElements = (Element) ages;
        Node ageNode = agesElements.getElementsByTagName(ageName).item(0);
        Element ageElement = (Element) ageNode;
        String startValue = ageElement.getAttribute("start");
        return Integer.parseInt(startValue);
    }

    private void loadNeeded(Node ages) {
        NodeList neededElements = ((Element) ages).getElementsByTagName("needed");

        valueToExit.clear();

        for(int x=0; x<neededElements.getLength(); x++) {
            Element neededElement = (Element) neededElements.item(x);

            int point = Integer.parseInt(neededElement.getAttribute("point"));
            int value = Integer.parseInt(neededElement.getAttribute("value"));

            valueToExit.put(point,value);
        }
    }

    private void loadStartingTerritories(Node node) {
        NodeList territories = ((Element) node).getElementsByTagName("territory");

        startingTerritories.clear();

        for(int x=0; x<territories.getLength(); x++) {
            Element neededElement = (Element) territories.item(x);

            String name = neededElement.getAttribute("name");
            startingTerritories.add(name);
        }
    }
}
