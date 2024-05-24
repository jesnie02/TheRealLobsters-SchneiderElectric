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
            setupComboBox();
        } catch (ApplicationWideException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateUI(Geography geography) {
        txtGeography.setText(geography.getGeographyName());
        cBoxCountries.getCheckModel().clearChecks();
        List<Country> selectedCountries = geography.getCountries();
        List<Country> allCountries = countryModel.getAllCountries();
        cBoxCountries.getItems().setAll(allCountries);
        for (Country country : selectedCountries) {
            for (Country comboBoxCountry : cBoxCountries.getItems()) {
                if (country.equals(comboBoxCountry)) {
                    cBoxCountries.getCheckModel().check(comboBoxCountry);
                }
            }
        }
    }

    private void setupComboBox(){
        cBoxCountries.getItems().addAll(countryModel.getAllCountries());
    }

    @FXML
    private void updateGeography(ActionEvent event) {
    }

    public void setGeography(Geography geography) {
        DataModelSingleton.getInstance().setCurrentGeography(geography);
        updateUI(geography);
    }

    public void setOnGeographyUpdated(Runnable onProfileUpdated) {
        this.onGeographyUpdated = onProfileUpdated;
    }


}
