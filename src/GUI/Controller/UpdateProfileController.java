package GUI.Controller;

import BE.Profile;
import CustomExceptions.ApplicationWideException;
import GUI.Model.ProfileModel;
import GUI.Utility.AlertBox;
import GUI.Utility.DataModelSingleton;
import GUI.Utility.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateProfileController implements Initializable {

    @FXML
    private TilePane tPaneProfileUpdateOverview;
    @FXML
    private TextField txtFName, txtLName, txtOverheadCost, txtAnnualSalary, txtFixedAmount, txtDailyWorkingHours, txtUtilizationInTheTeam;
    @FXML
    private Label lblProfileName;

    private FrameController frameController;
    private ProfileModel profileModel;
    private Runnable onProfileUpdated;  // Callback for updating the profile

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            profileModel = new ProfileModel();
            frameController = FrameController.getInstance();
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
        txtFixedAmount.setText(String.valueOf(profile.getFixedAmount()));
        txtDailyWorkingHours.setText(String.valueOf(profile.getDailyWorkingHours()));
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

        boolean success = profileModel.updateProfile(profile);
        if (success) {
            AlertBox.displayInfo("Profile Updated", "The profile has been successfully updated.");

            // Call the callback to notify that the profile has been updated
            if (onProfileUpdated != null) {
                onProfileUpdated.run();
            }

            // Close the update view
            Stage stage = (Stage) txtFName.getScene().getWindow();
            stage.close();

        } else {
            AlertBox.displayInfo("Update Failed", "Failed to update the profile. Please try again.");
        }
    }
}
