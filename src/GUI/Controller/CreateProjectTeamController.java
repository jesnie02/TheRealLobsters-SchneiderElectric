package GUI.Controller;

import BE.Country;
import BE.Profile;
import BE.ProjectTeam;
import GUI.Model.CountryModel;
import GUI.Model.ProfileModel;
import GUI.Model.TeamsModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;


import java.net.URL;
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
    private TableColumn<Profile, Double> colTeamDailyRate;
    @FXML
    private TableColumn<Profile, Double> colTeamHourlyRate;
    @FXML
    private TableColumn<Profile, Double> colTeamAnnualSalary;
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
    private TeamsModel teamsModel;




    public CreateProjectTeamController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            countryModel = new CountryModel();
            profileModel = new ProfileModel();
            teamsModel = new TeamsModel();
            populateComboBoxes();
            setTblProfileToTeam();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void populateComboBoxes() throws Exception {
        cBoxCountry.getItems().addAll(countryModel.getAllFromCountries());
        cBoxProfiles.getItems().addAll(profileModel.getAllProfiles());
        setCountryComboBoxConverter();
        setProfileComboBoxConverter();

    }


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
    }

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

    public void setTblProfileToTeam() {

        if (countriesMap == null) {
            try {
                countriesMap = countryModel.getCountriesMap();
            } catch (Exception e) {
                System.err.println("Error loading countries: " + e.getMessage());
                return; // Handle error gracefully
            }
        }

        // Define cell value factories with correct type parameters
        colTeamProfileId.setCellValueFactory(new PropertyValueFactory<>("profileId"));
        colTeamCountryId.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        colTeamName.setCellValueFactory(new PropertyValueFactory<>("fullName")); // Assuming 'name' is the correct property
        colTeamHourlyRate.setCellValueFactory(new PropertyValueFactory<>("hourlySalary"));
        colTeamDailyRate.setCellValueFactory(new PropertyValueFactory<>("dailyRate"));
        colTeamAnnualSalary.setCellValueFactory(new PropertyValueFactory<>("annualSalary"));
        colTeamCountry.setCellValueFactory(cellData -> {
            int countryId = cellData.getValue().getCountryId();
            return new SimpleStringProperty(countriesMap.getOrDefault(countryId, new Country("No Country")).getCountryName());
        });
    }

    @FXML
    void selectProfileToTable(ActionEvent event) {
        Profile selectedProfile = cBoxProfiles.getSelectionModel().getSelectedItem();

        if (selectedProfile != null) {
            tblProfileToTeam.getItems().add(selectedProfile);
            cBoxProfiles.getSelectionModel().clearSelection();
        }
    }


    @FXML
    void createProjectTeamToDatabase(ActionEvent event) {
        ObservableList<Profile> profiles = tblProfileToTeam.getItems();

        ProjectTeam projectTeam = new ProjectTeam(txtProjectTeamName.getText());
        projectTeam.setProfiles(profiles);

        try {
            teamsModel.addProfileToTeam(projectTeam);
            System.out.println(projectTeam + "hejsa");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        txtProjectTeamName.getText();
        cBoxCountry.getSelectionModel().getSelectedItem();
        System.out.println("Create Project Team to Database" + txtProjectTeamName.getText());
        System.out.println("Profiles in team: " + tblProfileToTeam.getItems());
        System.out.println("Country: " + cBoxCountry.getSelectionModel().getSelectedItem());
    }



}
