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

    public ImageManager imageManager;

    private MapRegion selectedRegion;
    private MapRegions mapRegions;

    GameMap(Scene scene) throws IOException, SAXException, ParserConfigurationException {
        mapCanvas = (Canvas) scene.lookup("#mapCanvas");
        regionCanvas = (Canvas) scene.lookup("#regionCanvas");
        coordinateLabel = (Label) scene.lookup("#coordinates");

        imageManager = new ImageManager();
        imageManager.loadImages("ImageDescriptions.xml");

        regionCanvas.setOnMouseClicked(event -> handleClick((float)event.getX(),(float)event.getY()));
        regionCanvas.setOnMouseMoved(event -> handleMove(event.getX(), event.getY()));

        mapRegions = new MapRegions();
        mapRegions.loadRegions();
    }

    void draw() {
        GraphicsContext mapCanvasContext = mapCanvas.getGraphicsContext2D();
        mapCanvasContext.drawImage(imageManager.get("Map"), 0, 0, 1400, 700);
    }

    void highlightRegion(MapRegion mapRegion) {
        drawRegionBorder(mapRegion, Color.BLACK, 3);
        drawRegionBorder(mapRegion, Color.WHITE, 2);
    }

    private void drawRegionBorder(MapRegion mapRegion, Color color, int lineWidth) {
        GraphicsContext context = regionCanvas.getGraphicsContext2D();

        for (MapLine mapRegionLine : mapRegion.edges) {
            context.setLineWidth(lineWidth);
            context.setStroke(color);
            context.strokeLine(mapRegionLine.from.x, mapRegionLine.from.y, mapRegionLine.to.x, mapRegionLine.to.y);
        }
    }

    void drawInfoBox(MapRegion selectedRegion) {
        MapPoint boxOffset = new MapPoint(900,0);

        if((boxOffset.y+480)>700) {
            boxOffset.y = 200;
        }

        drawInfoBoxBackground(boxOffset);

        strokeText(boxOffset.x+25, boxOffset.y+20, selectedRegion.name);

        String featureDescription = getFeatureDescriptions();

        if(!featureDescription.equals("")) {
            strokeText(boxOffset.x + 285, boxOffset.y + 235, featureDescription);
        }

        drawMapInset(new MapPoint(boxOffset.x+285, boxOffset.y+15));
    }

    private void drawInfoBoxBackground(MapPoint boxOffset) {
        GraphicsContext regionContext = regionCanvas.getGraphicsContext2D();

        regionContext.setLineWidth(1);
        LinearGradient g
                = LinearGradient.valueOf("from 0% 0% to 100% 100%, darkgrey  0% , white 50%,  darkgrey 100%");
        regionContext.setFill(g);
        regionContext.fillRoundRect(boxOffset.x, boxOffset.y, 500, 700, 30, 30);
    }

    private void strokeText(float x, float y, String text) {
        GraphicsContext regionContext = regionCanvas.getGraphicsContext2D();

        regionContext.setLineWidth(3);
        regionContext.setStroke(Color.WHITE);
        regionContext.strokeText(text, x, y);
        regionContext.setLineWidth(1);
        regionContext.setStroke(Color.BLACK);
        regionContext.strokeText(text, x, y);
    }

    private String getFeatureDescriptions() {
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

        return featureDescription;
    }

    void drawMapInset(MapPoint mapInsetOffset) {
        Image mapImage = imageManager.get("Map");

        GraphicsContext regionContext = regionCanvas.getGraphicsContext2D();

        MapPoint imageUpperLeft = getImageLocationFromScreen(selectedRegion.upperLeft);
        MapPoint imageLowerRight = getImageLocationFromScreen(selectedRegion.lowerRight);

        imageUpperLeft.x = Float.max(0, imageUpperLeft.x-20);
        imageUpperLeft.y = Float.max(0, imageUpperLeft.y-10);
        imageLowerRight.x = Float.min((float)mapCanvas.getWidth(), imageLowerRight.x+20);
        imageLowerRight.y = Float.min((float)mapCanvas.getHeight(), imageLowerRight.y+10);

        float imageHeight = imageLowerRight.y-imageUpperLeft.y;
        float imageWidth = imageLowerRight.x-imageUpperLeft.x;

        // Make square with the maximum dimension
        imageHeight = imageWidth = Float.max(imageHeight, imageWidth);

        regionContext.drawImage(mapImage,
                imageUpperLeft.x,
                imageUpperLeft.y,
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

    MapPoint getImageLocationFromScreen(MapPoint mapPoint) {
        Image mapImage = imageManager.get("Map");

        float aspectRatioX = (float)(mapImage.getWidth()/regionCanvas.getWidth());
        float aspectRatioY = (float)(mapImage.getHeight()/regionCanvas.getHeight());

        float imageStartX = Float.max(0,(mapPoint.x)*aspectRatioX);
        float imageStartY = Float.max(0,(mapPoint.y)*aspectRatioY);

        return new MapPoint(imageStartX,imageStartY);
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
