import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class GameMap {
    private Canvas mapCanvas;
    private Canvas regionCanvas;
    private Label coordinateLabel;

    private Image mapImage;

    private MapRegion selectedRegion;
    private MapRegions mapRegions;

    GameMap(ImageManager imageManager, Scene scene) throws IOException, SAXException, ParserConfigurationException {
        mapCanvas = (Canvas) scene.lookup("#mapCanvas");
        regionCanvas = (Canvas) scene.lookup("#regionCanvas");
        mapImage = imageManager.get("Map");

        coordinateLabel = (Label) scene.lookup("#coordinates");
        regionCanvas.setOnMouseClicked(event -> handleClick((float)event.getX(),(float)event.getY()));
        regionCanvas.setOnMouseMoved(event -> handleMove(event.getX(), event.getY()));

        mapRegions = new MapRegions();
        mapRegions.loadRegions();
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

    void drawInfoBox(MapRegion selectedRegion) {
        GraphicsContext regionContext = regionCanvas.getGraphicsContext2D();

        MapPoint boxOffset = new MapPoint(900,0);

        if((boxOffset.y+480)>700) {
            boxOffset.y = 200;
        }

        regionContext.setLineWidth(1);
        LinearGradient g
                = LinearGradient.valueOf("from 0% 0% to 100% 100%, darkgrey  0% , white 50%,  darkgrey 100%");
        regionContext.setFill(g);
        regionContext.fillRoundRect(boxOffset.x, boxOffset.y, 500, 700, 30, 30);
        regionContext.setLineWidth(3);
        regionContext.setStroke(Color.DARKGRAY);
        regionContext.strokeText(selectedRegion.name, boxOffset.x + 25, boxOffset.y + 20);
        regionContext.setLineWidth(1);
        regionContext.setStroke(Color.BLACK);
        regionContext.strokeText(selectedRegion.name, boxOffset.x + 25, boxOffset.y + 20);

        String featureDescription = "";

        if(selectedRegion.attributes.hasCitySite()) {
            featureDescription += "City Site";
        }

        if(selectedRegion.attributes.hasFloodPlain()) {
            if(!featureDescription.equals("")) {
                featureDescription += ", ";
            }
            featureDescription += "Flood Plain";
        }

        if(selectedRegion.attributes.hasVolcano()) {
            if(!featureDescription.equals("")) {
                featureDescription += ", ";
            }
            featureDescription += "Volcano";
        }

        if(!featureDescription.equals("")) {
            regionContext.strokeText(featureDescription, boxOffset.x + 285, boxOffset.y + 235);
        }

        MapPoint mapInsetOffset = new MapPoint(boxOffset.x+285, boxOffset.y+15);
        drawMapInset(selectedRegion, mapInsetOffset);
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

    private void handleMove(double x, double y) {
        String coordinateString = "";

        coordinateString += x + ", " + y;
        coordinateLabel.setText(coordinateString);

    }

    void handleClick(float x, float y) {
        MapPoint clickSpot = new MapPoint(x,y);

        System.out.println("<point x=\""+(int)x+"\" y=\""+(int)y+"\"/>");

        GraphicsContext regionContext = regionCanvas.getGraphicsContext2D();
        regionContext.clearRect(0,0,1400,700);

        MapRegion newSelectedRegion = mapRegions.getRegionByMapPoint(clickSpot);

        if(newSelectedRegion==selectedRegion) {
            selectedRegion = null;
        } else if(newSelectedRegion!=null) {
            selectedRegion = newSelectedRegion;
            highlightRegion(selectedRegion);
            System.out.println(selectedRegion.name);
            drawInfoBox(selectedRegion);
        }

        if(selectedRegion != null && !selectedRegion.pointInside(clickSpot)) {
            selectedRegion=null;
        }
    }
}
