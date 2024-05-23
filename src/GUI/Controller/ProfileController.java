package GUI.Controller;

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
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class for the Profile view.
 * Handles the interaction between the Profile view and the model classes.
 */
public class ProfileController {

    @FXML
    public MFXLegacyTableView<Profile> tblProfiles;
    @FXML
    public TableColumn<Profile, String> colNameProfile, colAnnualSalaryProfile;
    @FXML
    public TableColumn<Profile, String> colHourlyRateProfile, colDailyRateProfile;
    @FXML
    public TableColumn<Profile, Void> colDeleteIconProfile, colUpdateIconProfile;

    private Map<Integer, String> idToNameMap;

    // Instance of FrameController to control the main frame of the application
    private final FrameController frameController;
    private ProfileModel profileModel;
    private CountryModel countryModel;
    private GeographyModel geographyModel;
    private ProjectTeamsModel projectTeamsModel;


    public ProfileController() {
        profileModel = ProfileModel.getInstance();
        this.frameController = FrameController.getInstance();
    }

    @FXML
    private void openCreateProfile(ActionEvent actionEvent) {
        frameController.loadCreateProfileView();
    }

    public void initialize() {
        try {

            profileModel = ProfileModel.getInstance();
            countryModel = new CountryModel();
            geographyModel = new GeographyModel();
            projectTeamsModel = new ProjectTeamsModel();
            setupTableView();
            loadProfiles();


            idToNameMap = createIdToNameMap();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }



    private void setupTableView() {
        setupCellValueFactories();
        setupButtonsInTable();
    }

    private void loadProfiles() {
        tblProfiles.setItems(profileModel.getAllProfiles());

    }

    private void setupCellValueFactories() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        colNameProfile.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colAnnualSalaryProfile.setCellValueFactory(cellData -> new SimpleStringProperty(currencyFormat.format(cellData.getValue().getAnnualSalary())));
        colHourlyRateProfile.setCellValueFactory(cellData -> new SimpleStringProperty(currencyFormat.format(cellData.getValue().getHourlySalary())));
        colDailyRateProfile.setCellValueFactory(cellData -> new SimpleStringProperty(currencyFormat.format(cellData.getValue().getDailyRate())));

        profileModel.getAllProfiles().addListener((ListChangeListener<? super Profile>) c -> {
            tblProfiles.refresh();
        });
    }

    private void setupButtonsInTable() {
        colUpdateIconProfile.setCellFactory(param -> new TableCell<Profile, Void>() {
            private final Button updateButton = createImageButton("/pictures/editLogo.png");

            {
                updateButton.setOnAction(event -> {
                    Profile profile = getTableView().getItems().get(getIndex());
                    openUpdateProfileView(profile);
                    tblProfiles.refresh();
                });

            }

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




        colDeleteIconProfile.setCellFactory(param -> new TableCell<Profile, Void>() {
            private final Button deleteButton = createImageButton("/pictures/TrashLogo.png");

            {
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

        // Adjust image size
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);

        Button button = new Button("", imageView);
        button.setStyle("-fx-background-color: transparent;");
        button.setAlignment(Pos.CENTER);

        return button;
    }


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




    private void deleteProfile(Profile profile) {
        Optional<ButtonType> result = AlertBox.displayConfirmation("Confirm Deletion", "Are you sure you want to delete the profile?\nProfile: " + profile.getFullName());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = profileModel.deleteProfile(profile);
            if (success) {
                tblProfiles.getItems().remove(profile);
                AlertBox.displayInfo("Profile Deleted", "Profile has been successfully deleted.");
            } else {
                AlertBox.displayInfo("Deletion Failed", "Failed to delete the profile.");
            }
        }
    }


    private void openUpdateProfileView(Profile profile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/updateProfileView.fxml"));
            Parent root = loader.load();

            // Get the controller of the update profile view
            UpdateProfileController controller = loader.getController();
            // Pass the selected profile to the controller
            controller.setProfile(profile);// Set a callback to refresh the table view when the update is done
            controller.setOnProfileUpdated(() -> {
                // Reload profiles and refresh the table view
                loadProfiles();
                tblProfiles.refresh();
            });
            Stage stage = new Stage();
            stage.setTitle("Update Profile");
            stage.setScene(new Scene(root));
            stage.initOwner(tblProfiles.getScene().getWindow());
            stage.setUserData(this);
            stage.show();
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }



}
