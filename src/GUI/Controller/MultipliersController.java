package GUI.Controller;
import GUI.Model.CountryModel;
import GUI.Model.MultiplierModel;
import GUI.Model.ProfileModel;
import GUI.Model.TeamsModel;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MultipliersController {

    @FXML
    private MFXSlider sliderGM, sliderMU;
    @FXML
    private TextField txtGM, txtMU;
    @FXML
    private ComboBox cBoxProfile_Multiplier, cBoxTeam_Multiplier;
    @FXML
    private Label lblMUDailyResult, lblGMDailyResult, lblGMHourlyResult, lblMUHourlyResult;
    private CountryModel countryModel;
    private TeamsModel teamsModel;
    private MultiplierModel multiplierModel;
    private ProfileModel profileModel;
    private double grossMargin, markUp;
    @FXML
    private Button btnShowCalculation;


    @FXML
    private void initialize() {
        try {
            countryModel = new CountryModel();
            teamsModel = new TeamsModel();
            profileModel = new ProfileModel();
            multiplierModel = new MultiplierModel(this);

            setUpSliders();
            setValueInField();
            setUpComboBoxes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void setUpSliders() {
        sliderGM.valueProperty().addListener(((observable, oldValue, newValue) -> {
            grossMargin = newValue.doubleValue();
            setValueInField();
        }));
        sliderMU.valueProperty().addListener(((observable, oldValue, newValue) -> {
            markUp = newValue.doubleValue();
            setValueInField();
        }));
    }

    private void setUpComboBoxes() throws Exception {
        cBoxTeam_Multiplier.setItems(teamsModel.getAllProjectTeams());
        cBoxProfile_Multiplier.setItems(profileModel.showAllProfilesNames());
    }

    private void setValueInField() {
        txtGM.setText(String.valueOf(grossMargin));
        txtMU.setText(String.valueOf(markUp));
    }

    public double getGMValue() {
        return grossMargin;
    }

    public double getMUValue() {
        return markUp;
    }


    @FXML
    private void calculateResult(ActionEvent actionEvent) throws NumberFormatException {
        double dayRate = getDailyRate(); //TODO: Get the day rate from the database
        double hourlyRate = getHourlyRate(); //TODO: Get the hourly rate from the database

        calculateAndSetResult(dayRate, hourlyRate);
    }

    private void calculateAndSetResult(double dayRate, double hourlyRate) {
        double percentageDGM = parseTextField(txtGM);
        double percentageDMU = parseTextField(txtMU);

        // Here we get the result of the daily rate with the multiplier from the slider
        double resultDailyGM = multiplierModel.getResultOfHourlyRateWithMultiplier(dayRate, percentageDGM);
        lblGMDailyResult.setText(formatResult(resultDailyGM));

        double resultDailyMU = multiplierModel.getResultOfDayRWithMultiplier(dayRate, percentageDMU);
        lblMUDailyResult.setText(formatResult(resultDailyMU));

        // Here we get the result of the hourly rate with the multiplier from the slider
        double resultHourlyGM = multiplierModel.getResultOfHourlyRateWithMultiplier(hourlyRate, percentageDGM);
        lblGMHourlyResult.setText(formatResult(resultHourlyGM));

        double resultHourlyMU = multiplierModel.getResultOfDayRWithMultiplier(hourlyRate, percentageDMU);
        lblMUHourlyResult.setText(formatResult(resultHourlyMU));
    }

    private double parseTextField(TextField textField) {
        return Double.parseDouble(textField.getText());
    }

    private String formatResult(double result) {
        return String.format("%.2f", result);
    }

    private double getDailyRate() {
        String selectedProfile = cBoxProfile_Multiplier.getValue().toString();
        double dailyRate = profileModel.getDailyRateForProfile(selectedProfile);

        return dailyRate; //TODO: Get the daily rate from the database
    }

    private double getHourlyRate() {
        String selectedProfile = cBoxProfile_Multiplier.getValue().toString();
        double hourlyRate = profileModel.getHourlyRateForProfile(selectedProfile);
        return hourlyRate; //TODO: Get the hourly rate from the database
    }

}
