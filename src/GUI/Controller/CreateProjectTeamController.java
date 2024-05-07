package GUI.Controller;

import BE.Country;
import BE.Profile;
import BE.ProjectTeam;
import GUI.Model.CountryModel;
import GUI.Model.ProfileModel;
import GUI.Model.ProjectTeamsModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;


import java.net.URL;
import java.text.NumberFormat;
import java.util.Map;
import java.util.ResourceBundle;


public class CreateProjectTeamController implements Initializable {

    @FXML
    private ComboBox<Country> cBoxCountry;

    @FXML
    private ComboBox<Profile> cBoxProfiles;

    @FXML
    private TableView<Profile> tblProfileToTeam;
    @FXML
    private TableColumn<Profile, String> colTeamCountry;
    @FXML
    private TableColumn<Profile, String> colTeamDailyRate;
    @FXML
    private TableColumn<Profile, String> colTeamHourlyRate;
    @FXML
    private TableColumn<Profile, String> colTeamAnnualSalary;
    @FXML
    private TableColumn<Profile, String> colTeamName;
    @FXML
    private TableColumn<Profile,Integer> colTeamCountryId;

    @FXML
    private TableColumn<Profile, Integer> colTeamProfileId;

    private Map<Integer, Country> countriesMap;

    @FXML
    private Label lblAnnualSalarySum,lblDailyRateSum, lblHourlyRateSum;

    @FXML
    private TextField txtProjectTeamName;

    private ProfileModel profileModel;
    private CountryModel countryModel;
    private ProjectTeamsModel projectTeamsModel;


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


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the combo boxes with data from the models.
     */
    private void populateComboBoxes() throws Exception {
        cBoxCountry.getItems().addAll(countryModel.getAllFromCountries());
        cBoxProfiles.getItems().addAll(profileModel.getAllProfiles());
        setCountryComboBoxConverter();
        setProfileComboBoxConverter();

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
                if (profile != null && countriesMap.containsKey(profile.getCountryId())) {
                    String countryName = countriesMap.get(profile.getCountryId()).getCountryName();
                    return profile.getfName() + " " + profile.getlName()+ "  -  "+ profile.getProjectRole() + "  -  " + countryName;
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

            lblAnnualSalarySum.setText(String.format("%.2f", annualSalarySum));
            lblDailyRateSum.setText(String.format("%.2f", dailyRateSum));
            lblHourlyRateSum.setText(String.format("%.2f", hourlyRateSum));
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
        }
    }

    /**
     * Sets the converter for the country combo box.
     */
    private void setCountryComboBoxConverter() {
        cBoxCountry.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country.getCountryName();
            }

            @Override
            public Country fromString(String string) {
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
        colTeamName.setCellValueFactory(new PropertyValueFactory<>("fullName")); // Assuming 'name' is the correct property
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
        colTeamCountry.setCellValueFactory(cellData -> {
            int countryId = cellData.getValue().getCountryId();
            return new SimpleStringProperty(countriesMap.getOrDefault(countryId, new Country("No Country")).getCountryName());
        });
    }

    /**
     * Adds the selected profile to the team table.
     */
    @FXML
    void selectProfileToTable(ActionEvent event) {
        Profile selectedProfile = cBoxProfiles.getSelectionModel().getSelectedItem();

        if (selectedProfile != null) {
            tblProfileToTeam.getItems().add(selectedProfile);
            cBoxProfiles.getSelectionModel().clearSelection();
        }
    }

    /**
     * Creates a new project team and adds it to the database.
     */
    @FXML
    void createProjectTeamToDatabase(ActionEvent event) {
        ObservableList<Profile> profiles = tblProfileToTeam.getItems();

        ProjectTeam projectTeam = new ProjectTeam(txtProjectTeamName.getText());
        projectTeam.setProfiles(profiles);

        Country selectedCountry = cBoxCountry.getSelectionModel().getSelectedItem();
        projectTeam.setCountry(selectedCountry);

        try {
            projectTeamsModel.addProfileToTeam(projectTeam);
            System.out.println(projectTeam + "hejsa");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        txtProjectTeamName.getText();
        cBoxCountry.getSelectionModel().getSelectedItem();
        System.out.println();colTeamAnnualSalary.getCellData(0);
        System.out.println("Create Project Team to Database" + txtProjectTeamName.getText());
        System.out.println("Profiles in team: " + tblProfileToTeam.getItems());
        System.out.println("Country: " + cBoxCountry.getSelectionModel().getSelectedItem());
    }

    /**
     * Removes the selected profile from the team table.
     */
    @FXML
    private void removeProfileFromTbl(ActionEvent actionEvent) {

        Profile selectedProfile = tblProfileToTeam.getSelectionModel().getSelectedItem();


        if (selectedProfile != null) {
            tblProfileToTeam.getItems().remove(selectedProfile);
        }
    }
}
