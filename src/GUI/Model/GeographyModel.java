package GUI.Model;

import BE.Country;
import BE.Geography;
import BLL.GeographyManager;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Map;


public class GeographyModel {

    private GeographyManager geographyManager;

    public GeographyModel() throws Exception {
        geographyManager = new GeographyManager();
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

    public ObservableList<Geography> getAllGeographiesGeographyOverview() throws Exception {
        ObservableList<Geography> geographies = javafx.collections.FXCollections.observableArrayList(
                geographyManager.getAllGeographiesGeographyOverview().stream()
                        .sorted(Comparator.comparing(Geography::getGeographyName))
                        .collect(Collectors.toList())
        );
        return geographies;
    }

    public Map<Integer, Geography> getGeographyMap() throws Exception {
        return geographyManager.getAllGeographies().stream()
                .collect(Collectors.toMap(Geography::getGeographyId, geography -> geography));
    }

}