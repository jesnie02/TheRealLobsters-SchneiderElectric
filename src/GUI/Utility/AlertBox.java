package GUI.Utility;

import CustomExceptions.ApplicationWideException;
import javafx.scene.control.Alert;

public class AlertBox {

    public static void displayError(ApplicationWideException ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(ex.getMessage());
        alert.showAndWait();

        ex.printStackTrace();
    }

    public static void displayInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
