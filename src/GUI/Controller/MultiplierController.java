package GUI.Controller;

import CustomExceptions.ApplicationWideException;
import GUI.Model.MultiplierModel;
import GUI.Model.ProfileModel;
import GUI.Model.ProjectTeamsModel;
import GUI.Utility.AlertBox;
import GUI.Utility.ExceptionHandler;
import GUI.Utility.SliderDecimalFilter;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MultiplierController {

    @FXML
    private ListView lstVProfile, lstVTeams;
    @FXML
    private MFXSlider sliderGMP, sliderGMT, sliderMUT, sliderMUP;
    @FXML
    private TextField txtGMP, txtGMT, txtMUT, txtMUP;
    @FXML
    private Label lblGMHourlyResultP, lblMUHourlyResultP, lblGMDailyResultP, lblMUDailyResultP, lblGMHourlyResultT, lblMUHourlyResultT, lblGMDailyResultT, lblMUDailyResultT;

    private double grossMarginP, markUpP, grossMarginT, markUpT;

    private ProjectTeamsModel projectTeamsModel;
    private MultiplierModel multiplierModel;
    private ProfileModel profileModel;




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
            bindSliderAndTextFieldProfileForM();
            bindSliderAndTextFieldProfilesForGM();
            bindSliderAndTextFieldTeamsForGM();
            bindSliderAndTextFieldTeamsForM();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
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

    public void bindSliderAndTextFieldProfilesForGM() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtGMP.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtGMP.textProperty(),sliderGMP.valueProperty(),  converter);
    }

    public void bindSliderAndTextFieldProfileForM() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtMUP.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtMUP.textProperty(),sliderMUP.valueProperty(),  converter);
    }

    public void bindSliderAndTextFieldTeamsForGM() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtGMT.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtGMT.textProperty(),sliderGMT.valueProperty(),  converter);
    }

    public void bindSliderAndTextFieldTeamsForM() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtMUT.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtMUT.textProperty(),sliderMUT.valueProperty(),  converter);
    }

    private String formatResult(double result) {
        return String.format("%.2f", result);
    }

    private void setupListViewProfiles(){
        lstVProfile.setItems(profileModel.showAllProfilesNames());
    }

    private void setupListViewTeams() {
        lstVTeams.setItems(projectTeamsModel.getEveryProjectTeam());
    }

    private void calculateAndSetResultForProfile()  {
        if (checkSelection(lstVProfile, "No profile selected, please select one.")) {
            double percentageGMP = parseTextField(txtGMP);
            double percentageMUP = parseTextField(txtMUP);
            double hourlyRate = getHourlyRate();
            double dayRate = getDailyRate();

            calculateAndSetResultForRateForProfile(dayRate, hourlyRate, percentageGMP, percentageMUP);
        }
    }

    private void calculateAndSetResultForTeam() {
        if (checkSelection(lstVTeams, "No team selected, please select one.")) {
            double percentageGMT = parseTextField(txtGMT);
            double percentageMUT = parseTextField(txtMUT);
            double avgHourlyRate = getAvgHourlyRate();
            double avgDailyRate = getAvgDailyRate();

            calculateAndSetResultForRateForTeams(avgDailyRate, avgHourlyRate, percentageGMT, percentageMUT);
        }
    }

    private boolean checkSelection(ListView listView, String errorMessage) {
        if (listView.getSelectionModel().getSelectedItem() == null) {
            AlertBox.displayInfo("Selection Error", errorMessage);
            return false;
        }
        return true;
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

            return profileModel.getRateForProfile(selectedProfile, rateType);

    }

    private double getDailyRate() {
        return getRate(ProfileModel.RateType.DAILY);
    }

    private double getHourlyRate() {
        return getRate(ProfileModel.RateType.HOURLY);
    }

    public double getRatesForTeam(ProjectTeamsModel.ProjectTeamRateType rateType){
        String selectedTeam = lstVTeams.getSelectionModel().getSelectedItem().toString();

            return projectTeamsModel.getRateForProjectTeam(selectedTeam, rateType);

    }

    private double getAvgDailyRate() {
        return getRatesForTeam(ProjectTeamsModel.ProjectTeamRateType.AVGDAILY);
    }

    private double getAvgHourlyRate() {
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
        sliderGMP.setValue(0);
        sliderMUP.setValue(0);
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
        sliderGMT.setValue(0);
        sliderMUT.setValue(0);
    }
}
