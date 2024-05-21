package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;
import GUI.Model.*;
import GUI.Utility.AlertBox;
import GUI.Utility.DataModelSingleton;
import GUI.Utility.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private SearchableComboBox<Profile> cBoxProfiles;
    @FXML
    private SearchableComboBox<Geography> cBoxGeographies;
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
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void setupSearchBox() {
        cBoxProfiles.setItems(FXCollections.observableArrayList(profileModel.getAllProfiles()));
        cBoxGeographies.setItems(FXCollections.observableArrayList(countryModel.getAllFromGeographies()));
        setCountryComboBoxConverter();
        setProfileComboBoxConverter();
    }

    private void setCountryComboBoxConverter() {
        cBoxGeographies.setConverter(new StringConverter<Geography>() {
            @Override
            public String toString(Geography geography) {

                return geography != null ? geography.getGeographyName() : " ";
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
                return profile != null ? profile.getFName() + " " + profile.getLName() : "Select a Profile";
            }
            @Override
            public Profile fromString(String string) {
                return null;
            }
        });
    }

    public void initDataFromTeam(){
        ProjectTeam currentTeam = DataModelSingleton.getInstance().getCurrentTeam();
        if (currentTeam != null) {
            txtProjectTeamName.setText(currentTeam.getTeamName());
            System.out.println("InitData, UpdateTeamController: " + currentTeam);
            lblAnnualSalarySum.setText(String.format("%.2f", currentTeam.getSumOfAnnualSalary()));
            lblDailyRateSum.setText(String.format("%.2f", currentTeam.getSumOfDailyRate()));
            lblHourlyRateSum.setText(String.format("%.2f", currentTeam.getSumOfHourlyRate()));
            cBoxGeographies.setValue(currentTeam.getGeography());

            profiles.setAll(projectTeamsModel.getProfileForTeam(currentTeam.getTeamId()));
            utilizationsMap.clear();

           for (Profile profile : profiles) {
                utilizationsMap.put(profile, Double.valueOf(profile.getUtilization()));
            }

           tblProfileToTeam.setItems(profiles);
           tblProfileToTeam.refresh();

        }
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
        try {
            String teamName = txtProjectTeamName.getText();
            ObservableList<Profile> profilesInTeam = tblProfileToTeam.getItems();
            if (teamName == null || teamName.isEmpty() || profilesInTeam.isEmpty()) {
                AlertBox.displayInfo("Invalid Input", "Team name and profiles cannot be empty.");
                return;
            }
            ProjectTeam currentTeam = DataModelSingleton.getInstance().getCurrentTeam();
            currentTeam.setTeamName(teamName);
            currentTeam.setProfiles(profilesInTeam);
            currentTeam.setUtilizationsMap(utilizationsMap);

            currentTeam.setGeography(cBoxGeographies.getValue());

            projectTeamsModel.updateTeam(currentTeam);
            AlertBox.displayInfo("Success", "The team " + teamName + " has been successfully updated.");
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
            AlertBox.displayError(e);
        }
    }


    @FXML
    private void removeProfileFromTbl(ActionEvent actionEvent) {
        Profile selectedProfile = tblProfileToTeam.getSelectionModel().getSelectedItem();


        if (selectedProfile != null) {
            tblProfileToTeam.getItems().remove(selectedProfile);
        }
    }

    @FXML
    private void selectProfileToTable(ActionEvent actionEvent) {
        Profile selectedProfile = (Profile) cBoxProfiles.getValue();

        if (selectedProfile != null) {
            utilizationsMap.put(selectedProfile, sliderUtilization.getValue());
            selectedProfile.setHourlyRate(selectedProfile.getHourlySalary()*(utilizationsMap.get(selectedProfile)));
            selectedProfile.setDailyRate(selectedProfile.getDailyRate()*(utilizationsMap.get(selectedProfile)));
            selectedProfile.setAnnualSalary(selectedProfile.getAnnualSalary()*(utilizationsMap.get(selectedProfile)));
            tblProfileToTeam.getItems().add(selectedProfile);
            cBoxProfiles.setValue(null);
        }
    }


}
