package GUI.Controller;

import BE.Country;
import GUI.Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import BE.Profile;
import GUI.Model.ProfileModel;
import GUI.Model.TeamsModel;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import GUI.Model.CountryModel;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CreateProfileController implements Initializable {

    // slider for overhead and utilization and textField to show the value of the slider
    @FXML
    private MFXSlider sliderOverhead, sliderUtilization;
    @FXML
    private TextField txtOverheadView,txtUtilizationView;

    // textFields for the profile name, annual salary, fixed amount and effective hours
    @FXML
    private TextField txtFirstnameProfile, txtLastnameProfile, txtAnnualSalary, txtFixedAmount, txtEffectiveHours;

    @FXML
    private CheckBox checkOverhead, checkProduction;

    @FXML
    private Label lblHourlyResult, lblDailyResult, lblShowMassage, lblFixedAmountResult;

    // comboBox for country and team
    public ComboBox<Country> cBoxCountry_CreateProfile;
    public ComboBox<String> cBoxTeam_CreateProfile;


    private CountryModel countryModel;
    private TeamsModel teamsModel;
    private ProfileModel profileModel;


    /**
     * This method is called after all @FXML annotated members have been injected.
     * It sets up listeners on sliders and checkboxes, and populates the country and team ComboBoxes.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listenersOnSliders();
        setupCheckboxListeners();
        txtFixedAmount.textProperty().addListener((observable, oldValue, newValue) -> calculateHourlyRateWithFixedAmount());
        lblHourlyResult.textProperty().addListener((observable, oldValue, newValue) -> calculateHourlyRateWithFixedAmount());
        try {
            cBoxCountry_CreateProfile.setItems(countryModel.getAllCountries());
            cBoxTeam_CreateProfile.setItems(profileModel.getRoleList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets up listeners on the overhead and utilization sliders.
     * When the value of a slider changes, the corresponding TextField is updated with the new value.
     */
    public void listenersOnSliders() {
        sliderOverhead.valueProperty().addListener((observable, oldValue, newValue) -> {

            txtOverheadView.setText(String.format("%.1f", newValue.doubleValue()));
        });


        sliderUtilization.valueProperty().addListener((observable, oldValue, newValue) -> {

            txtUtilizationView.setText(String.format("%.1f", newValue.doubleValue()));
        });
    }

    /**
     * This method sets up listeners on the overhead and production checkboxes.
     * When one checkbox is selected, the other one is deselected.
     */
    private void setupCheckboxListeners() {
        checkOverhead.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkProduction.setSelected(false);
            }
        });

        checkProduction.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkOverhead.setSelected(false);
            }
        });
    }

    public CreateProfileController(){
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
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when the save button is clicked.
     * It validates the input, creates a new Profile object, and saves it to the database.
     */
    @FXML
    private void saveProfileToDatabase(ActionEvent actionEvent) {
        if (!validateInput()) {
            return;
        }

        String firstName = txtFirstnameProfile.getText();
        String lastName = txtLastnameProfile.getText();
        float annualSalary = (float) Double.parseDouble(txtAnnualSalary.getText());
        int countryId = ( cBoxCountry_CreateProfile.getValue()).getCountryId();
        String projectRole = cBoxTeam_CreateProfile.getValue();
        int hourlyResult = Integer.parseInt(lblHourlyResult.getText());
        int dailyResult = Integer.parseInt(lblDailyResult.getText());
        boolean overheadCost = checkOverhead.isSelected();

        Profile newProfile = new Profile(firstName, lastName, annualSalary, countryId, projectRole, hourlyResult, dailyResult, overheadCost);

        profileModel.saveProfile(newProfile);

        lblShowMassage.setText("Profile has been saved");
    }

    public void calculateHourlyRateWithFixedAmount() {
        double hourlyRate = Double.parseDouble(lblHourlyResult.getText());
        double fixedAmount = Double.parseDouble(txtFixedAmount.getText());
        double result = profileModel.calculateHourlyRateWithFixedAmount(hourlyRate, fixedAmount);
        lblFixedAmountResult.setText(String.valueOf(result));
    }


    /**
     * This method validates the input in the TextFields and ComboBoxes.
     * If a field is empty, it is highlighted in red and the method returns false.
     * If all fields are filled, the method returns true.
     */
    private boolean validateInput() {
        boolean isValid = true;

        // List of all TextFields and ComboBoxes
        List<Control> fields = Arrays.asList(txtFirstnameProfile, txtLastnameProfile, txtAnnualSalary, cBoxCountry_CreateProfile, cBoxTeam_CreateProfile);

        for (Control field : fields) {
            if (field instanceof TextField) {
                TextField textField = (TextField) field;
                if (textField.getText().isEmpty()) {
                    textField.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    textField.setStyle("");
                }
            } else if (field instanceof ComboBox) {
                ComboBox<?> comboBox = (ComboBox<?>) field;
                if (comboBox.getValue() == null) {
                    comboBox.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    comboBox.setStyle("");
                }
            }
        }

        return isValid;
    }
}

