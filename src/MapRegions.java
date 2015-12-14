import GameState.XMLDataFile;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Vector;

public class MapRegions {
    final String MAP_FILE_NAME = "map/MapRegions.xml";

    XMLDataFile mapFile;

    Vector<MapRegion> regions;

    public MapRegions() throws ParserConfigurationException, SAXException, IOException {
        regions = new Vector<>();
        mapFile = new XMLDataFile(MAP_FILE_NAME);
    }

    public void loadRegions() throws ParserConfigurationException, SAXException, IOException {
        NodeList mapRegionList = mapFile.getResourceItems("mapregion");

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

    public void draw(GraphicsContext context, ImageManager imageManager) {
        for (MapRegion mapRegion : regions) {
            mapRegion.draw(context, imageManager);
        }
    }
}
