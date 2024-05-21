package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.Profile;
import GUI.Model.GeographyModel;
import GUI.Utility.ExceptionHandler;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GeographyController  {
    public Button btnCreateGeography;
    public MFXLegacyTableView<Geography> tblGeographyOverview;
    public MFXLegacyTableView<Country> tblGeographyOverviewCountry;
    public TableColumn<Geography, String> colGeoOverviewName, colGeoOverviewTeamNumber, colGeoOverviewEmployeeNumber;
    public TableColumn colGeoOverviewEdit;
    public TableColumn colGeoOverviewDelete;
    public TableColumn colGeoOverviewCountry;

    private GeographyModel geographyModel;

    @FXML
    private void initialize() throws Exception {
        geographyModel = new GeographyModel();
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

       colGeoOverviewEdit.setCellFactory(param -> new TableCell<Profile, Void>() {
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

       colGeoOverviewDelete.setCellFactory(param -> new TableCell<Profile,Void>() {
           private final Button deleteButton = createImageButton("/pictures/TrashLogo.png");

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

    public void loadGeographies()  {
        ObservableList<Geography> geographies = geographyModel.getAllGeographiesGeographyOverview();
        tblGeographyOverview.setItems(geographies);
        tblGeographyOverview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                loadCountriesForGeography(newSelection);
            }
        });
    }

    private void loadCountriesForGeography(Geography newSelection) {
        List<Country> countries = newSelection.getCountries();
        ObservableList<Country> countryObservableList = FXCollections.observableArrayList(countries);
        tblGeographyOverviewCountry.setItems(countryObservableList);
    }

    public void createNewGeography(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createGeographyView.fxml"));
            loader.setControllerFactory(param -> new CreateGeographyController(geographyModel));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Create geography");
            stage.show();
        }
        catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }
}