import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class ImageManager {
    Map<String, Image> imageMap;

    ImageManager() {
        imageMap = new HashMap<>();
    }

    void loadImages(String imageDescriptionFileName) throws ParserConfigurationException, IOException, SAXException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream imageDescriptionFile = classLoader.getResourceAsStream(imageDescriptionFileName);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(imageDescriptionFile);
        doc.getDocumentElement().normalize();

        NodeList imageDescriptions = doc.getElementsByTagName("image");

        for(int nodeIndex=0; nodeIndex<imageDescriptions.getLength(); nodeIndex++) {
            Element imageDescription = (Element) imageDescriptions.item(nodeIndex);

            String name = imageDescription.getAttribute("name");
            String location = imageDescription.getAttribute("location");

            System.out.println(name+","+location);

            Image image = new Image(location);
            imageMap.put(name,image);
        }
    }

    Image get(String name) {
        return imageMap.get(name);
    }
}
