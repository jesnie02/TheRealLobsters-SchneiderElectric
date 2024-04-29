package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ProfileController {


    // Instance of FrameController to control the main frame of the application
    private FrameController frameController;

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
}
