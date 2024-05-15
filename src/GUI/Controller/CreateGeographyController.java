package GUI.Controller;

import GUI.Model.CountryModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateGeographyController implements Initializable {
    @FXML
    private TextField txtGeography;
    @FXML
    private CheckComboBox cBoxCountries;
    @FXML
    private ListView lstViewCreateGeography;
    private CountryModel countryModel;

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

    }


}
