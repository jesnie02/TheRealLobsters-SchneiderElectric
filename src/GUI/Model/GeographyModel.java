package GUI.Model;

import BE.Country;
import BE.Geography;
import BLL.GeographyManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;


public class GeographyModel {

    private GeographyManager geographyManager;

    public GeographyModel() throws Exception {
        geographyManager = new GeographyManager();
    }

    public ObservableList<Geography> getAllGeographies() throws Exception {
        ObservableList<Geography> geographies = javafx.collections.FXCollections.observableArrayList(
                geographyManager.getAllGeographies().stream()
                        .sorted(Comparator.comparing(Geography::getGeographyName))
                        .map(geography -> new Geography(geography.getGeographyId(), geography.getGeographyName()) {
                        })
                        .collect(Collectors.toList())
        );
        return geographies;
    }

    public ObservableList<Geography> getAllFromGeographies() throws Exception {
        ObservableList<Geography> geographies = javafx.collections.FXCollections.observableArrayList(
                geographyManager.getAllGeographies().stream()
                        .sorted(Comparator.comparing(Geography::getGeographyName))
                        .collect(Collectors.toList())
        );
        return geographies;
    }

    public Map<Integer, Geography> getGeographyMap() throws Exception {
        return geographyManager.getAllGeographies().stream()
                .collect(Collectors.toMap(Geography::getGeographyId, geography -> geography));
    }

    public ObservableList<Geography> getSumsAndAveragesForGeographies() throws Exception {
        ObservableList<Geography> geographies = javafx.collections.FXCollections.observableArrayList(
                geographyManager.getSumsAndAveragesForGeographies().stream()
                        .sorted(Comparator.comparing(Geography::getGeographyName))
                        .collect(Collectors.toList())
        );
        return geographies;
    }


}