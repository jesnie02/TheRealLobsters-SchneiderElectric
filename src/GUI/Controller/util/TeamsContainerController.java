package GUI.Controller.util;

import BE.Country;
import BE.Geography;
import BE.ProjectTeam;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Model.ProjectTeamsModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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

    ProjectTeamsModel projectTeamsModel;
    CountryModel countryModel;
    GeographyModel geographyModel;

    private Map<Integer, Country> countriesMap = new HashMap<>();
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
                    countriesMap = countryModel.getCountriesMap();
                } catch (Exception e) {
                    handleException(e);//TODO: Handle this exception
                }
            }).start();

        } catch (Exception e) {
            handleException(e);//TODO: Handle this exception
        }

    }



    public void updateUI(ProjectTeam team) throws Exception {
        Platform.runLater(() -> {
            updateTeamInformation(team);
            //updateCountryInformation(team);
        });

    }

    private void updateTeamInformation(ProjectTeam team) {
        lblTeamNameContainer.setText(team.getTeamName());
        lblNumberOfMembersContainer.setText(String.valueOf(team.getNumberOfProfiles()));
        lblAnnualAVGTeamCostContainer.setText(String.format("%.2f", team.getAvgAnnualSalary()));
    }
/*
    private void updateCountryInformation(ProjectTeam team) {
       // int teamCountryId = team.getCountryId();

        if (countriesMap.isEmpty()) {
            loadCountriesMap();
        }

        Country teamCountry = countriesMap.get(teamCountryId);
        if (teamCountry != null) {
            lblLocationContainer.setText(teamCountry.getCountryName());
            try {
                updateGeographyInformation(teamCountryId);
            } catch (Exception e) {
                handleException(e);
            }
        } else {
            lblLocationContainer.setText("No Country");
        }
    }

 */

    private void loadCountriesMap() {
        try {
            countriesMap = countryModel.getCountriesMap();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void updateGeographyInformation(int teamCountryId) {
        try {
           // geographies = geographyModel.getRegionsByCountryId(teamCountryId);
            String regionsString = geographies.stream()
                    .map(Geography::getGeographyName)
                    .collect(Collectors.joining(", "));
            lblGeographyContainer.setText(regionsString);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        e.printStackTrace(); // TODO: Handle this exception appropriately
    }

}