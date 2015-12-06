import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public GameMap gameMap;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("civ.fxml"));
        primaryStage.setTitle("Civilization");

        Scene scene = new Scene(root, 1500, 800);

        gameMap = new GameMap(scene);
        gameMap.draw();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}