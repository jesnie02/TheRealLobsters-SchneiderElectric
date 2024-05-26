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
import GUI.Utility.SliderDecimalFilter;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateProjectTeamController implements Initializable {

    @FXML
    private TableView<Profile> tblProfileToTeam;
    @FXML
    private TableColumn<Profile, String> colTeamAnnualSalary, colTeamName, colTeamHourlyRate, colTeamDailyRate, colTeamUtilizationTime, colTeamUtilizationCost;
    @FXML
    private TableColumn<Profile, Integer> colTeamCountryId, colTeamProfileId;
    @FXML
    private Label lblDailyRateSum, lblAnnualSalarySum, lblHourlyRateSum;
    @FXML
    private MFXSlider sliderUtilizationTime, sliderUtilizationCost;
    @FXML
    private TextField txtUtilizationTime, txtUtilizationCost, txtProjectTeamName;
    @FXML
    private SearchableComboBox<Profile> cBoxProfiles;
    @FXML
    private SearchableComboBox<Geography> cBoxGeographies;

    private Map<Integer, Country> countriesMap;
    private Map<Profile, Double> utilizationsTimeMap = new HashMap<>();
    private Map<Profile, Double> utilizationsCostMap = new HashMap<>();

    private ObservableList<Profile> profiles = FXCollections.observableArrayList();
    private double utilizationTime;
    private double utilizationCost;

    ProfileModel profileModel;
    CountryModel countryModel;
    ProfileRoleModel profileRoleModel;
    ProjectTeamsModel projectTeamsModel;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            profileModel = new ProfileModel();
            countryModel = new CountryModel();
            profileRoleModel = new ProfileRoleModel();
            projectTeamsModel = new ProjectTeamsModel();
            populateComboBoxes();
            setupSearchBox();
            setupTableView();
            bindSliderAndTextFieldTime();
            bindSliderAndTextFieldCost();
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

    public void bindSliderAndTextFieldTime() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtUtilizationTime.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtUtilizationTime.textProperty(),sliderUtilizationTime.valueProperty(),  converter);
    }

    public void bindSliderAndTextFieldCost() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtUtilizationCost.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtUtilizationCost.textProperty(),sliderUtilizationCost.valueProperty(),  converter);
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
            lblAnnualSalarySum.setText(String.format("%.2f", currentTeam.getSumOfAnnualSalary()));
            lblDailyRateSum.setText(String.format("%.2f", currentTeam.getSumOfDailyRate()));
            lblHourlyRateSum.setText(String.format("%.2f", currentTeam.getSumOfHourlyRate()));
            cBoxGeographies.setValue(currentTeam.getGeography());

            profiles.setAll(projectTeamsModel.getProfileForTeam(currentTeam.getTeamId()));
            utilizationsTimeMap.clear();

           for (Profile profile : profiles) {
                utilizationsTimeMap.put(profile, Double.valueOf(profile.getUtilizationTime()));
                utilizationsCostMap.put(profile, Double.valueOf(profile.getUtilizationCost()));
            }

           tblProfileToTeam.setItems(profiles);
           tblProfileToTeam.refresh();

        }
    }

    private void updateSumLabels() {
        double hourlyRateSum = 0;
        double dailyRateSum = 0;
        double annualSalarySum = 0;
        for (Profile profile : profiles) {
            hourlyRateSum += profile.getHourlyRate();
            dailyRateSum += profile.getDailyRate();
            annualSalarySum += profile.getAnnualSalary();
        }
        lblHourlyRateSum.setText(String.format("%.2f", hourlyRateSum));
        lblDailyRateSum.setText(String.format("%.2f", dailyRateSum));
        lblAnnualSalarySum.setText(String.format("%.2f", annualSalarySum));
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
                return;
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
        colTeamUtilizationTime.setCellValueFactory(cellData -> {
            double utilization = utilizationsTimeMap.getOrDefault(cellData.getValue(), 0.0);
            return new SimpleStringProperty(formatter.format(utilization) + " %");
        });
        colTeamUtilizationCost.setCellValueFactory(cellData -> {
            double utilization = utilizationsCostMap.getOrDefault(cellData.getValue(), 0.0);
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
            Geography selectedGeography = cBoxGeographies.getValue();
            if (selectedGeography != null) {
                currentTeam.setGeographyId(selectedGeography.getGeographyId());
            }
            currentTeam.setProfiles(profilesInTeam);
            currentTeam.setUtilizationsMap(utilizationsTimeMap);
            currentTeam.setUtilizationCostMap(utilizationsCostMap);
            currentTeam.setNumberOfProfiles(profilesInTeam.size());


            String hourlyRateStr = lblHourlyRateSum.getText().replace(",", ".");
            String dailyRateStr = lblDailyRateSum.getText().replace(",", ".");
            String annualSalaryStr = lblAnnualSalarySum.getText().replace(",", ".");

            currentTeam.setSumOfHourlyRate(Double.parseDouble(hourlyRateStr));
            currentTeam.setSumOfDailyRate(Double.parseDouble(dailyRateStr));
            currentTeam.setSumOfAnnualSalary(Double.parseDouble(annualSalaryStr));

            projectTeamsModel.updateTeam(currentTeam);

            AlertBox.displayInfo("Success", "The team " + teamName + " has been successfully updated.");
            FrameController.getInstance().loadTeamsView();
            TeamsController.getInstance().loadTeamsInTilePane();

        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
            AlertBox.displayError(e);
        }
    }

    @FXML
    private void removeProfileFromTbl(ActionEvent actionEvent) {
        Profile selectedProfile = tblProfileToTeam.getSelectionModel().getSelectedItem();
        if (selectedProfile != null) {
            profiles.remove(selectedProfile);
            utilizationsCostMap.remove(selectedProfile);
            utilizationsTimeMap.remove(selectedProfile);
            tblProfileToTeam.refresh();
            projectTeamsModel.removeProfileFromTeam(DataModelSingleton.getInstance().getCurrentTeam().getTeamId(), selectedProfile.getProfileId());
            updateSumLabels();
        }
    }

    @FXML
    private void selectProfileToTable(ActionEvent actionEvent) {
        Profile selectedProfile = cBoxProfiles.getValue();


        if (selectedProfile != null) {
            double utilizationTimeValue = sliderUtilizationTime.getValue();
            double utilizationCostValue = sliderUtilizationCost.getValue();

            utilizationsTimeMap.put(selectedProfile, utilizationTimeValue);
            utilizationsCostMap.put(selectedProfile, utilizationCostValue);

            selectedProfile.setHourlyRate(selectedProfile.getHourlySalary() / 100 * utilizationsCostMap.get(selectedProfile));
            selectedProfile.setDailyRate(selectedProfile.getDailyRate() / 100 * utilizationsCostMap.get(selectedProfile));
            selectedProfile.setAnnualSalary(selectedProfile.getAnnualSalary() / 100 * utilizationsCostMap.get(selectedProfile));
            tblProfileToTeam.getItems().add(selectedProfile);
            cBoxProfiles.setValue(null);
            updateSumLabels();
        }
    }

    private void populateComboBoxes() {
            cBoxProfiles.setItems(FXCollections.observableArrayList(profileModel.getAllProfiles()));
            cBoxGeographies.setItems(FXCollections.observableArrayList(countryModel.getAllFromGeographies()));

        cBoxProfiles.valueProperty().addListener((obs, oldProfile, newProfile) -> {
            if (newProfile != null) {
                txtUtilizationTime.setText(String.valueOf(utilizationsTimeMap.getOrDefault(newProfile, 0.0)));
                txtUtilizationCost.setText(String.valueOf(utilizationsCostMap.getOrDefault(newProfile, 0.0)));
                sliderUtilizationTime.setMax(100);
                Profile selectedProfile = newProfile;
                double profileUtilizationTime = selectedProfile.getTotalUtilization();
                double profileUtilizationCost = selectedProfile.getUtilizationCost();

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
        }

    private void setTextinFieldTime() {
        txtUtilizationTime.setText(String.valueOf(utilizationTime));
    }

    private void setTextinFieldCost() {
        txtUtilizationCost.setText(String.valueOf(utilizationCost));
    }
    }



