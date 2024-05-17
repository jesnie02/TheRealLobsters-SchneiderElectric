package GUI.Controller.util;

import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import GUI.Controller.TeamsController;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Model.ProjectTeamsModel;
import GUI.Utility.DataModelSingleton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TeamsContainerController implements Initializable {

    private static TeamsContainerController instance;
    private ProjectTeam selectedTeam;
    ProjectTeamsModel projectTeamsModel;
    CountryModel countryModel;
    GeographyModel geographyModel;

    private Map<Integer, Geography> geographyMap = new HashMap<>();
    private List<Geography> geographies;



    @FXML
    private Label lblAnnualAVGTeamCostContainer,lblGeographyContainer, lblNumberOfMembersContainer, lblTeamNameContainer, lblLocationContainer;


    public TeamsContainerController() {
        instance = this;
    }

    public static TeamsContainerController getInstance() {
        return instance;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            projectTeamsModel = new ProjectTeamsModel();
            geographyModel = new GeographyModel();
            loadGeographyMap();
        } catch (Exception e) {
                    handleException(e);//TODO: Handle this exception
        }
    }

    public void updateUI(ProjectTeam team) throws Exception {
        this.selectedTeam = team;

        Platform.runLater(() -> {
            updateTeamInformation(team);
            updateGeographyInformation(team);
        });
        lblTeamNameContainer.getParent().setOnMouseClicked(event -> openTeamDetailView());
    }

    private void updateTeamInformation(ProjectTeam team) {
        lblTeamNameContainer.setText(team.getTeamName());
        lblNumberOfMembersContainer.setText(String.valueOf(team.getNumberOfProfiles()));
        lblAnnualAVGTeamCostContainer.setText(String.format("%.2f", team.getAvgAnnualSalary()));

    }

    private void openTeamDetailView() {
        TeamsController teamsController = TeamsController.getInstance();
        Geography teamGeography = geographyMap.get(selectedTeam.getGeographyId());
        teamsController.showTeamDetails(selectedTeam, teamGeography);
    }

    private void updateGeographyInformation(ProjectTeam team) {
       int teamGeographyId = team.getGeographyId();
       Geography teamGeography = geographyMap.get(teamGeographyId);
        if (teamGeography != null) {
            lblGeographyContainer.setText(teamGeography.getGeographyName());
        } else {
            lblLocationContainer.setText("No Geography");
        }
    }

    private void loadGeographyMap() {
        try {
            geographyMap = geographyModel.getGeographyMap();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        e.printStackTrace(); // TODO: Handle this exception appropriately
    }

    public ProjectTeamsModel getTeam(){
        return this.projectTeamsModel;
    }
}