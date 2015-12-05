import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameMap {
    protected Canvas mapCanvas;
    protected Canvas regionCanvas;
    protected Image mapImage;

    GameMap(Image mapImage, Canvas mapCanvas, Canvas regionCanvas) {
        this.mapImage=mapImage;
        this.mapCanvas=mapCanvas;
        this.regionCanvas=regionCanvas;
    }

    void draw() {
        GraphicsContext mapCanvasContext = mapCanvas.getGraphicsContext2D();
        mapCanvasContext.drawImage(mapImage, 0, 0, 1400, 700);
    }

    void highlightRegion(MapRegion mapRegion) {
        GraphicsContext context = regionCanvas.getGraphicsContext2D();

        for (MapLine mapRegionLine : mapRegion.edges) {
            context.setLineWidth(3);
            context.setStroke(Color.BLACK);
            context.strokeLine(mapRegionLine.from.x, mapRegionLine.from.y, mapRegionLine.to.x, mapRegionLine.to.y);
        }

        for (MapLine mapRegionLine : mapRegion.edges) {
            context.setLineWidth(2);
            context.setStroke(Color.WHITE);
            context.strokeLine(mapRegionLine.from.x, mapRegionLine.from.y, mapRegionLine.to.x, mapRegionLine.to.y);
        }
    }

    void drawMapInset(MapRegion region, MapPoint mapInsetOffset) {
        GraphicsContext regionContext = regionCanvas.getGraphicsContext2D();

        double aspectRatioX = mapImage.getWidth()/regionCanvas.getWidth();
        double aspectRatioY = mapImage.getHeight()/regionCanvas.getHeight();
        double imageStartX = Double.max(0,(region.upperLeft.x-20)*aspectRatioX);
        double imageStartY = Double.max(0,(region.upperLeft.y-20)*aspectRatioY);
        double imageWidth = ((region.lowerRight.x-region.upperLeft.x)+40)*aspectRatioX;
        double imageHeight = ((region.lowerRight.y-region.upperLeft.y)+40)*aspectRatioY;

        // Make square with the maximum dimension
        imageHeight = imageWidth = Double.max(imageHeight, imageWidth);

        regionContext.drawImage(mapImage,
                imageStartX,
                imageStartY,
                imageWidth,
                imageHeight,
                mapInsetOffset.x,
                mapInsetOffset.y,
                200,
                200);

        regionContext.setStroke(Color.LIGHTGRAY);
        regionContext.setLineWidth(7);
        regionContext.strokeRoundRect(mapInsetOffset.x, mapInsetOffset.y, 203,203, 30,30);
    }
}
