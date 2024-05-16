package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import GUI.Model.CountryModel;
import GUI.Model.ProfileModel;
import GUI.Model.ProjectTeamsModel;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;


import java.net.URL;
import java.text.NumberFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.*;


public class CreateProjectTeamController implements Initializable {



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

    private Map<Integer, Country> countriesMap;

    private Map<Profile, Double> utilizationsMap = new HashMap<>();

    @FXML
    private Label lblAnnualSalarySum, lblDailyRateSum, lblHourlyRateSum;

    @FXML
    private TextField txtProjectTeamName;

    private ProfileModel profileModel;
    private CountryModel countryModel;
    private ProjectTeamsModel projectTeamsModel;
    private FrameController frameController;
    @FXML
    private MFXSlider sliderUtilization;
    @FXML
    private TextField txtUtilization;
    private double utilization;
    @FXML
    private ComboBox cBoxGeographies;
    @FXML
    private ComboBox cBoxProfiles;



    public CreateProjectTeamController() {

    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            countryModel = new CountryModel();
            profileModel = new ProfileModel();
            projectTeamsModel = new ProjectTeamsModel();
            populateComboBoxes();
            setTblProfileToTeam();
            setupSlider();
            setTextinField();
            setupRegex();
        } catch (Exception e) {
            throw new RuntimeException(e); //TODO: Handle this exception
        }
    }

    /**
     * Populates the combo boxes with data from the models.
     */
    private void populateComboBoxes() throws Exception {
        cBoxGeographies.getItems().addAll(countryModel.getAllFromGeographies());
        cBoxProfiles.getItems().addAll(profileModel.getAllProfiles());
        setCountryComboBoxConverter();
        setProfileComboBoxConverter();

        // Add a listener to the profile ComboBox's value property for utilization percentage
        cBoxProfiles.valueProperty().addListener((obs, oldProfile, newProfile) -> {
            if (newProfile != null) {
                Profile selectedProfile = (Profile) newProfile;
                double profileUtilization = selectedProfile.getTotalUtilization();
                sliderUtilization.setValue(profileUtilization);
                utilization = profileUtilization;
                setTextinField();
            }
        });
    }

    /**
     * Sets the converter for the profile combo box.
     */
    private void setProfileComboBoxConverter() {
        Map<Integer, Country> countriesMap;
        try {
            countriesMap = countryModel.getCountriesMap();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load countries", e); //TODO: Handle this exception
        }


        cBoxProfiles.setConverter(new StringConverter<Profile>() {
            @Override
            public String toString(Profile profile) {
                if (profile != null) {
                    return profile.getFName() + " " + profile.getLName() + "  -  " /*+ profile.getProjectRole()*/ ;
                } else {
                    return "Select a Profile";
                }
            }

            @Override
            public Profile fromString(String string) {
                return null;
            }
        });

        tblProfileToTeam.getItems().addListener((ListChangeListener<Profile>) change -> {
            updateTotals();
        });
    }

    /**
     * Updates the total values for the team.
     */
    private void updateTotals() {
        try {
            double annualSalarySum = projectTeamsModel.calculateTotalAnnualSalary(tblProfileToTeam.getItems());
            double dailyRateSum = projectTeamsModel.calculateTotalDailyRate(tblProfileToTeam.getItems());
            double hourlyRateSum = projectTeamsModel.calculateTotalHourlyRate(tblProfileToTeam.getItems());

            // the lbl are adjusted with utilization in method selectProfileToTable()

            lblAnnualSalarySum.setText(String.format("%.2f", annualSalarySum));
            lblDailyRateSum.setText(String.format("%.2f", dailyRateSum));
            lblHourlyRateSum.setText(String.format("%.2f", hourlyRateSum));
        } catch (Exception e) {
            e.printStackTrace(); // //TODO: Handle this exception
        }
    }

    /**
     * Sets the converter for the country combo box.
     */
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

    /**
     * Sets up the table view for the team profiles.
     */
    public void setTblProfileToTeam() {
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
            double utilization = utilizationsMap.get(cellData.getValue());
            return new SimpleStringProperty(formatter.format(utilization*100) + " %");
        });
    }

    /**
     * Adds the selected profile to the team table.
     */
    @FXML
    public void selectProfileToTable(ActionEvent event) {
        Profile selectedProfile = (Profile) cBoxProfiles.getValue();

        if (selectedProfile != null) {
            utilizationsMap.put(selectedProfile, sliderUtilization.getValue()/100);
            selectedProfile.setHourlyRate(selectedProfile.getHourlySalary()*(utilizationsMap.get(selectedProfile)));
            selectedProfile.setDailyRate(selectedProfile.getDailyRate()*(utilizationsMap.get(selectedProfile)));
            selectedProfile.setAnnualSalary(selectedProfile.getAnnualSalary()*(utilizationsMap.get(selectedProfile)));
            tblProfileToTeam.getItems().add(selectedProfile);
            cBoxProfiles.setValue(null);
        }
    }

    /**
     * Creates a new project team and adds it to the database.
     */
    @FXML
    public void createProjectTeamToDatabase(ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        ObservableList<Profile> profiles = tblProfileToTeam.getItems();

        ProjectTeam projectTeam = new ProjectTeam(txtProjectTeamName.getText());
        projectTeam.setProfiles(profiles);
        projectTeam.setUtilizationsMap(utilizationsMap);

        Geography selectedGeography = (Geography) cBoxGeographies.getValue();
        if(selectedGeography != null){
            projectTeam.setGeographyId(selectedGeography.getGeographyId());
        }
        try {
            projectTeamsModel.addProfileToTeam(projectTeam);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        txtProjectTeamName.clear();
        cBoxGeographies.setValue(null); // Clear the selection
    }

    private void setupSlider(){
        sliderUtilization.valueProperty().addListener((observable, oldValue, newValue) -> {
            utilization = newValue.doubleValue();
            setTextinField();
        });
    }

    private void setTextinField(){
        txtUtilization.setText(String.valueOf(utilization));
    }

        /**
         * Removes the selected profile from the team table.
         */
        @FXML
        private void removeProfileFromTbl (ActionEvent actionEvent){

            Profile selectedProfile = tblProfileToTeam.getSelectionModel().getSelectedItem();


            if (selectedProfile != null) {
                tblProfileToTeam.getItems().remove(selectedProfile);
            }
        }

    private void setRegexValidationForTextFields(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    private void setupRegex(){
        setRegexValidationForTextFields(txtUtilization);
    }


    private boolean validateInput() {
        boolean isValid = true;

        if (txtProjectTeamName.getText().isEmpty()) {
            txtProjectTeamName.setStyle("-fx-border-color: red");
            isValid = false;
        } else {
            txtProjectTeamName.setStyle("");
        }

        if (cBoxGeographies.getValue() == null) {
            cBoxGeographies.setStyle("-fx-border-color: red");
            isValid = false;
        } else {
            cBoxGeographies.setStyle("");
        }

        if (tblProfileToTeam.getItems().isEmpty()) {
            tblProfileToTeam.setStyle("-fx-border-color: red");
            isValid = false;
        } else {
            tblProfileToTeam.setStyle("");
        }
        return isValid;
    }
}


