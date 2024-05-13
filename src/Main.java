import GUI.Controller.FrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/frameView.fxml"));

            Parent root = loader.load();

            primaryStage.setTitle("Schneider Electric");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}