package GUI.Controller;

import BE.Profile;
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

public class ProfileController {


    public MFXLegacyTableView tblProfiles;
    public TableColumn colNameProfile, colTeamProfile, colCountryProfile, colRegionProfile, colAnnualSalaryProfile;
    public TableColumn colHourlyRateProfile, colDailyRateProfile, colUpdateIconProfile, colDeleteIconProfile, colProjectTeamProfile;

    // Instance of FrameController to control the main frame of the application
    private FrameController frameController;
    private ProfileModel profileModel;

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
        profileModel = new ProfileModel();
        loadProfiles();
        setCellValueFactories();
    }

    private void setCellValueFactories() {
        colNameProfile.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        //colTeamProfile.setCellValueFactory(new PropertyValueFactory<>("projectTeam"));
        //colCountryProfile.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        //colRegionProfile.setCellValueFactory(new PropertyValueFactory<>("GeographyId"));
        colProjectTeamProfile.setCellValueFactory(new PropertyValueFactory<>("projectRole"));
        colAnnualSalaryProfile.setCellValueFactory(new PropertyValueFactory<>("annualSalary"));
        colHourlyRateProfile.setCellValueFactory(new PropertyValueFactory<>("hourlySalary"));
        colDailyRateProfile.setCellValueFactory(new PropertyValueFactory<>("dailyRate"));

        colUpdateIconProfile.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        colDeleteIconProfile.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Object item, boolean empty) {
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
