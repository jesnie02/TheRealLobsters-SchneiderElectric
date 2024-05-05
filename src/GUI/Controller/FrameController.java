package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;


/**
 * This class is responsible for controlling the main frame of the application.
 * It implements the Initializable interface which means it provides a method to perform any necessary initialization after all the properties have been set.
 */
public class FrameController implements Initializable {
    private static FrameController instance;

    @FXML
    private StackPane stackPaneFrame;

    private Stack<Node> pageHistory = new Stack<>();
    private final String BASE_PATH = "/fxml/";



    /**
     * Constructor for the FrameController class.
     * It sets the instance variable to this object.
     */
   public FrameController() {
        instance = this;
    }

    /**
     * This method is called after all the properties have been set.
     * It loads the dashboard at the start of the application.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDashboardOnStart();
    }


    /**
     * This method loads the dashboard at the start of the application.
     * If the stackPaneFrame is null, it shows an error alert.
     */
    private void loadDashboardOnStart() {
        if (stackPaneFrame != null) {
            loadView("DashboardView.fxml");
        } else {
            showErrorAlert("Initialization Error", "UI components are not fully initialized.");
        }
    }


    /**
     * This method returns the instance of the FrameController.
     * If the instance is null, it creates a new instance.
     */
    public static synchronized FrameController getInstance() {
        if (instance == null) {
            instance = new FrameController();
        }
        return instance;
    }

    /**
     * This method checks if the stackPaneFrame is injected.
     * If not, it throws an IllegalStateException.
     */
    public void checkInitialization() {
        if (stackPaneFrame == null) {
            throw new IllegalStateException("StackPaneFrame is not injected: check your FXML file.");
        }
    }

    /**
     * This method loads the view with the given name.
     * It checks if the view is found and loads it.
     * If the view is not found, it shows an error alert.
     */
    private void loadView(String viewName) {
        checkInitialization();
        URL url = getClass().getResource(BASE_PATH + viewName);
        if (url == null) {
            showErrorAlert("Error Loading View", "The FXML file was not found: " + BASE_PATH + viewName);
            return;
        }
        try {
            Node node = FXMLLoader.load(url);
            transitionToNewScene(node);
        } catch (IOException e) {
            showErrorAlert("Error Loading View", "Failed to load the view: " + BASE_PATH + viewName + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method transitions to a new scene.
     * It sets the children of the stackPaneFrame to the given node.
     * It pushes the node to the pageHistory stack.
     */
    private void transitionToNewScene(Node node) {
        stackPaneFrame.getChildren().setAll(node);
        pageHistory.push(node);
    }


    /**
     * This method shows an error alert with the given header and content.
     */
    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * This method loads the previous view.
     * It pops the last node from the pageHistory stack and transitions to it.
     */
    @FXML
    private void openDashboard(ActionEvent actionEvent) {
        loadView("DashboardView.fxml");
    }


    /**
     * This method is called when the profiles button is clicked.
     * It loads the profile view.
     */
    @FXML
    private void openProfiles(ActionEvent actionEvent) {
        loadView("ProfileView.fxml");
    }

    /**
     * This method is called when the teams button is clicked.
     * It loads the teams view.
     */
    @FXML
    private void openTeams(ActionEvent actionEvent) {
        loadView("TeamsView.fxml");
    }

    /**
     * This method is called when the regions button is clicked.
     * It loads the regions view.
     */
    @FXML
    private void openGeography(ActionEvent actionEvent) {
        loadView("geographyView.fxml");
    }

    @FXML
    private void openMultiplier(ActionEvent actionEvent) {
        loadView("multipliersView.fxml");
    }

    /**
     * This method loads the create profile view.
     */
    public void loadCreateProfileView() {
        loadView("createProfileView.fxml");
    }

    /**
     * This method is called when the logout button is clicked.
     */
    @FXML
    private void buttonLogOut(ActionEvent actionEvent) {

    }


    void loadCreateTeamView() {
        loadView("createProjectTeamView.fxml");

    }

    void loadDetailView() {
        loadView("teamDetailsView.fxml");
    }
}
