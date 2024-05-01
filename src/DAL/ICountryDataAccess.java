package DAL;

import BE.Country;

import java.util.List;

public interface ICountryDataAccess {
    List<Country> getAllCountries() throws Exception;
}
