package GUI.Controller;

import BE.Country;
import BE.Geography;
import CustomExceptions.ApplicationWideException;
import GUI.Model.CountryModel;
import GUI.Utility.DataModelSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateGeographyController implements Initializable {

    private Runnable onGeographyUpdated;
    private CountryModel countryModel;
    private Geography geography;
    @FXML
    private CheckComboBox<Country> cBoxCountries;
    @FXML
    private TextField txtGeography;
    @FXML
    private Button btnSaveGeography;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            countryModel = new CountryModel();
            loadAllCountries();
        } catch (ApplicationWideException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateUI(Geography geography) {
        txtGeography.setText(geography.getGeographyName());
    }

    public void setGeography(Geography geography) {
        this.geography = geography;
        //DataModelSingleton.getInstance().setCurrentGeography(geography);
        updateUI(geography);
        loadGeographyDetails();
    }

    public void setOnGeographyUpdated(Runnable onProfileUpdated) {
        this.onGeographyUpdated = onProfileUpdated;
    }

    private void loadAllCountries() throws ApplicationWideException {
        cBoxCountries.getItems().setAll(countryModel.getAllCountries());
    }

    private void loadGeographyDetails(){
        List<Country> selectedCountries = geography.getCountries();
        cBoxCountries.getCheckModel().clearChecks();
        selectedCountries.forEach(country -> cBoxCountries.getCheckModel().check(country));
        System.out.println("Selected countries: " + selectedCountries);
    }

    public void setGeographyAndCountries(List<Country> countries) {
        cBoxCountries.getCheckModel().clearChecks();
        countries.forEach(country -> cBoxCountries.getCheckModel().check(country));
    }

    @FXML
    private void updateGeography(ActionEvent event) {
    }
}
