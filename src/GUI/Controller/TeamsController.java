package GUI.Controller;

import BE.Geography;
import BE.ProjectTeam;
import GUI.Controller.util.TeamsContainerController;
import GUI.Model.ProjectTeamsModel;
import GUI.Utility.DataModelSingleton;
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void loadTeamsInTilePane() throws Exception {
        List<ProjectTeam> teams = projectTeamsModel.getAllProjectTeamsData();
        for (ProjectTeam team : teams) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/containers/teamContainer.fxml"));
            Node teamNode = loader.load();
            TeamsContainerController controller = loader.getController();
            controller.updateUI(team);

            teamNode.setOnMouseClicked(event -> {
                try {
                    handleTeamSelection(team);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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

    private void handleTeamSelection(ProjectTeam selectedTeam) throws Exception {
         DataModelSingleton.getInstance().setCurrentTeam(selectedTeam);
         refreshView();
    }

    private void refreshView() throws Exception {
        ProjectTeam currentTeam = DataModelSingleton.getInstance().getCurrentTeam();
        UpdateProjectTeamController controller = frameController.getUpdateProjectTeamController();
        if (controller != null) {
            controller.initDataFromTeam();
        } else {
            System.err.println("UpdateProjectTeamController is null");
        }
    }


    @FXML
    private void openProjectTeamView(ActionEvent actionEvent) {
        frameController.loadCreateTeamView();
    }



}
