package GUI.Controller.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewFactory {

    private Map<String, Node> cache = new HashMap<>();

    public Node getView(String fxmlFile) {
        return cache.computeIfAbsent(fxmlFile, this::loadView);
    }

    private Node loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load FXML view: " + fxmlFile, e);
        }
    }
}
