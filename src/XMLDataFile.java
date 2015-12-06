import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class XMLDataFile {
    private Document document;

    XMLDataFile(String resourceName) throws IOException, SAXException, ParserConfigurationException {
        load(resourceName);
    }

    private InputStream getInputStream(String resourceName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(resourceName);
    }

    private void getDocument(InputStream resourceStream) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        document = dBuilder.parse(resourceStream);
        document.getDocumentElement().normalize();
    }

    private void load(String resourceName) throws ParserConfigurationException, IOException, SAXException {
        InputStream resourceStream = getInputStream(resourceName);
        getDocument(resourceStream);
    }

    NodeList getResourceItems(String itemName) {
        return document.getElementsByTagName(itemName);
    }
}
