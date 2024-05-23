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
import javafx.scene.paint.Color;

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
    private TableColumn<Profile, String> colTeamUtilizationTime, colTeamUtilizationCost, colTeamDailyRate, colTeamHourlyRate, colTeamAnnualSalary, colTeamName, colTeamCountryId, colTeamProfileId;
    @FXML
    private Label lblAnnualSalarySum, lblDailyRateSum, lblHourlyRateSum, lblMessageCreateTeam;
    @FXML
    private TextField txtProjectTeamName;
    @FXML
    private MFXSlider sliderUtilizationTime, sliderUtilizationCost;
    @FXML
    private TextField txtUtilizationTime, txtUtilizationCost;
    @FXML
    private ComboBox<Geography> cBoxGeographies;
    @FXML
    private ComboBox<Profile> cBoxProfiles;

    private double utilizationTime;
    private double utilizationCost;
    private Map<Integer, Country> countriesMap;
    private Map<Profile, Double> utilizationTimeMap = new HashMap<>();
    private Map<Profile, Double> utilizationCostMap = new HashMap<>();
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
            setupSliderTime();
            setupSliderCost();
            setTextinFieldTime();
            setTextinFieldCost();
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
                Profile selectedProfile = newProfile;
                double profileUtilizationTime = selectedProfile.getTotalUtilization();
                double profileUtilizationCost = selectedProfile.getUtilizationCost(); // Correctly fetch utilization cost


                sliderUtilizationTime.setMax(100);
                sliderUtilizationTime.setValue(profileUtilizationTime);
                sliderUtilizationTime.setUserData(profileUtilizationTime);

                sliderUtilizationCost.setMax(100);
                sliderUtilizationCost.setValue(profileUtilizationCost);
                sliderUtilizationCost.setUserData(profileUtilizationCost);

                utilizationTime = profileUtilizationTime;
                utilizationCost = profileUtilizationCost;

                setTextinFieldTime();
                setTextinFieldCost();
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

    private void setTblProfileToTeam() {
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
        colTeamUtilizationTime.setCellValueFactory(cellData -> {
            double utilization = utilizationTimeMap.getOrDefault(cellData.getValue(), 0.0);
            return new SimpleStringProperty(formatter.format(utilization) + " %");
        });
        colTeamUtilizationCost.setCellValueFactory(cellData -> {
            double utilization = utilizationCostMap.getOrDefault(cellData.getValue(), 0.0);
            return new SimpleStringProperty(formatter.format(utilization) + " %");
        });
    }

    @FXML
    public void selectProfileToTable(ActionEvent event) {
        Profile selectedProfile = cBoxProfiles.getValue();

        if (selectedProfile != null) {
            utilizationTimeMap.put(selectedProfile, sliderUtilizationTime.getValue());
            utilizationCostMap.put(selectedProfile, sliderUtilizationCost.getValue());

            selectedProfile.setHourlyRate(selectedProfile.getHourlySalary() / 100 * utilizationCostMap.get(selectedProfile));
            selectedProfile.setDailyRate(selectedProfile.getDailyRate() / 100 * utilizationCostMap.get(selectedProfile));
            selectedProfile.setAnnualSalary(selectedProfile.getAnnualSalary() / 100 * utilizationCostMap.get(selectedProfile));

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
        projectTeam.setUtilizationsMap(utilizationTimeMap);
        projectTeam.setUtilizationCostMap(utilizationCostMap);

        Geography selectedGeography = cBoxGeographies.getValue();
        if (selectedGeography != null) {
            projectTeam.setGeographyId(selectedGeography.getGeographyId());
        }

        try {
            projectTeamsModel.addProfileToTeam(projectTeam);
        } catch (ApplicationWideException e) {
            lblMessageCreateTeam.setTextFill(Color.RED);
            lblMessageCreateTeam.setText("Error occurred during the creation of the team.");
        }

        txtProjectTeamName.clear();
        cBoxGeographies.setValue(null);

        lblMessageCreateTeam.setTextFill(Color.GREEN);
        lblMessageCreateTeam.setText("The team was created successfully.");
    }

    private void setupSliderTime() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtUtilizationTime.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtUtilizationTime.textProperty(), sliderUtilizationTime.valueProperty(), converter);

        sliderUtilizationTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (sliderUtilizationTime.getUserData() != null) {
                double initialUtilization = (double) sliderUtilizationTime.getUserData();
                if (newValue.doubleValue() > initialUtilization) {
                    sliderUtilizationTime.setValue(initialUtilization);
                } else {
                    utilizationTime = newValue.doubleValue();
                }
            }
        });
    }

    private void setupSliderCost() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtUtilizationCost.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtUtilizationCost.textProperty(), sliderUtilizationCost.valueProperty(), converter);

        sliderUtilizationCost.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (sliderUtilizationCost.getUserData() != null) {
                double initialUtilization = (double) sliderUtilizationCost.getUserData();
                if (newValue.doubleValue() > initialUtilization) {
                    sliderUtilizationCost.setValue(initialUtilization);
                } else {
                    utilizationCost = newValue.doubleValue();
                }
            } else {
            }
        });
    }


    private void setTextinFieldTime() {
        txtUtilizationTime.setText(String.valueOf(utilizationTime));
    }

    private void setTextinFieldCost() {
        txtUtilizationCost.setText(String.valueOf(utilizationCost));
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
        setRegexValidationForTextFields(txtUtilizationTime);
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

