package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.ProjectTeam;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Model.ProjectTeamsModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GeographyController implements Initializable {
    @FXML
    private ComboBox<Country> cBoxCountryGeo;
    @FXML
    private ComboBox<Geography> cBoxRegionGeo;
    @FXML
    private ComboBox<ProjectTeam> cBoxTeamGeo;

    private CountryModel countryModel;
    private ProjectTeamsModel projectTeamsModel;
    private GeographyModel geographyModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            countryModel = new CountryModel();
            projectTeamsModel = new ProjectTeamsModel();
            geographyModel = new GeographyModel();

            cBoxCountryGeo.setItems(countryModel.getAllFromCountries());

            //cBoxRegionGeo.setItems(geographyModel.getAllFromGeographies());
        } catch (IOException e) {
            e.printStackTrace(); //TODO: Handle this exception
        } catch (Exception e) {
            throw new RuntimeException(e); //TODO: Handle this exception
        }
    }
}
