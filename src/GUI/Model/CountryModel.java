package GUI.Model;

import BLL.CountryManager;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.stream.Collectors;

public class CountryModel {

    private final CountryManager countryManager;

    public CountryModel() throws IOException {
        countryManager = new BLL.CountryManager();
    }

    public ObservableList<String> getAllCountries() throws Exception {
        ObservableList<String> countryNames = javafx.collections.FXCollections.observableArrayList(
                countryManager.getAllCountries().stream()
                        .map(BE.Countries::getCountryName)
                        .collect(Collectors.toList())
        );
        System.out.println(countryNames); // Add this line
        return countryNames;
    }
}