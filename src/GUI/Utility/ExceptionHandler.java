package GUI.Utility;

import CustomExceptions.ApplicationWideException;

public class ExceptionHandler {
    public static void handleException(Exception e) {
        if (e instanceof ApplicationWideException) {
            AlertBox.displayError((ApplicationWideException) e);
        }else {
            e.printStackTrace();
        }
    }
}
