import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;

public class MapRegion {
    class MapRegionAttributes {
        private boolean land;
        private boolean water;
        private boolean citySite;
        private boolean floodPlain;
        private boolean volcanic;

        boolean isLand() {
            return land;
        }

        boolean isWater() {
            return water;
        }

        boolean hasCitySite() {
            return citySite;
        }

        boolean isFloodPlain() {
            return floodPlain;
        }

        boolean isVolcanic() {
            return volcanic;
        }

        boolean isOcean() {
            return !land && water;
        }
    }

    String name;
    int agriculturalValue;
    boolean land;
    boolean water;
    boolean citySite;
    MapPoint cityLocation;
    boolean floodPlain;
    boolean volcanic;

    Vector<MapLine> edges;

    MapPoint upperLeft, lowerRight;

    MapRegion(String name, MapPoint cityLocation) {
        this.name = name;
        this.cityLocation = cityLocation;
        edges = new Vector<>();

        //
        // Create the bounding box by making the top left (MAX_VALUE, MAX_VALUE)
        // and bottom right (MIN_VALUE, MIN_VALUE)
        //

        upperLeft = new MapPoint(Integer.MAX_VALUE,Integer.MAX_VALUE);
        lowerRight = new MapPoint(Integer.MIN_VALUE, Integer.MIN_VALUE);

        land=false;
        water=false;
        citySite=false;
        floodPlain=false;
        volcanic=false;
    }

    void setLand(boolean value) {
        land=value;
    }

    void setWater(boolean value) {
        water=value;
    }

    void setCitySite(boolean value) {
        citySite=value;
    }

    void setFloodPlain(boolean value) {
        floodPlain=value;
    }

    void setAgriculturalValue(int value) {
        agriculturalValue=value;
    }

    void addEdge(MapLine edge) {
        edges.add(edge);

        //
        // Precalculate bounding box to limit calculations on pointInside calculations
        //

        upperLeft.x = min(upperLeft.x, edge.from.x, edge.to.x);
        upperLeft.y = min(upperLeft.y, edge.from.y, edge.to.y);
        lowerRight.x = max(lowerRight.x, edge.from.x, edge.to.x);
        lowerRight.y = max(lowerRight.y, edge.from.y, edge.to.y);
    }

    float min(float f1, float f2, float f3) {
        if(f1<=f2) {
            if(f3<=f1) {
                return f3;
            } else {
                return f1;
            }
        } else {
            if(f3<=f2) {
                return f3;
            } else {
                return f2;
            }
        }
    }

    float max(float f1, float f2, float f3) {
        if(f1>=f2) {
            if(f3>=f1) {
                return f3;
            } else {
                return f1;
            }
        } else {
            if(f3>=f2) {
                return f3;
            } else {
                return f2;
            }
        }
    }

    public boolean pointInside(MapPoint point) {
        if(!inBoundingBox(point)) {
            return false;
        }

        //
        // If the ray touches the edges an odd number of times then
        // the point is inside of the polygon. The endpoints on the
        // edges cause the points there the edges meet to show up
        // twice.
        //

        MapLine crossingsLine = new MapLine(point, new MapPoint(3000,point.y));

        int crossings = 0;
        for(MapLine mapLine: edges) {
            MapPoint intersectionPoint = mapLine.intersectsAt(crossingsLine);

            if(intersectionPoint == null) // lines are parallel
                continue;

            //
            // Add two if the points are mid-segment. Add one if the
            // points meet at the endpoint. Test for odd crossing by
            // looking at the second bit in crossings being set.
            //
            // Algorithm makes the assumption that no two endpoints
            // for a region lie on the same Y line. If they do you can
            // get a false positive to the right.
            //
            // if this is a problem you could draw a ray in both directions
            // and take the only region that meets both criteria
            //

            if(mapLine.pointOnLine(intersectionPoint) && crossingsLine.pointOnLine(intersectionPoint)) {
                if(mapLine.from.equal(intersectionPoint) || mapLine.to.equal(intersectionPoint)) {
                    crossings += 1;
                } else {
                    crossings += 2;
                }
            }
        }

        return (crossings & 0x02) == 0x02; // odd crossings means we are inside.
    }

    public boolean inBoundingBox(MapPoint point) {
        return point.x>upperLeft.x && point.y>upperLeft.y && point.x<lowerRight.x && point.y<lowerRight.y;
    }

    public void loadXMLPoints(NodeList pointList) {
        MapPoint firstPoint = null;
        MapPoint prevPoint = null;

        for(int pointIndex=0; pointIndex<pointList.getLength(); pointIndex++) {
            MapPoint mapPoint = loadXMLPoint(pointList.item(pointIndex));

            //
            // keep track of first point so we can close the polygon
            //

            if(firstPoint==null) {
                firstPoint = mapPoint;
            }

            //
            // keep track of previous point so we can draw an edge
            // cannot draw the first edge until we have at least two points
            //

            if(prevPoint != null) {
                addEdge(new MapLine(prevPoint, mapPoint));
            }

            prevPoint = mapPoint;
        }

        //
        // Close the polygon
        //

        if(firstPoint != prevPoint) {
            addEdge(new MapLine(prevPoint, firstPoint));
        }
    }

    private MapPoint loadXMLPoint(Node point) {
        Element pointElements = (Element) point;

        float x = Float.parseFloat(pointElements.getAttribute("x"));
        float y = Float.parseFloat(pointElements.getAttribute("y"));

        return new MapPoint(x,y);
    }

    void draw(GraphicsContext context, Image cityImage, Image asianTile) {
        for (MapLine mapRegionLine : edges) {
            context.setLineWidth(3);
            context.setStroke(Color.BLACK);
            context.strokeLine(mapRegionLine.from.x, mapRegionLine.from.y, mapRegionLine.to.x, mapRegionLine.to.y);
        }

        for (MapLine mapRegionLine : edges) {
            context.setLineWidth(2);
            context.setStroke(Color.WHITE);
            context.strokeLine(mapRegionLine.from.x, mapRegionLine.from.y, mapRegionLine.to.x, mapRegionLine.to.y);
        }

        if(true) {
            context.setFill(Color.BLUE);
            context.fillOval(cityLocation.x-2,cityLocation.y-2,25,25);
            context.setFill(new ImagePattern(cityImage));
            context.fillOval(cityLocation.x,cityLocation.y,20,20);

            context.setFill(Color.BLUE);
            context.fillRect(cityLocation.x+30, cityLocation.y+30, 60,60);
            context.setFill(new ImagePattern(asianTile));
            context.fillRect(cityLocation.x+33, cityLocation.y+33, 55,55);

            context.setStroke(Color.WHITE);
            context.setLineWidth(3);
            context.strokeText("x3",cityLocation.x+62, cityLocation.y+53);
            context.setStroke(Color.BLACK);
            context.setLineWidth(1);
            context.strokeText("x3",cityLocation.x+62, cityLocation.y+53);

        }
    }

    @Override
    public String toString() {
        String returnString = "{" + name + ": [";

        for(MapLine edge: edges) {
            returnString += edge + ",";
        }

        return returnString + "]}";
    }
}