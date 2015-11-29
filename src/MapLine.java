
public class MapLine {
    final float TOLERANCE = 0.99f;

    MapPoint from;
    MapPoint to;

    MapLine(MapPoint from, MapPoint to) {
        this.from = from;
        this.to = to;
    }

    boolean pointOnLine(MapPoint point) {
        // Check to see if the point is before or after the x-axis
        if(xBeforeLine(point.x) || xAfterLine(point.x)) {
            return false;
        }

        float slope = slope();

        // Plug in the "from" point to solve for offset
        float offset = from.y - slope*from.x;

        // Calculate y
        float calcY = slope*point.x + offset;

        // Compare y to the supplied point allowing for floating point tolerance
        return Math.abs(point.y-calcY)<TOLERANCE;
    }

    public float slope() {
        float rise = to.y - from.y;
        float run = to.x - from.x;
        return rise/run;
    }

    private boolean xBeforeLine(float x) {
        return to.x > x && from.x > x;
    }

    private boolean xAfterLine(float x) {
        return to.x < x && from.x < x;
    }

    boolean intersectsWith(MapLine line) {
        MapPoint mapPoint = intersectsAt(line);

        return mapPoint != null && line.pointOnLine(mapPoint) && pointOnLine(mapPoint);
    }

    public MapPoint intersectsAt(MapLine line) {
        //
        // y = mx + b
        //
        // For two lines
        //
        // Y1 = M1*X1 + B1
        // Y2 = M2*X2 + B2
        //
        // Subtract them
        //
        // Y1 - Y2 = M1*X1 - M2*X2 + B1 - B2
        //
        // Since they intersect at the equal point Y1=Y2 and X1=X2
        //
        // Y - Y = M1*X - M2*X + (B1 - B2)
        //
        // Solving for X
        //
        // -(B1 - B2) = (M1 - M2)*X
        // X = -(B1 - B2) / (M1 - M2)  <--- M1 == M2 then lines are parallel
        //
        // Solving for Y
        //
        // Y = M1*X + B1
        //

        float M1 = slope();
        float B1 = from.y - M1*from.x;

        float M2 = line.slope();
        float B2 = line.from.y - M2*line.from.x;

        if((M1-M2) == 0) { // slopes the equal then lines are parallel
            return null;
        }

        float x = -(B1-B2)/(M1-M2);
        float y = M1*x + B1;

        return new MapPoint(x,y);
    }

    @Override
    public String toString() {
        return "["+from+","+to+"]";
    }
}
