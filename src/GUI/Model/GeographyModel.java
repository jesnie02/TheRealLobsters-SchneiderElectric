package GUI.Model;

import BE.Geography;
import BLL.GeographyManager;
import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Map;


public class GeographyModel {

    private final GeographyManager geographyManager;

    private final ObservableList<Geography> geographies;

    public GeographyModel() throws ApplicationWideException {
        geographyManager = new GeographyManager();
        geographies = FXCollections.observableArrayList(geographyManager.getAllGeographiesGeographyOverview());
        loadInitialGeographies();


    }

    public ObservableList<Geography> getSumsAndAveragesForGeographies()  {
        ObservableList<Geography> geographies = null;
        try {
            geographies = FXCollections.observableArrayList(
                    geographyManager.getSumsAndAveragesForGeographies().stream()
                            .sorted(Comparator.comparing(Geography::getGeographyName))
                            .collect(Collectors.toList())
            );
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return geographies;
    }

    private void loadInitialGeographies() {
        geographies.setAll(this.geographies.stream()
                .sorted(Comparator.comparing(Geography::getGeographyName))
                .collect(Collectors.toList()));
    }

    public ObservableList<Geography> getAllGeographiesGeographyOverview() throws ApplicationWideException {
        return geographyManager.getAllGeographiesGeographyOverview().stream()
                .sorted(Comparator.comparing(Geography::getGeographyName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public Map<Integer, Geography> getGeographyMap() throws ApplicationWideException {
        return geographyManager.getAllGeographies().stream()
                .collect(Collectors.toMap(Geography::getGeographyId, geography -> geography));
    }

    public ObservableList<Geography> getGeographies() {
        return geographies;
    }

    public void saveGeography(Geography geography)  {
        try {
            geographyManager.saveGeography(geography);
            geographies.add(geography);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }


    }

    public boolean deleteGeography(Geography geography) {
        try {
            geographyManager.deleteGeography(geography);
            return geographies.remove(geography);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
            return false;
        }
    }

    public void updateGeography(Geography geography) {
        try {
            geographyManager.updateGeography(geography);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        // Update the local list if necessary
        int index = geographies.indexOf(geography);
        if (index >= 0) {
            geographies.set(index, geography);
        }
    }
}