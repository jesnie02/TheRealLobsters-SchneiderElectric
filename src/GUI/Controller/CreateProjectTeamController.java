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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.*;

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
        double annualSalarySum = 0.0;
        double dailyRateSum = 0.0;
        double hourlyRateSum = 0.0;

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        for (Profile profile : tblProfileToTeam.getItems()) {
            double utilizationCost = utilizationCostMap.getOrDefault(profile, 0.0);
            double annualSalary = (profile.getAnnualSalary()+profile.getFixedAmount()) * (utilizationCost / 100);
            double dailyRate = profile.getDailyRate() * (utilizationCost / 100);
            double hourlyRate = profile.getHourlySalary() * (utilizationCost / 100);

            annualSalarySum += annualSalary;
            dailyRateSum += dailyRate;
            hourlyRateSum += hourlyRate;
        }

        lblAnnualSalarySum.setText(formatter.format(annualSalarySum));
        lblDailyRateSum.setText(formatter.format(dailyRateSum));
        lblHourlyRateSum.setText(formatter.format(hourlyRateSum));
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
            Profile profile = cellData.getValue();
            double hourlySalary = profile.getHourlySalary();
            double utilizationCost = utilizationCostMap.getOrDefault(profile, 100.0); // Default to 100% if not set
            double adjustedHourlyRate = hourlySalary * (utilizationCost / 100);
            return new SimpleStringProperty(formatter.format(adjustedHourlyRate));
        });

        colTeamDailyRate.setCellValueFactory(cellData -> {
            Profile profile = cellData.getValue();
            double dailyRate = profile.getDailyRate();
            double utilizationCost = utilizationCostMap.getOrDefault(profile, 100.0); // Default to 100% if not set
            double adjustedDailyRate = dailyRate * (utilizationCost / 100);
            return new SimpleStringProperty(formatter.format(adjustedDailyRate));
        });

        colTeamAnnualSalary.setCellValueFactory(cellData -> {
            Profile profile = cellData.getValue();
            double annualSalary = profile.getAnnualSalary();
            double fixedAmount = profile.getFixedAmount();
            double utilizationCost = utilizationCostMap.getOrDefault(profile, 100.0); // Default to 100% if not set
            double totalAnnualSalary = (annualSalary + fixedAmount) * (utilizationCost / 100);
            return new SimpleStringProperty(formatter.format(totalAnnualSalary));
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
            double utilizationTime = sliderUtilizationTime.getValue();
            double utilizationCost = sliderUtilizationCost.getValue();

            utilizationTimeMap.put(selectedProfile, utilizationTime);
            utilizationCostMap.put(selectedProfile, utilizationCost);

            tblProfileToTeam.getItems().add(selectedProfile);
            cBoxProfiles.setValue(null);
            updateTotals();
        }
    }


    public void createProjectTeam(ActionEvent event) {
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

        // Calculate and set total values for the team
        double totalAnnualSalary = profiles.stream()
                .mapToDouble(profile -> {
                    double utilizationCost = utilizationCostMap.getOrDefault(profile, 100.0); // Default to 100% if not set
                    return (profile.getAnnualSalary() + profile.getFixedAmount()) * (utilizationCost / 100);
                })
                .sum();

        double totalDailyRate = profiles.stream()
                .mapToDouble(profile -> {
                    double utilizationCost = utilizationCostMap.getOrDefault(profile, 100.0); // Default to 100% if not set
                    return profile.getDailyRate() * (utilizationCost / 100);
                })
                .sum();

        double totalHourlyRate = profiles.stream()
                .mapToDouble(profile -> {
                    double utilizationCost = utilizationCostMap.getOrDefault(profile, 100.0); // Default to 100% if not set
                    return profile.getHourlySalary() * (utilizationCost / 100);
                })
                .sum();

        projectTeam.setSumOfAnnualSalary(totalAnnualSalary);
        projectTeam.setSumOfDailyRate(totalDailyRate);
        projectTeam.setSumOfHourlyRate(totalHourlyRate);

        try {
            projectTeamsModel.addProfileToTeam(projectTeam);


            lblMessageCreateTeam.setTextFill(Color.GREEN);
            lblMessageCreateTeam.setText("The team was created successfully.");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> {
                lblMessageCreateTeam.setText("");
            }));
            timeline.play();
            FrameController.getInstance().loadTeamsView();
            TeamsController.getInstance().loadTeamsInTilePane();
        } catch (ApplicationWideException e) {
            lblMessageCreateTeam.setTextFill(Color.RED);
            lblMessageCreateTeam.setText("Error occurred during the creation of the team: " + e.getMessage());
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> {
                lblMessageCreateTeam.setText("");
            }));
            timeline.play();
        }
        clearInputInFields();

    }


    private void clearInputInFields() {
        txtProjectTeamName.clear();
        cBoxGeographies.getSelectionModel().clearSelection();
        tblProfileToTeam.getItems().clear();
        sliderUtilizationCost.setValue(0);
        sliderUtilizationTime.setValue(0);

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
        setRegexValidationForTextFields(txtUtilizationCost);
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

