package GUI.Controller;


import GUI.Utility.ExceptionHandler;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


import java.io.IOException;
import java.net.URL;
import java.util.*;


public class FrameController implements Initializable {



    private static FrameController instance;
    private UpdateProjectTeamController updateProjectTeamController;

    @FXML
    private StackPane stackPaneFrame;

    private final UUID id = UUID.randomUUID();

    // A map to store the views that have been loaded.
    private Map<String, Node> viewCache = new HashMap<>();

    private Map<String, Button> buttonMap = new HashMap<>();

    // A stack to store the history of the pages that have been visited.
    private Stack<Node> pageHistory = new Stack<>();

    private final String BASE_PATH = "/fxml/";
    @FXML
    private Button btnDashboard, btnProfiles, btnTeams, btnGeography ,btnMultiplier ,btnCurrency;

    private final BooleanProperty dashboardSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty profilesSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty teamsSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty CurrencySelected = new SimpleBooleanProperty(false);
    private final BooleanProperty multiplierSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty geographySelected = new SimpleBooleanProperty(false);


    public FrameController() {
        instance = this;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindButtonStyles();
        Platform.runLater(this::addStylesheet);
        //initializeButtonMap();
        loadDashboardOnStart();
    }



    private void loadDashboardOnStart() {
        if (stackPaneFrame != null) {
            loadView("dashboardView.fxml");
        } else {
            showErrorAlert("Initialization Error", "UI components are not fully initialized.");
        }
    }


    // This method returns the instance of the FrameController class.
    public static synchronized FrameController getInstance() {

        if (instance == null) {
            instance = new FrameController();
        }
        return instance;
    }




    private void loadView(String viewName) {
        Node view = viewCache.computeIfAbsent(viewName, this::loadFXML);
        if (view != null) {
            stackPaneFrame.getChildren().setAll(view);
            //updateButtonSelection(viewName);
        }
    }

    private Node loadFXML(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE_PATH + viewName));
            return loader.load();
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
            return null;
        }
    }

    private void bindButtonStyles() {
        btnDashboard.getStyleClass().addAll("button");
        btnProfiles.getStyleClass().addAll("button");
        btnTeams.getStyleClass().addAll("button");
        btnGeography.getStyleClass().addAll("button");
        btnMultiplier.getStyleClass().addAll("button");
        btnCurrency.getStyleClass().addAll("button");

        dashboardSelected.addListener((obs, oldVal, newVal) -> updateStyleClass(btnDashboard, newVal));
        profilesSelected.addListener((obs, oldVal, newVal) -> updateStyleClass(btnProfiles, newVal));
        teamsSelected.addListener((obs, oldVal, newVal) -> updateStyleClass(btnTeams, newVal));
        geographySelected.addListener((obs, oldVal, newVal) -> updateStyleClass(btnGeography, newVal));
        CurrencySelected.addListener((obs, oldVal, newVal) -> updateStyleClass(btnCurrency, newVal));
        multiplierSelected.addListener((obs, oldVal, newVal) -> updateStyleClass(btnMultiplier, newVal));
    }





    private void updateStyleClass(Button button, boolean isSelected) {
        // Run updates on the JavaFX application thread to avoid concurrency issues
        Platform.runLater(() -> {
            if (isSelected) {
                if (!button.getStyleClass().contains("button-selected")) {
                    button.getStyleClass().add("button-selected");
                }
            } else {
                button.getStyleClass().remove("button-selected");
            }
        });
    }

    private void addStylesheet() {
        try {
            if (stackPaneFrame.getScene() != null) {
                // Adjust path if necessary
                URL stylesheetURL = getClass().getResource("/style/Style.css");
                if (stylesheetURL != null) {
                    stackPaneFrame.getScene().getStylesheets().add(stylesheetURL.toExternalForm());
                } else {
                    System.out.println("Stylesheet file not found.");
                }
            } else {
                System.out.println("Scene is still null.");
            }
        } catch (Exception e) {
            System.err.println("Error loading stylesheet: " + e.getMessage());
            ExceptionHandler.handleException(e);
        }
    }

/*
    private void initializeButtonMap() {
        buttonMap.put("DashboardView", btnDashboard);
        buttonMap.put("TeamsView", btnTeams);
        buttonMap.put("ProfileView", btnProfiles);
        buttonMap.put("GeographyView", btnGeography);
        buttonMap.put("multipliersView", btnMultiplier);
        buttonMap.put("currencyView", btnCurrency);
    }

    public void selectDashboardButton() {
        updateButtonSelection("DashboardView");
        System.out.println("Dashboard button selected"+id);

    }

    public void selectProfileButton() {
        updateButtonSelection("ProfileView");
        System.out.println("Profile button selected"+id);

    }

    public void selectTeamsButton() {
        updateButtonSelection("TeamsView");
        System.out.println("Teams button selected"+id);

    }

    void updateButtonSelection(String viewName) {
        clearButtonSelection();
        Button selectedButton = buttonMap.get(viewName);
        if (selectedButton != null) {
            selectedButton.getStyleClass().add("button-selected");

        }
    }

    private void clearButtonSelection() {
        buttonMap.values().forEach(button -> button.getStyleClass().remove("button-selected"));
    }

 */




    public UpdateProjectTeamController getUpdateProjectTeamController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/updateProjectTeamView.fxml"));
            Node updateNode = loader.load();
            UpdateProjectTeamController controller = loader.getController();
            setMainView(updateNode);
            return controller;
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
            return null;
        }
    }




    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void openDashboard(ActionEvent actionEvent) {
        loadView("DashboardView.fxml");

    }

    @FXML
    private void openProfiles(ActionEvent actionEvent) {
        loadView("ProfileView.fxml");

    }

    @FXML
    private void openTeams(ActionEvent actionEvent) {
        loadView("TeamsView.fxml");

    }

    @FXML
    private void openGeography(ActionEvent actionEvent) {
        loadView("geographyView.fxml");
    }

    @FXML
    private void openMultiplier(ActionEvent actionEvent) {
        loadView("multipliersView.fxml");
    }

    @FXML
    public void loadCreateProfileView() {
        loadView("createProfileView.fxml");
    }

    @FXML
    public void loadCreateTeamView() {
        loadView("createProjectTeamView.fxml");
    }

    @FXML
    public void loadTeamsView() {
        loadView("TeamsView.fxml");
    }

    @FXML
    public void loadUpdateProjectTeamView() throws IOException {
       loadView("updateProjectTeamView.fxml");
    }

    @FXML
    public void loadProfileView() {
        loadView("ProfileView.fxml");
    }

    @FXML
    private void openCurrency(ActionEvent actionEvent) {
        loadView("currencyView.fxml");
    }

    // This method is called when the shutdown button is clicked.
    @FXML
    private void buttonLogOut(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void setMainView(Node node) {
        if (stackPaneFrame != null){
            stackPaneFrame.getChildren().setAll(node);
        } else {
            showErrorAlert("Initialization Error", "UI components are not fully initialized.");
        }
    }
}
