package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.ProjectTeam;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Model.ProjectTeamsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    public Label lblDailyRateSumCountry,lblHourlyRateSumCountry,lblAvgDailyRateCountry,lblAvgHourlyRateCountry;
    @FXML
    private ComboBox<Country> cBoxCountryGeo;
    @FXML
    private ComboBox<Geography> cBoxGeographyDash;
    @FXML
    private ComboBox<ProjectTeam> cBoxTeamDash;

    private CountryModel countryModel;
    private ProjectTeamsModel projectTeamsModel;
    private GeographyModel geographyModel;
    @FXML
    private Label lblAvgHourlyRateTeam;
    @FXML
    private Label lblAvgDailyRateTeam;
    @FXML
    private Label lblHourlyRateSumTeam;
    @FXML
    private Label lblDailyRateSumTeam;

    @FXML
    private Label lblSumDailyRateGeo, lblSumHourlyRateGeo, lblAvgDailyRateGeo, lblAvgHourlyRateGeo;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            countryModel = new CountryModel();
            projectTeamsModel = new ProjectTeamsModel();
            geographyModel = new GeographyModel();

            //Geography
            cBoxGeographyDash.setItems(geographyModel.getSumsAndAveragesForGeographies());
            cBoxGeographyDash.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateLabelsGeographyTab(newValue);
            });

            //Country
            cBoxCountryGeo.setItems(countryModel.getSumsAndAveragesForCountries());
            cBoxCountryGeo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateLabelsCountryTab(newValue);
            });

            //Team
            cBoxTeamDash.setItems(projectTeamsModel.getAllProjectTeamsData());
            //cBoxRegionGeo.setItems(geographyModel.getAllFromGeographies());
            cBoxTeamDash.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateLabelsTeamTab(newValue);
            });


        } catch (IOException e) {
            e.printStackTrace(); //TODO: Handle this exception
        } catch (Exception e) {
            throw new RuntimeException(e); //TODO: Handle this exception
        }
    }

    private void updateLabelsGeographyTab(Geography selectedGeography) {
        if (selectedGeography != null) {
            lblSumDailyRateGeo.setText(String.format("%.2f", selectedGeography.getSumOfDailyRate()));
            lblSumHourlyRateGeo.setText(String.format("%.2f", selectedGeography.getSumOfHourlyRate()));
            lblAvgDailyRateGeo.setText(String.format("%.2f", selectedGeography.getAvgDailyRate()));
            lblAvgHourlyRateGeo.setText(String.format("%.2f", selectedGeography.getAvgHourlyRate()));
        } else {
            // Clear labels if no geography is selected
            lblSumDailyRateGeo.setText("");
            lblSumHourlyRateGeo.setText("");
            lblAvgDailyRateGeo.setText("");
            lblAvgHourlyRateGeo.setText("");
        }
    }

    private void updateLabelsTeamTab(ProjectTeam selectedTeam) {
        if (selectedTeam != null) {
            lblHourlyRateSumTeam.setText(String.format("%.2f", selectedTeam.getSumOfHourlyRate()));
            lblDailyRateSumTeam.setText(String.format("%.2f", selectedTeam.getSumOfDailyRate()));
            lblAvgDailyRateTeam.setText(String.format("%.2f", selectedTeam.getAvgDailyRate()));
            lblAvgHourlyRateTeam.setText(String.format("%.2f", selectedTeam.getAvgHourlyRate()));
        } else {
            // Clear labels if no team is selected
            lblHourlyRateSumTeam.setText("");
            lblDailyRateSumTeam.setText("");
            lblAvgDailyRateTeam.setText("");
            lblAvgHourlyRateTeam.setText("");
        }
    }

    private void updateLabelsCountryTab(Country selectedCountry) {
        if (selectedCountry != null) {
            lblHourlyRateSumCountry.setText(String.format("%.2f", selectedCountry.getSumOfHourlyRate()));
            lblDailyRateSumCountry.setText(String.format("%.2f", selectedCountry.getSumOfDailyRate()));
            lblAvgDailyRateCountry.setText(String.format("%.2f", selectedCountry.getAvgDailyRate()));
            lblAvgHourlyRateCountry.setText(String.format("%.2f", selectedCountry.getAvgHourlyRate()));
        } else {
            // Clear labels if no country is selected
            lblHourlyRateSumCountry.setText("");
            lblDailyRateSumCountry.setText("");
            lblAvgDailyRateCountry.setText("");
            lblAvgHourlyRateCountry.setText("");
        }
    }

    public void calculateAndSetLabelsCountry(ActionEvent actionEvent) {
        Country selectedCountry = cBoxCountryGeo.getSelectionModel().getSelectedItem();
        updateLabelsCountryTab(selectedCountry);
    }

}
