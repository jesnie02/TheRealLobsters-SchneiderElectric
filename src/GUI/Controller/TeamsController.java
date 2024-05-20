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
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
public class TeamsController implements Initializable {

    @FXML
    private TilePane tPaneTeamOverview;
    private ProjectTeamsModel projectTeamsModel;
    private TeamsContainerController teamsContainerController;
    private static TeamsController instance;

    // Instance of FrameController to control the main frame of the application
    private FrameController frameController;

    public TeamsController() {
        this.frameController = FrameController.getInstance();
        teamsContainerController = new TeamsContainerController();
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
        tPaneTeamOverview.getChildren().clear();
        teamsContainerController.initialize();
        List<ProjectTeam> teams = projectTeamsModel.getAllProjectTeamsData();
        for (ProjectTeam team : teams) {
            VBox teamBox = teamsContainerController.getTeamVBox(team.getTeamId());
            if (teamBox != null) {
                final Geography currentGeography = team.getGeography();
                teamBox.setOnMouseClicked(event -> showTeamDetails(team, currentGeography));
                tPaneTeamOverview.getChildren().add(teamBox);

                if (currentGeography == null) {
                    System.out.println("Geography object in team is null");
                } else {
                    System.out.println("Geography object in team: " + currentGeography);
                }
            }
        }
    }

    public void showTeamDetails(ProjectTeam team, Geography geography) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/teamDetailsView.fxml"));
            Node teamNode = loader.load();
            TeamDetailsController controller = loader.getController();
            controller.updateUI(team, geography);
            System.out.println("gummi" + geography);
            frameController.setMainView(teamNode);
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void handleTeamSelection(ProjectTeam selectedTeam) {

        DataModelSingleton.getInstance().setCurrentTeam(selectedTeam);
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