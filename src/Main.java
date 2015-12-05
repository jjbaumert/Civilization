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
            gameMap.drawInfoBox(selectedRegion);
        }

        if(selectedRegion != null && !selectedRegion.pointInside(clickSpot)) {
            selectedRegion=null;
        }
    }

    private void readMapRegions() throws ParserConfigurationException, SAXException, IOException {
        mapRegions = new MapRegions();
        mapRegions.loadRegions();
    }


    public static void main(String[] args) {
        launch(args);
    }
}