package GUI.Controller;

import BE.Country;
import BE.Geography;
import GUI.Model.CountryModel;

import GUI.Model.GeographyModel;
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
    private CheckComboBox cBoxCountries;
    private CountryModel countryModel;
    private GeographyModel geographyModel;

    public CreateGeographyController(GeographyModel geographyModel) {
        this.geographyModel = geographyModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            countryModel = new CountryModel();

            setupComboBox();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupComboBox() throws Exception {
        cBoxCountries.getItems().addAll(countryModel.getAllCountries());
    }

    @FXML
    private void saveGeography(ActionEvent actionEvent) {
        String geographyName = txtGeography.getText();
        List<Country> selectedCountries = getSelectedCountries();

        Geography geography = new Geography(geographyName, selectedCountries);

        try {
            geographyModel.saveGeography(geography);

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private List<Country> getSelectedCountries() {
        return cBoxCountries.getCheckModel().getCheckedItems();
    }

}
