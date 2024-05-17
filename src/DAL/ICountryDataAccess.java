package DAL;

import BE.Country;
import CustomExceptions.ApplicationWideException;

import java.util.List;

public interface ICountryDataAccess {
    List<Country> getAllCountries() throws ApplicationWideException;
}
