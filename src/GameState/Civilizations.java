package GameState;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Civilizations extends XMLDataFile {
    private Map<String, CivilizationDescription> civilizations;

    public Civilizations() throws ParserConfigurationException, SAXException, IOException {
        super("Civilizations.xml");

        civilizations = new HashMap<>();
    }

    public void loadCivilizations() {
        NodeList cardDescriptions = getResourceItems("civilization");

        for (int nodeIndex = 0; nodeIndex < cardDescriptions.getLength(); nodeIndex++) {
            Node civilization = cardDescriptions.item(nodeIndex);

            CivilizationDescription civilizationDescription = new CivilizationDescription();

            civilizationDescription.loadCivilization(civilization);
            civilizations.put(civilizationDescription.getName(), civilizationDescription);
        }
    }

    public CivilizationDescription getCivilization(String civilizationName) {
        return civilizations.get(civilizationName);
    }

    public Set<String> getNames() {
        return civilizations.keySet();
    }
}
