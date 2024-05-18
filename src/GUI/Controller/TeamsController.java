package GUI.Controller;

import BE.Geography;
import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;
import GUI.Controller.util.TeamsContainerController;
import GUI.Model.ProjectTeamsModel;
import GUI.Utility.DataModelSingleton;
import GUI.Utility.ExceptionHandler;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            projectTeamsModel = new ProjectTeamsModel();
            tPaneTeamOverview.setHgap(36);
            tPaneTeamOverview.setVgap(50);
            tPaneTeamOverview.setPadding(new Insets(16, -16, 16, -16));
            tPaneTeamOverview.setAlignment(Pos.CENTER);
            loadTeamsInTilePane();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void loadTeamsInTilePane() {
        List<ProjectTeam> teams = projectTeamsModel.getAllProjectTeamsData();
        for (ProjectTeam team : teams) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/containers/teamContainer.fxml"));
            Node teamNode = null;
            try {
                teamNode = loader.load();
            } catch (IOException e) {
                ExceptionHandler.handleException(e);
            }
            TeamsContainerController controller = loader.getController();
            controller.updateUI(team);

            teamNode.setOnMouseClicked(event -> handleTeamSelection(team));
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
            ExceptionHandler.handleException(e);
        }
    }

    private void handleTeamSelection(ProjectTeam selectedTeam) {
        DataModelSingleton.getInstance().setCurrentTeam(selectedTeam);
        showTeamDetails(selectedTeam, selectedTeam.getGeography());
    }

    private void refreshView() {
        ProjectTeam currentTeam = DataModelSingleton.getInstance().getCurrentTeam();
        UpdateProjectTeamController controller = frameController.getUpdateProjectTeamController();
        if (controller != null) {
            controller.initDataFromTeam();
        }
    }

    @FXML
    private void openProjectTeamView(ActionEvent actionEvent) {
        frameController.loadCreateTeamView();
    }
}