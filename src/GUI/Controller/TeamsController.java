package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import GUI.Controller.util.TeamsController1;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeamsController implements Initializable {

    @FXML
    private TilePane tPaneTeamOverview;


    public void loadTeamsInTilePane() throws IOException {
        for (int i = 0; i < 10; i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/containers/teamContainer.fxml"));
            Node teamsView = loader.load();
            TeamsController1 teamsController1 = loader.getController();
            // teamsController1.setData("Some URL"); // Commented out
            tPaneTeamOverview.getChildren().add(teamsView);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            tPaneTeamOverview.setPrefColumns(3);
            tPaneTeamOverview.setHgap(10);
            tPaneTeamOverview.setVgap(10);
            tPaneTeamOverview.setPadding(new Insets(16, 10, 10, 24));
            tPaneTeamOverview.setAlignment(Pos.CENTER);
            loadTeamsInTilePane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Instance of FrameController to control the main frame of the application
    private FrameController frameController;



    /**
     * Constructor for the TeamsController class.
     * It initializes the frameController variable with the instance of FrameController.
     */
    public TeamsController() {
        this.frameController = FrameController.getInstance();
    }




    @FXML
    private void openProjectTeamView(ActionEvent actionEvent) {
        frameController.loadCreateTeamView();
    }
}
