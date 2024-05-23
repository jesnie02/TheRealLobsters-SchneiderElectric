package DAL;

import BE.Country;
import BE.Geography;
import CustomExceptions.ApplicationWideException;

import java.util.List;

public interface ICountryDataAccess {
    List<Country> getAllCountries() throws ApplicationWideException;

    List<Country> getCountriesForGeographyOverview(int GeographyId) throws ApplicationWideException;
}
