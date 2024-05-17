package GUI.Controller;

import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import GUI.Model.*;
import GUI.Utility.DataModelSingleton;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateProjectTeamController implements Initializable {
    @FXML
    private TableColumn colTeamAnnualSalary;
    @FXML
    private TableColumn colTeamCountryId;
    @FXML
    private TableColumn colTeamHourlyRate;
    @FXML
    private TableColumn colTeamDailyRate;
    @FXML
    private TableColumn colTeamGeography;
    @FXML
    private TableColumn colTeamName;
    @FXML
    private TableColumn colTeamProfileId;
    @FXML
    private TableView tblProfileToTeam;
    @FXML
    private TextField txtUtilization;
    @FXML
    private MFXSlider sliderUtilization;
    @FXML
    private Label lblHourlyRateSum;
    @FXML
    private Label lblDailyRateSum;
    @FXML
    private Label lblAnnualSalarySum;
    @FXML
    private TextField txtProjectTeamName;
    @FXML
    private SearchableComboBox cBoxProfiles, cBoxGeographies;

    ProfileModel profileModel;
    CountryModel countryModel;
    ProfileRoleModel profileRoleModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            profileModel = new ProfileModel();
            countryModel = new CountryModel();
            profileRoleModel = new ProfileRoleModel();
            setupSearchBox();
            initDataFromTeam();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupSearchBox() throws Exception {
        cBoxProfiles.getItems().addAll(profileModel.getAllProfiles());
        cBoxGeographies.getItems().addAll(countryModel.getAllFromGeographies());
        setCountryComboBoxConverter();
        setProfileComboBoxConverter();
    }

    private void setCountryComboBoxConverter() {
        cBoxGeographies.setConverter(new StringConverter<Geography>() {
            @Override
            public String toString(Geography geography) {
                return geography.getGeographyName();
            }

            @Override
            public Geography fromString(String string) {
                return null;
            }
        });
    }

    private void setProfileComboBoxConverter() {
         cBoxProfiles.setConverter(new StringConverter<Profile>() {
            @Override
            public String toString(Profile profile) {
                if (profile != null) {
                    return profile.getFName() + " " + profile.getLName() ;
                } else {
                    return "Select a Profile";
                }
            }
            @Override
            public Profile fromString(String string) {
                return null;
            }
        });
    }

    public void initDataFromTeam() throws Exception {
        ProjectTeam currentTeam = DataModelSingleton.getInstance().getCurrentTeam();
        if (currentTeam != null) {
            txtProjectTeamName.setText(currentTeam.getTeamName());
            lblAnnualSalarySum.setText(String.format("%.2f", currentTeam.getSumOfAnnualSalary()));
            lblDailyRateSum.setText(String.format("%.2f", currentTeam.getSumOfDailyRate()));
            lblHourlyRateSum.setText(String.format("%.2f", currentTeam.getSumOfHourlyRate()));
        }
    }

    public void refreshTeamData() throws Exception {
        initDataFromTeam();
    }

    @FXML
    private void updateProjectTeam(ActionEvent actionEvent) {
    }

    @FXML
    private void removeProfileFromTbl(ActionEvent actionEvent) {
    }

    @FXML
    private void selectProfileToTable(ActionEvent actionEvent) {
    }


}
