package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;
import GUI.Model.CountryModel;
import GUI.Model.ProfileModel;
import GUI.Model.ProjectTeamsModel;
import GUI.Utility.SliderDecimalFilter;
import GUI.Utility.ExceptionHandler;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.*;
import java.util.logging.Handler;

public class CreateProjectTeamController implements Initializable {

    @FXML
    private TableView<Profile> tblProfileToTeam;
    @FXML
    private TableColumn<Profile, String> colTeamUtilization, colTeamDailyRate, colTeamHourlyRate, colTeamAnnualSalary, colTeamName, colTeamCountryId, colTeamProfileId;
    @FXML
    private Label lblAnnualSalarySum, lblDailyRateSum, lblHourlyRateSum;
    @FXML
    private TextField txtProjectTeamName;
    @FXML
    private MFXSlider sliderUtilization;
    @FXML
    private TextField txtUtilization;
    @FXML
    private ComboBox<Geography> cBoxGeographies;
    @FXML

    private ComboBox<Profile> cBoxProfiles;
    private double utilization;
    private Map<Integer, Country> countriesMap;
    private Map<Profile, Double> utilizationsMap = new HashMap<>();
    private FilteredList<Profile> filteredProfiles;

    private ProfileModel profileModel;
    private CountryModel countryModel;
    private ProjectTeamsModel projectTeamsModel;

    public CreateProjectTeamController() {
    }

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
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void populateComboBoxes() {
        cBoxGeographies.getItems().addAll(countryModel.getAllFromGeographies());

        ObservableList<Profile> profiles = FXCollections.observableArrayList(profileModel.getAllProfiles());
        filteredProfiles = new FilteredList<>(profiles, p -> true);
        cBoxProfiles.setItems(filteredProfiles);

        setCountryComboBoxConverter();
        setProfileComboBoxConverter();

        cBoxProfiles.valueProperty().addListener((obs, oldProfile, newProfile) -> {
            if (newProfile != null) {
                Profile selectedProfile = (Profile) newProfile;
                double profileUtilization = selectedProfile.getTotalUtilization();
                sliderUtilization.setMax(100); // Set the maximum value of the slider to 100%
                sliderUtilization.setValue(profileUtilization);
                sliderUtilization.setUserData(profileUtilization); // Store the initial utilization value in userData
                utilization = profileUtilization;
                setTextinField();
            }
        });

        tblProfileToTeam.getItems().addListener((ListChangeListener<Profile>) change -> {
            filteredProfiles.setPredicate(profile -> !tblProfileToTeam.getItems().contains(profile));
        });
    }

    private void setProfileComboBoxConverter() {
        cBoxProfiles.setConverter(new StringConverter<Profile>() {
            @Override
            public String toString(Profile profile) {
                if (profile != null) {
                    return profile.getFName() + " " + profile.getLName();
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

    private void updateTotals() {
        double annualSalarySum = projectTeamsModel.calculateTotalAnnualSalary(tblProfileToTeam.getItems());
        double dailyRateSum = projectTeamsModel.calculateTotalDailyRate(tblProfileToTeam.getItems());
        double hourlyRateSum = projectTeamsModel.calculateTotalHourlyRate(tblProfileToTeam.getItems());

        lblAnnualSalarySum.setText(String.format("%.2f", annualSalarySum));
        lblDailyRateSum.setText(String.format("%.2f", dailyRateSum));
        lblHourlyRateSum.setText(String.format("%.2f", hourlyRateSum));
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

    public void setTblProfileToTeam() {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        if (countriesMap == null) {
            try {
                countriesMap = countryModel.getCountriesMap();
            } catch (ApplicationWideException e) {
                ExceptionHandler.handleException(e);
            }
        }

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
            return new SimpleStringProperty(formatter.format(utilization) + " %");
        });
    }

    @FXML
    public void selectProfileToTable(ActionEvent event) {
        Profile selectedProfile = (Profile) cBoxProfiles.getValue();

        if (selectedProfile != null) {
            utilizationsMap.put(selectedProfile, sliderUtilization.getValue());
            selectedProfile.setHourlyRate(selectedProfile.getHourlySalary()/100 * (utilizationsMap.get(selectedProfile)));
            selectedProfile.setDailyRate(selectedProfile.getDailyRate()/100 * (utilizationsMap.get(selectedProfile)));
            selectedProfile.setAnnualSalary(selectedProfile.getAnnualSalary()/100 * (utilizationsMap.get(selectedProfile)));
            tblProfileToTeam.getItems().add(selectedProfile);
            cBoxProfiles.setValue(null);
        }
    }

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
        if (selectedGeography != null) {
            projectTeam.setGeographyId(selectedGeography.getGeographyId());
        }

        projectTeamsModel.addProfileToTeam(projectTeam);

        txtProjectTeamName.clear();
        cBoxGeographies.setValue(null);
    }

    // Configures the slider for utilization input, ensuring it doesn't exceed the initial set value.
    private void setupSlider() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtUtilization.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtUtilization.textProperty(), sliderUtilization.valueProperty(), converter);

        // Ensure the slider cannot exceed the initial utilization value stored in userData
        sliderUtilization.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (sliderUtilization.getUserData() != null) {
                double initialUtilization = (double) sliderUtilization.getUserData();
                if (newValue.doubleValue() > initialUtilization) {
                    sliderUtilization.setValue(initialUtilization);
                } else {
                    utilization = newValue.doubleValue();
                }
            }
        });
    }

    private void setTextinField() {
        txtUtilization.setText(String.valueOf(utilization));
    }

    @FXML
    private void removeProfileFromTbl(ActionEvent actionEvent) {
        Profile selectedProfile = tblProfileToTeam.getSelectionModel().getSelectedItem();

        if (selectedProfile != null) {
            tblProfileToTeam.getItems().remove(selectedProfile);
        }
    }

    private void setRegexValidationForTextFields(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    private void setupRegex() {
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
