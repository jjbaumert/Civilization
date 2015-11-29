import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MapRegionTest {
    MapRegions mapRegions;
    Map<String, MapRegion> regionMap;

    @Before
    public void setUp() throws Exception {
        mapRegions = new MapRegions();
        mapRegions.loadRegions();
        regionMap = new HashMap<>();

        for(MapRegion mapRegion: mapRegions.regions) {
            regionMap.put(mapRegion.name, mapRegion);
        }
    }

    @Test
    public void testPointInside_HappyPath() throws Exception {
        assertTrue("76,80 is not inside Britannia",
                regionMap.get("Britannia").pointInside(new MapPoint(76,79)));
        assertTrue("57,69 is not inside Hibernia",
                regionMap.get("Hibernia").pointInside(new MapPoint(57,69)));
        assertTrue("55,90 is not inside the Atlantic",
                regionMap.get("Atlantic").pointInside(new MapPoint(55,90)));
        assertTrue("8,262 is not inside Northern Iberia",
                regionMap.get("Northern Iberia").pointInside(new MapPoint(8,262)));
        assertTrue("301,307 is not inside Vocontii",
                regionMap.get("Vocontii").pointInside(new MapPoint(301,307)));
    }

    @Test
    public void testPointInside_InvalidNearby() throws Exception {
        assertFalse("76,80 is inside Hibernia",
                regionMap.get("Hibernia").pointInside(new MapPoint(76,79)));
        assertFalse("76,80 is inside Atlantic",
                regionMap.get("Atlantic").pointInside(new MapPoint(76,79)));
    }

    @Test
    public void testPointInside_InvalidAtEndpointOnly() throws Exception {
        assertFalse("301,307 is not inside Gallia",
                regionMap.get("Gallia").pointInside(new MapPoint(301, 307)));
    }

    @Test
    public void testPointInside_AtIntersection() throws Exception {
        assertTrue("76,80 is not inside Britannia",
                regionMap.get("Britannia").pointInside(new MapPoint(76, 80)));
    }

    @Test
    public void testMin() throws Exception {
        MapRegion mapRegion = regionMap.get("Britannia");
        assertTrue(mapRegion.min(-1, 0, 1)==-1);
        assertTrue(mapRegion.min(0, 1, -1)==-1);
        assertTrue(mapRegion.min(1, -1, 0)==-1);
        assertTrue(mapRegion.min(0, -1, 1)==-1);
        assertTrue(mapRegion.min(1, 0, -1)==-1);
        assertTrue(mapRegion.min(-1, 1, 0)==-1);
    }

    @Test
    public void testMax() throws Exception {
        MapRegion mapRegion = regionMap.get("Britannia");
        assertTrue(mapRegion.max(-1, 0, 1)==1);
        assertTrue(mapRegion.max(0, 1, -1)==1);
        assertTrue(mapRegion.max(1, -1, 0)==1);
        assertTrue(mapRegion.max(0, -1, 1)==1);
        assertTrue(mapRegion.max(1, 0, -1)==1);
        assertTrue(mapRegion.max(-1, 1, 0)==1);
    }
}