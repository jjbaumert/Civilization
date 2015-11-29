
public class MapPoint {
    float x, y;

    MapPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "["+x+","+y+"]";
    }

    public boolean equal(MapPoint point) {
        return point.x == x && point.y == y;
    }
}
