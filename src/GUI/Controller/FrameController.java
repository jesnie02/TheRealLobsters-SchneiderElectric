package GUI.Controller;


import GUI.Utility.ExceptionHandler;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class FrameController implements Initializable {

   public enum ViewType {
        DASHBOARD, PROFILES, TEAMS, GEOGRAPHY, MULTIPLIER, CURRENCY
   }

    private ObjectProperty<ViewType> currentView = new SimpleObjectProperty<>(ViewType.DASHBOARD);

    private static FrameController instance;
    private UpdateProjectTeamController updateProjectTeamController;
    private BooleanProperty areChangesMade = new SimpleBooleanProperty(false);

    @FXML
    private StackPane stackPaneFrame;

    private final UUID id = UUID.randomUUID();

    // A map to store the views that have been loaded.
    private Map<String, Node> viewCache = new HashMap<>();


    // A stack to store the history of the pages that have been visited.
    private Stack<Node> pageHistory = new Stack<>();

    private static final PseudoClass ACTIVPSEOUDOCLASS = PseudoClass.getPseudoClass("active");

    private final String BASE_PATH = "/fxml/";
    @FXML
    private Button btnDashboard, btnProfiles, btnTeams, btnGeography ,btnMultiplier ,btnCurrency;



    public FrameController() {
        instance = this;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnDashboard.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, true);
        currentView.addListener((ob, ov, nv) -> {
            if (areChangesMade.get()) {
                // Ask user to confirm view change if changes are made
                boolean proceed = showConfirmationDialog();
                if (!proceed) {
                    currentView.set(ov); // Revert to the old view
                    return;
                }
                areChangesMade.set(false); // Reset changes flag
            }
            btnDashboard.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, nv == ViewType.DASHBOARD);
            btnProfiles.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, nv == ViewType.PROFILES);
            btnTeams.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, nv == ViewType.TEAMS);
            btnGeography.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, nv == ViewType.GEOGRAPHY);
            btnMultiplier.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, nv == ViewType.MULTIPLIER);
            btnCurrency.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, nv == ViewType.CURRENCY);

            // Re-enable buttons when switching away from UpdateProjectTeamController view
            if (!nv.equals(ViewType.TEAMS)) {
                areChangesMade.set(false);
            }
        });
        loadDashboardOnStart();
        setupBindings();
    }

    private void setCurrentView(ViewType viewType) {
        btnDashboard.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, viewType == ViewType.DASHBOARD);
        btnProfiles.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, viewType == ViewType.PROFILES);
        btnTeams.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, viewType == ViewType.TEAMS);
        btnGeography.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, viewType == ViewType.GEOGRAPHY);
        btnMultiplier.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, viewType == ViewType.MULTIPLIER);
        btnCurrency.pseudoClassStateChanged(ACTIVPSEOUDOCLASS, viewType == ViewType.CURRENCY);
        loadView(viewType.toString().toLowerCase() + "View.fxml");
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
        currentView.set(ViewType.DASHBOARD);
        loadView("DashboardView.fxml");

    }

    @FXML
    private void openProfiles(ActionEvent actionEvent) {
        currentView.set(ViewType.PROFILES);
        loadView("ProfileView.fxml");

    }

    @FXML
    private void openTeams(ActionEvent actionEvent) {
        currentView.set(ViewType.TEAMS);
        loadView("TeamsView.fxml");

    }

    @FXML
    private void openGeography(ActionEvent actionEvent) {
        currentView.set(ViewType.GEOGRAPHY);
        loadView("geographyView.fxml");
    }

    @FXML
    private void openMultiplier(ActionEvent actionEvent) {
        currentView.set(ViewType.MULTIPLIER);
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
        currentView.set(ViewType.CURRENCY);
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

    public void setAreChangesMade(boolean changesAreMade) {
        this.areChangesMade.set(changesAreMade);
    }

    private void setupBindings() {
        btnDashboard.disableProperty().bind(areChangesMade);
        btnProfiles.disableProperty().bind(areChangesMade);
        btnTeams.disableProperty().bind(areChangesMade);
        btnGeography.disableProperty().bind(areChangesMade);
        btnMultiplier.disableProperty().bind(areChangesMade);
        btnCurrency.disableProperty().bind(areChangesMade);
    }

    private boolean showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Navigation");
        alert.setHeaderText("Unsaved Changes");
        alert.setContentText("You have unsaved changes. Do you really want to leave this page?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
