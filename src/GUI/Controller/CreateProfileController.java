package GUI.Controller;

import GUI.Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateProfileController {
    @FXML
    private ComboBox<String> cBoxCountry_CreateProfile, cBoxTeam_CreateProfile;
    @FXML
    private Label lblHourlyResult, lblFixedAmountResult;
    @FXML
    private TextField txtFixedAmount;

    private CountryModel countryModel;
    private TeamsModel teamsModel;
    private ProfileModel profileModel;

    public CreateProfileController() {
        try {
            countryModel = new CountryModel();
            teamsModel = new TeamsModel();
            profileModel = new ProfileModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        try {
            cBoxCountry_CreateProfile.setItems(countryModel.getAllCountries());
            cBoxTeam_CreateProfile.setItems(teamsModel.getAllProjectTeams());

            ChangeListener<String> textFieldListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                calculateHourlyRateWithFixedAmount();
            };

            txtFixedAmount.textProperty().addListener(textFieldListener);
            lblHourlyResult.textProperty().addListener(textFieldListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateHourlyRateWithFixedAmount() {
        double hourlyRate = Double.parseDouble(lblHourlyResult.getText());
        double fixedAmount = Double.parseDouble(txtFixedAmount.getText());
        double result = profileModel.calculateHourlyRateWithFixedAmount(hourlyRate, fixedAmount);
        lblFixedAmountResult.setText(String.valueOf(result));
    }
}