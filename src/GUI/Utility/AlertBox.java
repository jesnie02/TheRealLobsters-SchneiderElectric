package GUI.Utility;

import CustomExceptions.ApplicationWideException;
import javafx.scene.control.Alert;

public class AlertBox {

        public static void displayError(ApplicationWideException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Something went wrong");
            alert.setHeaderText(ex.getMessage());
            alert.showAndWait();

            // optionally print stack stace
            ex.printStackTrace();
        }
    }

