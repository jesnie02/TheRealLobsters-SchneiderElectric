package GUI.Model;

import BE.Country;
import BE.Geography;
import BLL.GeographyManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Map;


public class GeographyModel {

    private final GeographyManager geographyManager;

    private final ObservableList<Geography> geographies;

    public GeographyModel() throws Exception {
        geographyManager = new GeographyManager();
        geographies = FXCollections.observableArrayList();
        loadInitialGeographies();
    }

    /*
    public List<Geography> getRegionsByCountryId(int countryId) throws Exception {
        return geographyManager.getRegionsByCountryId(countryId);
    }

     */



    public ObservableList<Geography> getSumsAndAveragesForGeographies() throws Exception {
        ObservableList<Geography> geographies = javafx.collections.FXCollections.observableArrayList(
                geographyManager.getSumsAndAveragesForGeographies().stream()
                        .sorted(Comparator.comparing(Geography::getGeographyName))
                        .collect(Collectors.toList())
        );
        return geographies;
    }


    private void loadInitialGeographies() throws Exception {
        List<Geography> geographyList = geographyManager.getAllGeographiesGeographyOverview();
        geographies.setAll(geographyList.stream()
                .sorted(Comparator.comparing(Geography::getGeographyName))
                .collect(Collectors.toList()));
    }

    public ObservableList<Geography> getAllGeographiesGeographyOverview() {
        return geographies;
    }

    public Map<Integer, Geography> getGeographyMap() throws Exception {
        return geographyManager.getAllGeographies().stream()
                .collect(Collectors.toMap(Geography::getGeographyId, geography -> geography));
    }

    public void saveGeography(Geography geography) {
        geographyManager.saveGeography(geography);
        geographies.add(geography);

    }
}