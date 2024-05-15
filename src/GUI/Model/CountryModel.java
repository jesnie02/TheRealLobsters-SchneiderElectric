package GUI.Model;


import BE.Country;
import BE.Geography;
import BLL.CountryManager;
import BLL.GeographyManager;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class CountryModel {

    private final CountryManager countryManager;
    private final GeographyManager geographyManager;

    public CountryModel() throws Exception {
        countryManager = new BLL.CountryManager();
        geographyManager = new BLL.GeographyManager();
    }

    /**
     * Gets all countries from the database.
     *
     * @return A list of all countries.
     * @throws Exception
     */
    public ObservableList<Country> getAllCountries() throws Exception {
        ObservableList<Country> countries = javafx.collections.FXCollections.observableArrayList(
                countryManager.getAllCountries().stream()
                        .sorted(Comparator.comparing(Country::getCountryName))
                        .collect(Collectors.toList())
        );
        return countries;
    }

    public ObservableList<Geography> getAllFromGeographies() throws Exception {
        ObservableList<Geography> geographies = javafx.collections.FXCollections.observableArrayList(
                geographyManager.getAllGeographies().stream()
                        .sorted(Comparator.comparing(Geography::getGeographyName))
                        .collect(Collectors.toList())
        );
        return geographies;
    }

    public Map<Integer, Country> getCountriesMap() throws Exception {
        return countryManager.getAllCountries().stream()
                .collect(Collectors.toMap(Country::getCountryId, country -> country));
    }

    public ObservableList<Country> getSumsAndAveragesForCountries() throws Exception {
        ObservableList<Country> countries = javafx.collections.FXCollections.observableArrayList(
                countryManager.getSumsAndAveragesForCountries().stream()
                        .sorted(Comparator.comparing(Country::getCountryName))
                        .collect(Collectors.toList())
        );
        return countries;
    }

}