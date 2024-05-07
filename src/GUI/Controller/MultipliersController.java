package GUI.Controller;
import GUI.Model.MultiplierModel;
import GUI.Model.ProfileModel;
import GUI.Model.ProjectTeamsModel;
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
    private double grossMargin, markUp;

    private ProjectTeamsModel projectTeamsModel;
    private MultiplierModel multiplierModel;
    private ProfileModel profileModel;


    @FXML
    private void initialize() {
        try {
            projectTeamsModel = new ProjectTeamsModel();
            profileModel = new ProfileModel();
            multiplierModel = new MultiplierModel(this);

            setUpSliders();
            setValueInField();
            setUpComboBoxes();
            setUpTextfields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpTextfields() {
        // Add a listener to the text property of the txtGM TextField
        txtGM.textProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                double value = Double.parseDouble(newValue);
                sliderGM.setValue(value);
            } catch (NumberFormatException e) {

            }
        }));
        // Add a listener to the text property of the txtMU TextField
        txtMU.textProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                double value = Double.parseDouble(newValue);
                sliderMU.setValue(value);
            } catch (NumberFormatException e) {

            }
        }));
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
        cBoxTeam_Multiplier.setItems(projectTeamsModel.getEveryProjectTeam());
        cBoxProfile_Multiplier.setItems(profileModel.showAllProfilesNames());
    }

    private void setValueInField() {
        txtGM.setText(String.valueOf(grossMargin));
        txtMU.setText(String.valueOf(markUp));
    }

    @FXML
    private void calculateResult(ActionEvent actionEvent) throws Exception {
        calculateAndSetResult();
    }

    private void calculateAndSetResult() throws Exception {
        double percentageGM = parseTextField(txtGM);
        double percentageMU = parseTextField(txtMU);

        if (cBoxProfile_Multiplier.getValue() != null) {
            double hourlyRate = getHourlyRate();
            double dayRate = getDailyRate();

            calculateAndSetResultForRate(dayRate, hourlyRate, percentageGM, percentageMU);
        } else if (cBoxTeam_Multiplier.getValue() != null) {
            double avgHourlyRate = getAvgHourlyRate();
            double avgDailyRate = getAvgDailyRate();

            calculateAndSetResultForRate(avgDailyRate, avgHourlyRate, percentageGM, percentageMU);
        } else {
            throw new Exception("No profile or team selected");//TODO: Handle this exception
        }
    }

    private void calculateAndSetResultForRate(double dayRate, double hourlyRate, double percentageGM, double percentageMU) {
        // Here we get the result of the daily rate with the multiplier from the slider
        double resultDailyGM = multiplierModel.getResultOfHourlyRateWithMultiplier(dayRate, percentageGM);
        lblGMDailyResult.setText(formatResult(resultDailyGM));

        double resultDailyMU = multiplierModel.getResultOfDayRWithMultiplier(dayRate, percentageMU);
        lblMUDailyResult.setText(formatResult(resultDailyMU));

        // Here we get the result of the hourly rate with the multiplier from the slider
        double resultHourlyGM = multiplierModel.getResultOfHourlyRateWithMultiplier(hourlyRate, percentageGM);
        lblGMHourlyResult.setText(formatResult(resultHourlyGM));

        double resultHourlyMU = multiplierModel.getResultOfDayRWithMultiplier(hourlyRate, percentageMU);
        lblMUHourlyResult.setText(formatResult(resultHourlyMU));
    }

    private double parseTextField(TextField textField) {
        return Double.parseDouble(textField.getText());
    }

    private String formatResult(double result) {
        return String.format("%.2f", result);
    }

    private double getRate(ProfileModel.RateType rateType) {
        String selectedProfile = cBoxProfile_Multiplier.getValue().toString();
        return profileModel.getRateForProfile(selectedProfile, rateType);
    }

    private double getDailyRate() {
        return getRate(ProfileModel.RateType.DAILY);
    }

    private double getHourlyRate() {
        return getRate(ProfileModel.RateType.HOURLY);
    }

    public double getRatesForTeam(ProjectTeamsModel.ProjectTeamRateType rateType) throws Exception {
        String selectedTeam = cBoxTeam_Multiplier.getValue().toString();
        return projectTeamsModel.getRateForProjectTeam(selectedTeam, rateType);
    }

    private double getAvgDailyRate() throws Exception{
        return getRatesForTeam(ProjectTeamsModel.ProjectTeamRateType.AVGDAILY);
    }

    private double getAvgHourlyRate() throws Exception{
        return getRatesForTeam(ProjectTeamsModel.ProjectTeamRateType.AVGHOURLY);
    }
}
