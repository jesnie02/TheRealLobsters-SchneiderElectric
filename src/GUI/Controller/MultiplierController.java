package GUI.Controller;

import CustomExceptions.ApplicationWideException;
import GUI.Model.MultiplierModel;
import GUI.Model.ProfileModel;
import GUI.Model.ProjectTeamsModel;
import GUI.Utility.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MultiplierController {
    @FXML
    private ListView lstVProfile;
    @FXML
    private MFXSlider sliderGMP;
    @FXML
    private TextField txtGMP;
    @FXML
    private MFXSlider sliderMUP;
    @FXML
    private TextField txtMUP;
    @FXML
    private Label lblGMHourlyResultP;
    @FXML
    private Label lblMUHourlyResultP;
    @FXML
    private Label lblGMDailyResultP;
    @FXML
    private Label lblMUDailyResultP;
    @FXML
    private ListView lstVTeams;
    @FXML
    private MFXSlider sliderGMT;
    @FXML
    private TextField txtGMT;
    @FXML
    private MFXSlider sliderMUT;
    @FXML
    private TextField txtMUT;
    @FXML
    private Label lblGMHourlyResultT;
    @FXML
    private Label lblMUHourlyResultT;
    @FXML
    private Label lblGMDailyResultT;
    @FXML
    private Label lblMUDailyResultT;
    private ProjectTeamsModel projectTeamsModel;
    private MultiplierModel multiplierModel;
    private ProfileModel profileModel;

    private double grossMarginP, markUpP, grossMarginT, markUpT;



    @FXML
    private void initialize() {
        try {
            projectTeamsModel = new ProjectTeamsModel();
            profileModel = new ProfileModel();
            multiplierModel = new MultiplierModel(this);

            setupSlidersForProfile();
            setupSlidersForTeams();
            setValueInFieldProfile();
            setupListViewProfiles();
            setValueInFieldTeams();
            setupListViewTeams();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSlidersForProfile(){
        sliderGMP.valueProperty().addListener((observable, oldValue, newValue) -> {
            grossMarginP = newValue.doubleValue();
            setValueInFieldProfile();
        });
        sliderMUP.valueProperty().addListener((observable, oldValue, newValue) -> {
            markUpP = newValue.doubleValue();
            setValueInFieldProfile();
        });
    }

    private void setupSlidersForTeams(){
        sliderGMT.valueProperty().addListener((observable, oldValue, newValue) -> {
            grossMarginT = newValue.doubleValue();
            setValueInFieldTeams();
        });
        sliderMUT.valueProperty().addListener((observable, oldValue, newValue) -> {
            markUpT = newValue.doubleValue();
            setValueInFieldTeams();
        });
    }

    private void setValueInFieldProfile(){
        txtGMP.setText(String.valueOf(grossMarginP));
        txtMUP.setText(String.valueOf(markUpP));
    }

    private void setValueInFieldTeams(){
        txtGMT.setText(String.valueOf(grossMarginT));
        txtMUT.setText(String.valueOf(markUpT));
    }

    private String formatResult(double result) {
        return String.format("%.2f", result);
    }

    private void setupListViewProfiles(){
        try {
            lstVProfile.setItems(profileModel.showAllProfilesNames());
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void setupListViewTeams() throws Exception {
        lstVTeams.setItems(projectTeamsModel.getEveryProjectTeam());
    }

    private void calculateAndSetResultForProfile() throws Exception {
        double percentageGMP = parseTextField(txtGMP);
        double percentageMUP = parseTextField(txtMUP);

        if (lstVProfile.getSelectionModel().getSelectedItem() != null) {
            double hourlyRate = getHourlyRate();
            double dayRate = getDailyRate();

            calculateAndSetResultForRateForProfile(dayRate, hourlyRate, percentageGMP, percentageMUP);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection error");
            alert.setHeaderText(null);
            alert.setContentText("No profile selected, please select one");
            alert.showAndWait();
        }
    }

    private void calculateAndSetResultForTeam() throws Exception {
        double percentageGMT = parseTextField(txtGMT);
        double percentageMUT = parseTextField(txtMUT);
        if (lstVTeams.getSelectionModel().getSelectedItem() != null) {

            double avgHourlyRate = getAvgHourlyRate();
            double avgDailyRate = getAvgDailyRate();

            calculateAndSetResultForRateForTeams(avgDailyRate, avgHourlyRate, percentageGMT, percentageMUT);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection error");
            alert.setHeaderText(null);
            alert.setContentText("No team selected, please select one");
            alert.showAndWait();
        }
    }

    private void calculateAndSetResultForRateForProfile(double dayRate, double hourlyRate, double percentageGM, double percentageMU) {
        // Here we get the result of the daily rate with the multiplier from the slider
        double resultDailyGM = multiplierModel.getResultOfHourlyRateWithMultiplier(dayRate, percentageGM);
        lblGMDailyResultP.setText(formatResult(resultDailyGM));

        double resultDailyMU = multiplierModel.getResultOfDayRWithMultiplier(dayRate, percentageMU);
        lblMUDailyResultP.setText(formatResult(resultDailyMU));

        // Here we get the result of the hourly rate with the multiplier from the slider
        double resultHourlyGM = multiplierModel.getResultOfHourlyRateWithMultiplier(hourlyRate, percentageGM);
        lblGMHourlyResultP.setText(formatResult(resultHourlyGM));

        double resultHourlyMU = multiplierModel.getResultOfDayRWithMultiplier(hourlyRate, percentageMU);
        lblMUHourlyResultP.setText(formatResult(resultHourlyMU));
    }

    private void calculateAndSetResultForRateForTeams(double dayRate, double hourlyRate, double percentageGM, double percentageMU) {
        // Here we get the result of the daily rate with the multiplier from the slider
        double resultDailyGM = multiplierModel.getResultOfHourlyRateWithMultiplier(dayRate, percentageGM);
        lblGMDailyResultT.setText(formatResult(resultDailyGM));

        double resultDailyMU = multiplierModel.getResultOfDayRWithMultiplier(dayRate, percentageMU);
        lblMUDailyResultT.setText(formatResult(resultDailyMU));

        // Here we get the result of the hourly rate with the multiplier from the slider
        double resultHourlyGM = multiplierModel.getResultOfHourlyRateWithMultiplier(hourlyRate, percentageGM);
        lblGMHourlyResultT.setText(formatResult(resultHourlyGM));

        double resultHourlyMU = multiplierModel.getResultOfDayRWithMultiplier(hourlyRate, percentageMU);
        lblMUHourlyResultT.setText(formatResult(resultHourlyMU));
    }


    private double parseTextField(TextField textField) {
        return Double.parseDouble(textField.getText());
    }

    private double getRate(ProfileModel.RateType rateType) {
        String selectedProfile = lstVProfile.getSelectionModel().getSelectedItem().toString();
        try {
            return profileModel.getRateForProfile(selectedProfile, rateType);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
            return 0.0; // return a default value when an exception is caught
        }
    }

    private double getDailyRate() {
        return getRate(ProfileModel.RateType.DAILY);
    }

    private double getHourlyRate() {
        return getRate(ProfileModel.RateType.HOURLY);
    }

    public double getRatesForTeam(ProjectTeamsModel.ProjectTeamRateType rateType) throws Exception {
        String selectedTeam = lstVTeams.getSelectionModel().getSelectedItem().toString();
        return projectTeamsModel.getRateForProjectTeam(selectedTeam, rateType);
    }

    private double getAvgDailyRate() throws Exception{
        return getRatesForTeam(ProjectTeamsModel.ProjectTeamRateType.AVGDAILY);
    }

    private double getAvgHourlyRate() throws Exception{
        return getRatesForTeam(ProjectTeamsModel.ProjectTeamRateType.AVGHOURLY);
    }

    @FXML
    private void calculateForProfile(ActionEvent actionEvent) throws Exception {
        calculateAndSetResultForProfile();
    }

    @FXML
    private void resetProfile(ActionEvent actionEvent) {
        lstVProfile.getSelectionModel().clearSelection();

        lblMUHourlyResultP.setText("0");
        lblMUDailyResultP.setText("0");
        lblGMHourlyResultP.setText("0");
        lblGMDailyResultP.setText("0");
    }

    @FXML
    private void calculateForTeams(ActionEvent actionEvent) throws Exception {
        calculateAndSetResultForTeam();
    }

    @FXML
    private void resetTeams(ActionEvent actionEvent) {
        lstVTeams.getSelectionModel().clearSelection();

        lblMUHourlyResultT.setText("0");
        lblMUDailyResultT.setText("0");
        lblGMHourlyResultT.setText("0");
        lblGMDailyResultT.setText("0");
    }


}
