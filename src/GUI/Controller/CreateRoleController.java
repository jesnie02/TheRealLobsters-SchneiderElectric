package GUI.Controller;

import GUI.Model.ProfileRoleModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateRoleController implements Initializable {

    @FXML
    private TextField txtProfileRole;

    private ProfileRoleModel profileRoleModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profileRoleModel = new ProfileRoleModel();
    }

    @FXML
    private void createRole(ActionEvent actionEvent) {
        String role = txtProfileRole.getText().trim();

        if (role.isEmpty()) {
            displayInfo("Role cannot be empty");
            return;
        }
        profileRoleModel.createRole(role);
        displayInfo("Role saved successfully");
        closeWindow(actionEvent);
    }


    private void displayInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void closeWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
