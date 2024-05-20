package GUI.Model;


import BE.Country;
import BE.Geography;
import BLL.CountryManager;
import BLL.GeographyManager;
import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class CountryModel {

    private final CountryManager countryManager;
    private final GeographyManager geographyManager;

    public CountryModel() throws ApplicationWideException {
        countryManager = new BLL.CountryManager();
        geographyManager = new BLL.GeographyManager();
    }


    public ObservableList<Country> getAllCountries()  {
        ObservableList<Country> countries = null;
        try {
            countries = javafx.collections.FXCollections.observableArrayList(
                    countryManager.getAllCountries().stream()
                            .sorted(Comparator.comparing(Country::getCountryName))
                            .collect(Collectors.toList())
            );
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return countries;
    }

    public ObservableList<Geography> getAllFromGeographies() {
        ObservableList<Geography> geographies = null;
        try {
            geographies = FXCollections.observableArrayList(
                    geographyManager.getAllGeographies().stream()
                            .sorted(Comparator.comparing(Geography::getGeographyName))
                            .collect(Collectors.toList())
            );
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return geographies;
    }

    public Map<Integer, Country> getCountriesMap() throws ApplicationWideException{
        return countryManager.getAllCountries().stream()
                .collect(Collectors.toMap(Country::getCountryId, country -> country));
    }

    public ObservableList<Country> getSumsAndAveragesForCountries() {
        ObservableList<Country> countries = null;
        try {
            countries = javafx.collections.FXCollections.observableArrayList(
                    countryManager.getSumsAndAveragesForCountries().stream()
                            .sorted(Comparator.comparing(Country::getCountryName))
                            .collect(Collectors.toList())
            );
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return countries;
    }

}