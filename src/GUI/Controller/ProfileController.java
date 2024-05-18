package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Model.ProfileModel;
import GUI.Model.ProjectTeamsModel;
import GUI.Utility.AlertBox;
import GUI.Utility.ExceptionHandler;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Controller class for the Profile view.
 * Handles the interaction between the Profile view and the model classes.
 */
public class ProfileController {


    public MFXLegacyTableView<Profile> tblProfiles;
    public TableColumn<Profile, String> colNameProfile, colRoleProfile,  colAnnualSalaryProfile;
    public TableColumn<Profile, String> colHourlyRateProfile, colDailyRateProfile;
    public TableColumn<Profile, Void> colDeleteIconProfile, colUpdateIconProfile;

    private Map<Integer, String> idToNameMap;

    // Instance of FrameController to control the main frame of the application
    private final FrameController frameController;
    private ProfileModel profileModel;
    private CountryModel countryModel;
    private GeographyModel geographyModel;
    private ProjectTeamsModel projectTeamsModel;


    public ProfileController() {

        this.frameController = FrameController.getInstance();
    }

    @FXML
    private void openCreateProfile(ActionEvent actionEvent) {
        frameController.loadCreateProfileView();
    }

    public void initialize() {

        try {
            profileModel = new ProfileModel();
            countryModel = new CountryModel();
            geographyModel = new GeographyModel();
            projectTeamsModel = new ProjectTeamsModel();
            loadProfiles();
            setCellValueFactories();
            idToNameMap = createIdToNameMap();

        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }

    }


    /**
     * Sets up the cell value factories for the table columns.
     * The cell value factories determine how the data for each cell is retrieved from the profile objects.
     */
    private void setCellValueFactories() {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        colNameProfile.setCellValueFactory(new PropertyValueFactory<>("fullName"));



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








        colUpdateIconProfile.setCellFactory(param -> new TableCell<Profile, Void>() {
            private final Button updateButton = createImageButton("/pictures/editLogo.png");

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
            private final Button deleteButton = createImageButton("/pictures/TrashLogo.png");

            {
                // Add an action event to the delete button
                deleteButton.setOnAction(event -> {
                    Profile profile = getTableView().getItems().get(getIndex());
                    deleteProfile(profile);
                });
            }

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

    private Button createImageButton(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);

        // Juster størrelsen på billedet
        imageView.setFitWidth(20);  // Erstat 20 med den ønskede bredde
        imageView.setFitHeight(20); // Erstat 20 med den ønskede højde
        imageView.setPreserveRatio(true);

        Button button = new Button("", imageView);
        button.setStyle("-fx-background-color: transparent;");

        // Centrer billedet på knappen
        button.setAlignment(Pos.CENTER);

        return button;
    }



    /**
     * Creates a map from id to name.
     * @return a map where the key is the id and the value is the name.
     */
    /**
     * Creates a map from team ID to team name.
     * This map is used for displaying the team name in the table view.
     * @return a map from team ID to team name
     */
    public Map<Integer, String> createIdToNameMap() {

        Map<Integer, String> idToNameMap = new HashMap<>();
            for (ProjectTeam team : projectTeamsModel.getAllProjectTeamsData()) {
                idToNameMap.put(team.getTeamId(), team.getTeamName());
            }
        return idToNameMap;
    }

    /**
     * Loads the profiles into the table.
     */
    private void loadProfiles() {
            tblProfiles.setItems(profileModel.getAllProfiles());
    }

    private void deleteProfile(Profile profile) {
        boolean success = profileModel.deleteProfile(profile);
        if (success) {
            tblProfiles.getItems().remove(profile);
            AlertBox.displayInfo("Profile Deleted", "Profile has been successfully deleted.");
        } else {
            AlertBox.displayInfo("Deletion Failed", "Failed to delete the profile.");
        }
    }
}
