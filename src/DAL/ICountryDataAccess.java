package DAL;

import BE.Countries;

import java.util.List;

public interface ICountryDataAccess {
    List<Countries> getAllCountries() throws Exception;
}
