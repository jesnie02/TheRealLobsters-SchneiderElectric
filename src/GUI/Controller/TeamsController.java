package GUI.Controller;

import BE.ProjectTeam;
import GUI.Controller.util.TeamsContainerController;
import GUI.Model.ProjectTeamsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TeamsController implements Initializable {

    @FXML
    private TilePane tPaneTeamOverview;
    private ProjectTeamsModel projectTeamsModel;

    public void loadTeamsInTilePane() throws Exception {
        List<ProjectTeam> teams = projectTeamsModel.getAllProjectTeamsData(); // Assuming this method returns the list of all teams
        for (ProjectTeam team : teams) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/containers/teamContainer.fxml"));
            Node teamNode = loader.load();
            TeamsContainerController controller = loader.getController();  // Your controller for the team container
            controller.updateUI(team);  // Assuming you have a method to set the team in the controller
            teamNode.setOnMouseClicked(this::openTeamDetailView);
            tPaneTeamOverview.getChildren().add(teamNode);
        }
    }

    private void openTeamDetailView(MouseEvent event) {
        frameController.loadDetailView();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            projectTeamsModel = new ProjectTeamsModel();
            tPaneTeamOverview.setHgap(36);
            tPaneTeamOverview.setVgap(50);
            tPaneTeamOverview.setPadding(new Insets(16, -16, 16, -16));
            tPaneTeamOverview.setAlignment(Pos.CENTER);
            loadTeamsInTilePane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
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
       frameController = FrameController.getInstance();
    }

    @FXML
    private void openProjectTeamView(ActionEvent actionEvent) {
        frameController.loadCreateTeamView();
    }


}
