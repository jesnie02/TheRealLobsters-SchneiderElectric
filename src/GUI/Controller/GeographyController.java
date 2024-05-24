package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.Profile;
import CustomExceptions.ApplicationWideException;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Utility.AlertBox;
import GUI.Utility.ExceptionHandler;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class GeographyController  {
    public Button btnCreateGeography;
    public MFXLegacyTableView<Geography> tblGeographyOverview;
    public MFXLegacyTableView<Country> tblGeographyOverviewCountry;
    public TableColumn<Geography, String> colGeoOverviewName, colGeoOverviewTeamNumber, colGeoOverviewEmployeeNumber;
    public TableColumn<Geography, Void> colGeoOverviewEdit;
    public TableColumn<Geography,Void> colGeoOverviewDelete;
    public TableColumn<Geography, String> colGeoOverviewCountry;

    private GeographyModel geographyModel;
    private CountryModel countryModel;

    @FXML
    private void initialize() throws Exception {
        geographyModel = new GeographyModel();
        countryModel = new CountryModel();
        loadGeographies();
        initializeTables();
    }

    private void initializeTables() {
        colGeoOverviewName.setCellValueFactory(new PropertyValueFactory<>("geographyName"));
        colGeoOverviewCountry.setCellValueFactory(new PropertyValueFactory<>("countryName"));

        colGeoOverviewEmployeeNumber.setCellValueFactory((TableColumn.CellDataFeatures<Geography, String> cellData) -> {
            int employeeCount = cellData.getValue().getProfileCount();
            return new SimpleStringProperty(String.valueOf(employeeCount));
        });

        colGeoOverviewTeamNumber.setCellValueFactory((TableColumn.CellDataFeatures<Geography, String> cellData) -> {
            int teamCount = cellData.getValue().getTeamCount();
            return new SimpleStringProperty(String.valueOf(teamCount));
        });

        colGeoOverviewEdit.setCellFactory(param -> new TableCell<Geography, Void>() {
            private final Button updateButton = createImageButton("/pictures/editLogo.png");

            {
                updateButton.setOnAction(event ->{
                    Geography geography = getTableView().getItems().get(getIndex());
                    openUpdateGeographyView(geography);
                    tblGeographyOverview.refresh();

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

        colGeoOverviewDelete.setCellFactory(param -> new TableCell<Geography,Void>() {
            private final Button deleteButton = createImageButton("/pictures/TrashLogo.png");

            {
                deleteButton.setOnAction(event -> {
                    Geography geography = getTableView().getItems().get(getIndex());
                    deleteGeography(geography);
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
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);

        Button button = new Button("", imageView);
        button.setStyle("-fx-background-color: transparent;");
        button.setAlignment(Pos.CENTER);

        return button;
    }

    public void loadGeographies() throws ApplicationWideException {
        ObservableList<Geography> geographies = geographyModel.getAllGeographiesGeographyOverview();
        tblGeographyOverview.setItems(geographies);
        tblGeographyOverview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ObservableList<Country> countries = countryModel.getCountriesForGeographyOverview(newSelection.getGeographyId());
                tblGeographyOverviewCountry.setItems(countries);
            }
        });
    }

    private void deleteGeography(Geography geography) {
        Optional<ButtonType> result = AlertBox.displayConfirmation("Confirm Deletion", "Are you sure you want to delete the geography?\nGeography: " + geography.getGeographyName());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = geographyModel.deleteGeography(geography);
            if (success) {
                tblGeographyOverview.getItems().remove(geography);
                AlertBox.displayInfo("Geography Deleted", "Geography has been successfully deleted.");
            } else {
                AlertBox.displayInfo("Deletion Failed", "Failed to delete the geography.");
            }
        }
    }

    public void createNewGeography(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createGeographyView.fxml"));
            loader.setControllerFactory(param -> new CreateGeographyController(geographyModel));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Create geography");
            stage.show();
        }
        catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void openUpdateGeographyView(Geography geography) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/updateGeographyView.fxml"));
            Parent root = loader.load();
            UpdateGeographyController controller = loader.getController();
            controller.setGeography(geography);// Set a callback to refresh the table view when the update is done
            controller.setOnGeographyUpdated(() -> {
                // Reload profiles and refresh the table view
                try {
                    loadGeographies();
                } catch (ApplicationWideException e) {
                    throw new RuntimeException(e);
                }
                tblGeographyOverview.refresh();
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Geography");
            stage.show();
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }
}