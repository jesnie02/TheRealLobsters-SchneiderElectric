package GUI.Controller;

import BE.Country;
import BE.Geography;
import CustomExceptions.ApplicationWideException;
import GUI.Model.CountryModel;

import GUI.Model.GeographyModel;
import GUI.Utility.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreateGeographyController implements Initializable {
    @FXML
    private TextField txtGeography;
    @FXML
    private CheckComboBox<Country> cBoxCountries;
    private CountryModel countryModel;
    private final GeographyModel geographyModel;

    public CreateGeographyController(GeographyModel geographyModel) {
        this.geographyModel = geographyModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            countryModel = new CountryModel();

            setupComboBox();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void setupComboBox() {
        cBoxCountries.getItems().addAll(countryModel.getAllCountries());
    }

    @FXML
    private void saveGeography(ActionEvent actionEvent) {
        if (!validateInput()) {
            return;
        }
        String geographyName = txtGeography.getText();
        List<Country> selectedCountries = getSelectedCountries();
        Geography geography = new Geography(geographyName, selectedCountries);


        geographyModel.saveGeography(geography);
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }



    private List<Country> getSelectedCountries() {
        return cBoxCountries.getCheckModel().getCheckedItems();
    }

    private boolean validateInput() {
        boolean isValid = true;

        // Validate the TextField for geography name
        if (txtGeography.getText().isEmpty()) {
            txtGeography.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            txtGeography.setStyle("");
        }

        // Validate the CheckComboBox for selected countries
        if (cBoxCountries.getCheckModel().getCheckedItems().isEmpty()) {
            cBoxCountries.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            cBoxCountries.setStyle("");
        }

        return isValid;
    }

}
