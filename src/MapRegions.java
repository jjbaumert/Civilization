import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class MapRegions {
    final String MAP_FILE_NAME = "map/MapRegions.xml";

    Vector<MapRegion> regions;

    public MapRegions() {
        regions = new Vector<>();
    }

    public void loadRegions() throws ParserConfigurationException, SAXException, IOException {
        InputStream mapFile = getMapFile();
        Document mapDocument = getXMLDocument(mapFile);

        loadMapRegions(mapDocument);
    }

    private InputStream getMapFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(MAP_FILE_NAME);
    }

    private Document getXMLDocument(InputStream mapFile) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(mapFile);
        doc.getDocumentElement().normalize();

        return doc;
    }

    private void loadMapRegions(Document doc) {
        NodeList mapRegionList = doc.getElementsByTagName("mapregion");

        for(int nodeIndex=0; nodeIndex<mapRegionList.getLength(); nodeIndex++) {
            Node mapRegion = mapRegionList.item(nodeIndex);
            MapRegion region = new MapRegion();

            region.loadMapRegion(mapRegion);
            regions.add(region);
        }
    }

    public MapRegion getRegionByMapPoint(MapPoint point) {
        for (MapRegion region : regions) {
            if (region.pointInside(point)) {
                return region;
            }
        }
        return null;
    }

    public void draw(GraphicsContext context, Image cityImage, Image asianTile) {
        for (MapRegion mapRegion : regions) {
            mapRegion.draw(context, cityImage, asianTile);
        }
    }
}
