import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
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
    public GameMap gameMap;

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

        gameMap = new GameMap(imageManager.get("Map"), mapCanvas, regionCanvas);
        gameMap.draw();

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
            selectedRegion = newSelectedRegion;
            gameMap.highlightRegion(selectedRegion);
            System.out.println(selectedRegion.name);
            drawInfoBox(regionContext);
        }

        if(selectedRegion != null && !selectedRegion.pointInside(clickSpot)) {
            selectedRegion=null;
        }
    }

    private void drawInfoBox(GraphicsContext regionContext) {
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
        gameMap.drawMapInset(selectedRegion, mapInsetOffset);


    }

    private void readMapRegions() throws ParserConfigurationException, SAXException, IOException {
        mapRegions = new MapRegions();
        mapRegions.loadRegions();
    }


    public static void main(String[] args) {
        launch(args);
    }
}