import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    private void getMapRegions(Document doc) {
        NodeList mapRegionList = doc.getElementsByTagName("mapregion");

        for(int nodeIndex=0; nodeIndex<mapRegionList.getLength(); nodeIndex++) {
            Node mapRegion = mapRegionList.item(nodeIndex);
            Element mapRegionElements = (Element) mapRegion;

            String name = mapRegionElements.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            NodeList floodPlain = mapRegionElements.getElementsByTagName("flood");
            NodeList volcano = mapRegionElements.getElementsByTagName("volcano");
            Node boundary = mapRegionElements.getElementsByTagName("boundary").item(0);
            NodeList pointList = ((Element)boundary).getElementsByTagName("point");

            NodeList city = mapRegionElements.getElementsByTagName("city");

            boolean citySite = false;
            MapPoint cityMapPoint = new MapPoint(4000,4000);

            if(city.getLength()>0) {
                Element cityLocation = (Element) city.item(0);
                float cityLocationX = Float.parseFloat(cityLocation.getAttribute("x"));
                float cityLocationY = Float.parseFloat(cityLocation.getAttribute("y"));

                cityMapPoint = new MapPoint(cityLocationX, cityLocationY);

                if (cityLocation.hasAttribute("site")) {
                    citySite = Boolean.parseBoolean(cityLocation.getAttribute("site"));
                }

            }

            MapRegion region = new MapRegion(name, cityMapPoint);

            if(citySite) {
                region.setCitySite(true);
            }

            if(floodPlain.getLength()!=0) {
                region.setFloodPlain(true);
            }


            regions.add(region);
            region.loadXMLPoints(pointList);
        }
    }

    public void loadRegions() throws ParserConfigurationException, SAXException, IOException {
        InputStream mapFile = getMapFile();
        Document mapDocument = getXMLDocument(mapFile);

        getMapRegions(mapDocument);
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
        for(int x=0;x<regions.size();x++) {
            MapRegion mapRegion = regions.get(x);
            mapRegion.draw(context, cityImage, asianTile);
        }
    }
}
