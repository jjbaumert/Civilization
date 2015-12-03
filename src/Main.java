import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main extends Application {
    private MapRegions mapRegions;
    private Label coordinateLabel;
    private Canvas regionCanvas;
    private MapRegion selectedRegion;
    public ImageManager imageManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("civ.fxml"));
        primaryStage.setTitle("Civilization");

        imageManager = new ImageManager();
        imageManager.loadImages("ImageDescriptions.xml");


        Scene scene = new Scene(root, 1500, 800);
        Canvas mapCanvas = (Canvas) scene.lookup("#mapCanvas");

        regionCanvas = (Canvas) scene.lookup("#regionCanvas");
        coordinateLabel = (Label) scene.lookup("#coordinates");

        GraphicsContext mapCanvasContext = mapCanvas.getGraphicsContext2D();
        mapCanvasContext.drawImage(imageManager.get("Map"), 0, 0, 1400, 700);

        regionCanvas.setOnMouseClicked(event -> handleClick((float)event.getX(),(float)event.getY()));
        regionCanvas.setOnMouseMoved(event -> handleMove(event.getX(), event.getY()));


        readMapRegions();

        primaryStage.setScene(scene);
        primaryStage.show();

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
            regionContext.setStroke(Color.WHITE);
            selectedRegion = newSelectedRegion;
            selectedRegion.draw(regionContext, imageManager);
            System.out.println(selectedRegion.name);

            drawInfoBox(regionContext);
        }

        if(selectedRegion != null && !selectedRegion.pointInside(clickSpot)) {
            selectedRegion=null;
        }
    }

    private void drawInfoBox(GraphicsContext regionContext) {
        float boxOffsetX = 900;
        float boxOffsetY = 0;

        if((boxOffsetY+480)>700) {
            boxOffsetY = 200;
        }

        regionContext.setLineWidth(1);
        LinearGradient g
                = LinearGradient.valueOf("from 0% 0% to 100% 100%, darkgrey  0% , white 50%,  darkgrey 100%");
        regionContext.setFill(g);
        regionContext.fillRoundRect(boxOffsetX, boxOffsetY, 500, 700, 30, 30);
        regionContext.setLineWidth(3);
        regionContext.setStroke(Color.DARKGRAY);
        regionContext.strokeText(selectedRegion.name, boxOffsetX + 25, boxOffsetY + 20);
        regionContext.setLineWidth(1);
        regionContext.setStroke(Color.BLACK);
        regionContext.strokeText(selectedRegion.name, boxOffsetX + 25, boxOffsetY + 20);

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
            regionContext.strokeText(featureDescription, boxOffsetX + 285, boxOffsetY + 235);
        }

        Image mapImage = imageManager.get("Map");

        double aspectRatioX = mapImage.getWidth()/regionCanvas.getWidth();
        double aspectRatioY = mapImage.getHeight()/regionCanvas.getHeight();
        double imageStartX = Double.max(0,(selectedRegion.upperLeft.x-20)*aspectRatioX);
        double imageStartY = Double.max(0,(selectedRegion.upperLeft.y-20)*aspectRatioY);
        double imageWidth = ((selectedRegion.lowerRight.x-selectedRegion.upperLeft.x)+40)*aspectRatioX;
        double imageHeight = ((selectedRegion.lowerRight.y-selectedRegion.upperLeft.y)+40)*aspectRatioY;

        // Make square with the maximum dimension
        imageHeight = imageWidth = Double.max(imageHeight, imageWidth);

        regionContext.drawImage(mapImage,
                imageStartX,
                imageStartY,
                imageWidth,
                imageHeight,
                boxOffsetX + 285,
                boxOffsetY + 15,
                200,
                200);

        regionContext.setStroke(Color.LIGHTGRAY);
        regionContext.setLineWidth(7);
        regionContext.strokeRoundRect(boxOffsetX+284, boxOffsetY+14, 203,203, 30,30);
    }

    private void readMapRegions() throws ParserConfigurationException, SAXException, IOException {
        mapRegions = new MapRegions();
        mapRegions.loadRegions();
    }


    public static void main(String[] args) {
        launch(args);
    }
}