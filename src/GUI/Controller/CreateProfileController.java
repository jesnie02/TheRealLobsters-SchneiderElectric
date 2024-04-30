package GUI.Controller;

import GUI.Model.*;
import javafx.scene.control.*;
import java.io.IOException;

public class CreateProfileController {
    public ComboBox cBoxCountry_CreateProfile, cBoxTeam_CreateProfile;
    public TextField txtAnnualSalary, txtFixedAmount;
    public Label lblHourlyResult, lblFixedAmountResult;

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

    public void initialize() {
        try {
            cBoxCountry_CreateProfile.setItems(countryModel.getAllCountries());
            cBoxTeam_CreateProfile.setItems(teamsModel.getAllProjectTeams());
            txtAnnualSalary.textProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("txtAnnualSalary changed from " + oldValue + " to " + newValue);
                sendValuesToProfileModel();
                displayCalculatedHourlyRate();
            });

            txtFixedAmount.textProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("txtFixedAmount changed from " + oldValue + " to " + newValue);
                sendValuesToProfileModel();
                displayCalculatedHourlyRate();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendValuesToProfileModel() {
        profileModel.setHourlyResultAndFixedAmount(lblHourlyResult.getText(), txtFixedAmount.getText());
        System.out.println("Values have been sent to the model: " + lblHourlyResult.getText() + ", " + txtFixedAmount.getText());
    }

    public void displayCalculatedHourlyRate() {
        lblFixedAmountResult.setText(String.valueOf(profileModel.fetchAndReturnCalculatedHourlyRateWithFixedAmount()));
    }
}