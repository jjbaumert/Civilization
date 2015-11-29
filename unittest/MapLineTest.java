import org.junit.Test;

import static org.junit.Assert.*;

public class MapLineTest {
    final float FROM_X = 0, TO_X = 10000, FROM_Y = 0, TO_Y = 3000;

    private MapPoint getMiddleLine(MapPoint start, MapPoint end) {
        return new MapPoint((start.x+end.x)/2,(start.y+end.y)/2);
    }

    public MapPoint getOffEdge(MapPoint point, int xOffset, int yOffset) {
        return new MapPoint(point.x+xOffset, point.y+yOffset);
    }

    @Test
    public void testPointOnLine_NotOnLine() throws Exception {
        MapPoint start = new MapPoint(FROM_X,FROM_Y);
        MapPoint end = new MapPoint(TO_X,TO_Y);
        MapPoint middle = getMiddleLine(start,end);
        MapLine edge = new MapLine(start,end);

        for(int offset=1;offset<30;offset++) {
            MapPoint offEdgeY = getOffEdge(middle, 0, offset);
            assertFalse(edge.pointOnLine(offEdgeY));

            offEdgeY = getOffEdge(middle, 0, -offset);
            assertFalse(edge.pointOnLine(offEdgeY));
        }
    }

    @Test
    public void testPointOnLine_HappyPath() throws Exception {
        MapPoint start = new MapPoint(FROM_X,FROM_Y);
        MapPoint end = new MapPoint(TO_X,TO_Y);
        MapPoint middle = getMiddleLine(start,end);
        MapLine edge = new MapLine(start,end);

        assertTrue(edge.pointOnLine(start));
        assertTrue(edge.pointOnLine(end));
        assertTrue(edge.pointOnLine(middle));
    }

    @Test
    public void testPointOnLine_HappyPathManyPoints() throws Exception {
        MapPoint start = new MapPoint(FROM_X, FROM_Y);
        MapPoint end = new MapPoint(TO_X, TO_Y);
        MapLine edge = new MapLine(start, end);

        double a = FROM_Y-TO_Y;
        double b = FROM_X-TO_X;
        double c = a*FROM_X+b*FROM_Y;

        for(int x=(int)FROM_X; x<=(int)TO_X; x++) {
            double calculatedY = (a*x-c)/b;
            assertTrue("Checking if "+x+","+calculatedY+" is on "+edge,edge.pointOnLine(new MapPoint(x,(int)calculatedY)));
        }
    }

    @Test
    public void testPointNotOnLine_BeyondEnds() throws Exception {
        MapPoint start = new MapPoint(FROM_X, FROM_Y);
        MapPoint end = new MapPoint(TO_X, TO_Y);
        MapLine edge = new MapLine(start, end);

        double a = FROM_Y-TO_Y;
        double b = FROM_X-TO_X;
        double c = a*FROM_X+b*FROM_Y;

        assertTrue("Test assumes FROM_X is smaller than TO_X", FROM_X<TO_X);

        float x = FROM_X-1;
        double calculatedY = (a*x-c)/b;
        assertFalse("Checking if " + x + "," + calculatedY + " is not on " + edge,
                edge.pointOnLine(new MapPoint(x, (int) calculatedY)));

        x = TO_X+1;
        calculatedY = (a*x-c)/b;
        assertFalse("Checking if "+x+","+calculatedY+" is not on "+edge,
                edge.pointOnLine(new MapPoint(x,(int)calculatedY)));
    }

    @Test
    public void testIntersectsWith_HappyPath() throws Exception {
        MapLine line1 = new MapLine(new MapPoint(0,100), new MapPoint(100,0));
        MapLine line2 = new MapLine(new MapPoint(0,0), new MapPoint(100,100));

        assertTrue(line1.intersectsWith(line2));
        assertTrue(line2.intersectsWith(line1));
    }

    @Test
    public void testIntersectsWith_ParallelLines() throws Exception {
        MapLine line1 = new MapLine(new MapPoint(0,0), new MapPoint(0,100));
        MapLine line2 = new MapLine(new MapPoint(5,0), new MapPoint(5,100));

        assertFalse(line1.intersectsWith(line2));
        assertFalse(line2.intersectsWith(line1));
    }

    @Test
    public void testIntersectsWith_NoIntersectBefore() throws Exception {
        MapLine line1 = new MapLine(new MapPoint(0,100), new MapPoint(100,0));
        MapLine line2 = new MapLine(new MapPoint(0,0), new MapPoint(48,48));

        assertFalse(line1.intersectsWith(line2));
        assertFalse(line2.intersectsWith(line1));
    }

    @Test
    public void testIntersectsWith_NoIntersectAfter() throws Exception {
        MapLine line1 = new MapLine(new MapPoint(0,100), new MapPoint(100,0));
        MapLine line2 = new MapLine(new MapPoint(52,52), new MapPoint(100,100));

        assertFalse(line1.intersectsWith(line2));
        assertFalse(line2.intersectsWith(line1));
    }
}