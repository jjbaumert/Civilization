import GameState.XMLDataFile;
import javafx.scene.image.Image;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ImageManager extends XMLDataFile {
    Map<String, Image> imageMap;

    ImageManager(String resourceName) throws ParserConfigurationException, SAXException, IOException {
        super(resourceName);
        imageMap = new HashMap<>();
    }

    void loadImages() {
        NodeList imageDescriptions = getResourceItems("image");

        for(int nodeIndex=0; nodeIndex<imageDescriptions.getLength(); nodeIndex++) {
            Element imageDescription = (Element) imageDescriptions.item(nodeIndex);

            String name = imageDescription.getAttribute("name");
            String location = imageDescription.getAttribute("location");

            Image image = new Image(location);
            imageMap.put(name,image);
        }
    }

    Image get(String name) {
        return imageMap.get(name);
    }
}
