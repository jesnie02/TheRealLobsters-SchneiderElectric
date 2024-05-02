package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TeamsController {
    // Instance of FrameController to control the main frame of the application
    private FrameController frameController;



    /**
     * Constructor for the TeamsController class.
     * It initializes the frameController variable with the instance of FrameController.
     */
    public TeamsController() {
        this.frameController = FrameController.getInstance();
    }




    @FXML
    private void openProjectTeamView(ActionEvent actionEvent) {
        frameController.loadCreateTeamView();
    }
}
