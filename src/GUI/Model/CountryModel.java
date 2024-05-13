package GUI.Model;


import BE.Country;
import BLL.CountryManager;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class CountryModel {

    private final CountryManager countryManager;

    public CountryModel() throws IOException {
        countryManager = new BLL.CountryManager();
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
                        .map(country -> new Country(country.getCountryId(), country.getCountryName()) {
                            @Override
                            public String toString() {
                                return getCountryName();
                            }
                        })
                        .collect(Collectors.toList())
        );
        return countries;
    }

    public ObservableList<Country> getAllFromCountries() throws Exception {
        ObservableList<Country> countries = javafx.collections.FXCollections.observableArrayList(
                countryManager.getAllCountries().stream()
                        .sorted(Comparator.comparing(Country::getCountryName))
                        .collect(Collectors.toList())
        );
        return countries;
    }

    public Map<Integer, Country> getCountriesMap() throws Exception {
        return countryManager.getAllCountries().stream()
                .collect(Collectors.toMap(Country::getCountryId, country -> country));
    }
/*
    public ObservableList<Country> getSumsAndAveragesForCountries() throws Exception {
        ObservableList<Country> countries = javafx.collections.FXCollections.observableArrayList(
                countryManager.getSumsAndAveragesForCountries().stream()
                        .sorted(Comparator.comparing(Country::getCountryName))
                        .collect(Collectors.toList())
        );
        return countries;
    }

 */
}