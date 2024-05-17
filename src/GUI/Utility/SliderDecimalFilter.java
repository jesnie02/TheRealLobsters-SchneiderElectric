package GUI.Utility;

import javafx.scene.control.TextFormatter.Change;

import java.util.function.UnaryOperator;


public class SliderDecimalFilter implements UnaryOperator<Change> {
    @Override
    public Change apply(Change change) {
        String newText = change.getControlNewText();

        if (newText.isEmpty()) {
            return change;
        }

        if (!newText.matches("-?\\d*(\\.\\d{0," + 1 + "})?")) {
            return null;
        }

        try {
            double value = Double.parseDouble(newText);
            if (value < 0 || value > 100.00) {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return change;
    }
}