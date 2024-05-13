package GUI.Model;

import BE.Geography;
import BLL.GeographyManager;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

}