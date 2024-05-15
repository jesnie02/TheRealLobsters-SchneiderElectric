package GUI.Controller;

import BE.Geography;
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
    private static TeamsController instance;


    // Instance of FrameController to control the main frame of the application
    private FrameController frameController;


    public TeamsController() {
        this.frameController = FrameController.getInstance();
        instance = this;
    }

    public static TeamsController getInstance() {
        return instance;
    }


    public void loadTeamsInTilePane() throws Exception {
        List<ProjectTeam> teams = projectTeamsModel.getAllProjectTeamsData(); // Assuming this method returns the list of all teams
        for (ProjectTeam team : teams) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/containers/teamContainer.fxml"));
            Node teamNode = loader.load();
            TeamsContainerController controller = loader.getController();  // Your controller for the team container
            controller.updateUI(team);  // Assuming you have a method to set the team in the controller
            tPaneTeamOverview.getChildren().add(teamNode);
        }
    }

    public void showTeamDetails(ProjectTeam team, Geography geography) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/teamDetailsView.fxml"));
            Node teamNode = loader.load();
            TeamDetailsController controller = loader.getController();
            controller.updateUI(team, geography);
            frameController.setMainView(teamNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @FXML
    private void openProjectTeamView(ActionEvent actionEvent) {
        frameController.loadCreateTeamView();
    }



}
