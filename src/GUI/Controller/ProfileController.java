package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Model.ProfileModel;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.text.NumberFormat;

public class ProfileController {


    public MFXLegacyTableView<Profile> tblProfiles;
    public TableColumn<Profile, String> colNameProfile, colTeamProfile, colCountryProfile, colRegionProfile, colAnnualSalaryProfile;
    public TableColumn<Profile, String> colHourlyRateProfile, colDailyRateProfile, colProjectTeamProfile;
    public TableColumn<Profile, Void> colDeleteIconProfile, colUpdateIconProfile;


    // Instance of FrameController to control the main frame of the application
    private final FrameController frameController;
    private ProfileModel profileModel;
    private CountryModel countryModel;
    private GeographyModel geographyModel;

    /**
     * Constructor for the ProfileController class.
     * It initializes the frameController variable with the instance of FrameController.
     */
    public ProfileController() {

        this.frameController = FrameController.getInstance();
    }

    /**
     * This method is called when the create profile button is clicked.
     * It loads the create profile view.
     */
    @FXML
    private void openCreateProfile(ActionEvent actionEvent) {
        frameController.loadCreateProfileView();
    }

    public void initialize() {

        try {
            profileModel = new ProfileModel();
            countryModel = new CountryModel();
            geographyModel = new GeographyModel();
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: Handle this exception
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        loadProfiles();
        setCellValueFactories();
    }

    private void setCellValueFactories() {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        colNameProfile.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        //colTeamProfile.setCellValueFactory(new PropertyValueFactory<>("projectTeam"));
        //colCountryProfile.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        //colRegionProfile.setCellValueFactory(new PropertyValueFactory<>("GeographyId"));
        colProjectTeamProfile.setCellValueFactory(new PropertyValueFactory<>("projectRole"));
        colAnnualSalaryProfile.setCellValueFactory(cellData -> {
            double annualSalary = cellData.getValue().getAnnualSalary();
            return new SimpleStringProperty(formatter.format(annualSalary));
        });
        colHourlyRateProfile.setCellValueFactory(cellData -> {
            double hourlySalary = cellData.getValue().getHourlySalary();
            return new SimpleStringProperty(formatter.format(hourlySalary));
        });

        colDailyRateProfile.setCellValueFactory(cellData -> {
            double dailyRate = cellData.getValue().getDailyRate();
            return new SimpleStringProperty(formatter.format(dailyRate));
        });

        colCountryProfile.setCellValueFactory(cellData -> {
            Profile profile = cellData.getValue();
            int countryId = profile.getCountryId();
            Country country = null;
            try {
                country = countryModel.getCountriesMap().get(countryId);
            } catch (Exception e) {
                throw new RuntimeException(e); // TODO: Handle this exception
            }
            return new SimpleStringProperty(country != null ? country.getCountryName() : "No Country");
        });


        colTeamProfile.setCellValueFactory(cellData -> {
            Profile profile = cellData.getValue();
            String team = profile.getProjectTeam();
            return new SimpleStringProperty(team != null ? team : "No Team");
        });

        colCountryProfile.setCellValueFactory(cellData -> {
            Profile profile = cellData.getValue();
            int countryId = profile.getCountryId();
            Country country = null;
            try {
                country = countryModel.getCountriesMap().get(countryId);
            } catch (Exception e) {
                throw new RuntimeException(e); // TODO: Handle this exception
            }
            return new SimpleStringProperty(country != null ? country.getCountryName() : "No Country");
        });


        colUpdateIconProfile.setCellFactory(param -> new TableCell<Profile, Void>() {
            private final Button updateButton = new Button("Update");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        colDeleteIconProfile.setCellFactory(param -> new TableCell<Profile,Void>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void loadProfiles() {
        tblProfiles.setItems(profileModel.getAllProfiles());
    }
}
