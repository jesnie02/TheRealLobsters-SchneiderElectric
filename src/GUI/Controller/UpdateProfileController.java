package GUI.Controller;

import BE.Profile;
import CustomExceptions.ApplicationWideException;
import GUI.Model.ProfileModel;
import GUI.Utility.AlertBox;
import GUI.Utility.DataModelSingleton;
import GUI.Utility.ExceptionHandler;
import GUI.Utility.SliderDecimalFilter;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateProfileController implements Initializable {


    @FXML
    private TextField txtFName, txtLName, txtEffectiveHours,txtOverhead , txtAnnualSalary, txtFixedAmount, txtDailyWorkingHours;
    @FXML
    private Label lblProfileName;
    @FXML
    private MFXSlider sliderOverheadMulti;

    private FrameController frameController;
    private ProfileModel profileModel;
    private Runnable onProfileUpdated;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            profileModel = new ProfileModel();
            frameController = FrameController.getInstance();
            bindSliderAndTextField();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }



    public void setProfile(Profile profile) {
        DataModelSingleton.getInstance().setCurrentProfile(profile);
        updateUI(profile);
    }

    public void setOnProfileUpdated(Runnable onProfileUpdated) {
        this.onProfileUpdated = onProfileUpdated;
    }

    public void updateUI(Profile profile) {
        lblProfileName.setText(profile.getFName() + " " + profile.getLName());
        txtFName.setText(profile.getFName());
        txtLName.setText(profile.getLName());
        txtAnnualSalary.setText(String.valueOf(profile.getAnnualSalary()));
        txtOverhead.setText(String.valueOf(profile.getOverheadMultiplier()));
        txtFixedAmount.setText(String.valueOf(profile.getFixedAmount()));
        txtDailyWorkingHours.setText(String.valueOf(profile.getDailyWorkingHours()));
        txtEffectiveHours.setText(String.valueOf(profile.getEffectiveWorkingHours()));
    }

    public void bindSliderAndTextField() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtOverhead.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtOverhead.textProperty(),sliderOverheadMulti.valueProperty(),  converter);
    }

    @FXML
    private void updateProfile(ActionEvent actionEvent) {
        // Logic to update the profile in the model
        Profile profile = DataModelSingleton.getInstance().getCurrentProfile();
        profile.setFName(txtFName.getText());
        profile.setLName(txtLName.getText());
        profile.setAnnualSalary(Double.parseDouble(txtAnnualSalary.getText()));
        profile.setFixedAmount(Double.parseDouble(txtFixedAmount.getText()));
        profile.setDailyWorkingHours(Double.parseDouble(txtDailyWorkingHours.getText()));
        profile.setEffectiveWorkingHours(Double.parseDouble(txtEffectiveHours.getText()));
        profile.setOverheadMultiplier(Double.parseDouble(txtOverhead.getText()));

        try {
            profileModel.updateProfileRates(profile);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
            AlertBox.displayInfo("Update Failed", "Failed to update the rates.");
        }

        boolean success = profileModel.updateProfile(profile);
        if (success) {
            AlertBox.displayInfo("Profile Updated", "The profile has been successfully updated.");

            // callback to notify that the profile has been updated
            if (onProfileUpdated != null) {
                onProfileUpdated.run();
            }


            Stage stage = (Stage) txtFName.getScene().getWindow();
            stage.close();

        } else {
            AlertBox.displayInfo("Update Failed", "Failed to update the profile. Please try again.");
        }
    }
}
