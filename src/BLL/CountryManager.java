package BLL;

import BE.Country;
import CustomExceptions.ApplicationWideException;
import java.util.List;

public class CountryManager {

    private final DAL.Country_DAO countryDAO;

    public CountryManager() throws ApplicationWideException {
        countryDAO = new DAL.Country_DAO();
    }

    public List<Country> getAllCountries() throws ApplicationWideException {
        return countryDAO.getAllCountries();
    }

    public List<Country> getCountriesForGeographyOverview(int geographyId) throws ApplicationWideException {
        return countryDAO.getCountriesForGeographyOverview(geographyId);
    }

}
