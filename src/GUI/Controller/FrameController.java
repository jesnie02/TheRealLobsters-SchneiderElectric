package GUI.Controller;

import GUI.Utility.ExceptionHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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

    private final UUID uuid = UUID.randomUUID();

    // A map to store the views that have been loaded.
    private Map<String, Node> viewCache = new HashMap<>();

    // A stack to store the history of the pages that have been visited.
    private Stack<Node> pageHistory = new Stack<>();

    private final String BASE_PATH = "/fxml/";

    public FrameController() {
        instance = this;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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



    // This method loads the view with the given name.
    private void loadView(String viewName) {

        Node view = viewCache.get(viewName);

        if (view == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE_PATH + viewName));
                view = loader.load();

                if (viewName.equals("updateProjectTeamView.fxml")){
                    updateProjectTeamController = loader.getController();
                }
                viewCache.put(viewName, view);

            } catch (IOException e) {
                ExceptionHandler.handleException(e);
                return;
            }
        }
        if (stackPaneFrame != null) {
            stackPaneFrame.getChildren().setAll(view);
        } else {
            showErrorAlert("Initialization Error", "UI components are not fully initialized.");
        }
    }

    public UpdateProjectTeamController getUpdateProjectTeamController() {
        if (updateProjectTeamController == null) {
            loadView("updateProjectTeamView.fxml");
        }
        return updateProjectTeamController;
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
    public void loadUpdateProjectTeamView() {
        loadView("updateProjectTeamView.fxml");
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
