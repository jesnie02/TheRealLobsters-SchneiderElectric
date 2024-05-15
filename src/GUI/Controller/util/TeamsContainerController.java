package GUI.Controller.util;

import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import GUI.Controller.TeamsController;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Model.ProjectTeamsModel;
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

    private ProjectTeam selectedTeam;
    ProjectTeamsModel projectTeamsModel;
    CountryModel countryModel;
    GeographyModel geographyModel;

    private Map<Integer, Geography> geographyMap = new HashMap<>();
    private List<Geography> geographies;



    @FXML
    private Label lblAnnualAVGTeamCostContainer,lblGeographyContainer, lblLocationContainer, lblNumberOfMembersContainer, lblTeamNameContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            projectTeamsModel = new ProjectTeamsModel();
            countryModel = new CountryModel();
            geographyModel = new GeographyModel();

            new Thread(() -> {
                try {
                    geographyMap = geographyModel.getGeographyMap();
                } catch (Exception e) {
                    handleException(e);//TODO: Handle this exception
                }
            }).start();

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

        if (geographyMap.isEmpty()) {
            loadGeographyMap();
        }

        Geography teamGeography = geographyMap.get(teamGeographyId);
        if (teamGeography != null) {
            lblGeographyContainer.setText(teamGeography.getGeographyName());
            try {
                updateGeographyInformation(teamGeographyId);
            } catch (Exception e) {
                handleException(e);
            }
        } else {
            lblLocationContainer.setText("No Country");
        }
    }



    private void loadGeographyMap() {
        try {
            geographyMap = geographyModel.getGeographyMap();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void updateGeographyInformation(int teamCountryId) {
        try {
           geographies = geographyModel.getGeographyMap().values().stream()
                    .filter(geography -> geography.getGeographyId() == teamCountryId)
                    .collect(Collectors.toList());
            lblGeographyContainer.setText(geographies.get(0).getGeographyName());
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