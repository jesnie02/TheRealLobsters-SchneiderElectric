package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import GUI.Model.*;
import GUI.Utility.DataModelSingleton;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateProjectTeamController implements Initializable {
    @FXML
    private TableView<Profile> tblProfileToTeam;
    @FXML
    private TableColumn<Profile, String> colTeamUtilization;
    @FXML
    private TableColumn<Profile, String> colTeamDailyRate;
    @FXML
    private TableColumn<Profile, String> colTeamHourlyRate;
    @FXML
    private TableColumn<Profile, String> colTeamAnnualSalary;
    @FXML
    private TableColumn<Profile, String> colTeamName;
    @FXML
    private TableColumn<Profile, Integer> colTeamCountryId;

    @FXML
    private TableColumn<Profile, Integer> colTeamProfileId;
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
    private Map<Integer, Country> countriesMap;
    private Map<Profile, Double> utilizationsMap = new HashMap<>();
    ProfileModel profileModel;
    CountryModel countryModel;
    ProfileRoleModel profileRoleModel;
    ProjectTeamsModel projectTeamsModel;
    private ObservableList<Profile> profiles = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            profileModel = new ProfileModel();
            countryModel = new CountryModel();
            profileRoleModel = new ProfileRoleModel();
            projectTeamsModel = new ProjectTeamsModel();
            setupSearchBox();
            setupTableView();
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

            profiles.setAll(projectTeamsModel.getProfileForTeam(currentTeam.getTeamId()));
            utilizationsMap.clear();

        }
    }

    public void refreshTeamData() throws Exception {
        initDataFromTeam();
    }

    private void setupTableView(){
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        if (countriesMap == null) {
            try {
                countriesMap = countryModel.getCountriesMap();
            } catch (Exception e) {
                System.err.println("Error loading countries: " + e.getMessage());
                return; // TODO: Handle this error
            }
        }

        // Define cell value factories with correct type parameters
        colTeamProfileId.setCellValueFactory(new PropertyValueFactory<>("profileId"));
        colTeamCountryId.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        colTeamName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colTeamHourlyRate.setCellValueFactory(cellData -> {
            double hourlySalary = cellData.getValue().getHourlySalary();
            return new SimpleStringProperty(formatter.format(hourlySalary));
        });
        colTeamDailyRate.setCellValueFactory(cellData -> {
            double dailyRate = cellData.getValue().getDailyRate();
            return new SimpleStringProperty(formatter.format(dailyRate));
        });
        colTeamAnnualSalary.setCellValueFactory(cellData -> {
            double annualSalary = cellData.getValue().getAnnualSalary();
            return new SimpleStringProperty(formatter.format(annualSalary));
        });
        colTeamUtilization.setCellValueFactory(cellData -> {
            double utilization = utilizationsMap.getOrDefault(cellData.getValue(), 0.0);
            return new SimpleStringProperty(formatter.format(utilization) + " %");
        });
        tblProfileToTeam.setItems(profiles);
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
