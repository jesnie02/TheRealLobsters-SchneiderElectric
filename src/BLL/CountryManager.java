package BLL;

import BE.Country;
import CustomExceptions.ApplicationWideException;

import java.io.IOException;
import java.util.List;

public class CountryManager {

    private final DAL.Country_DAO countryDAO;

    public CountryManager() throws ApplicationWideException {
        countryDAO = new DAL.Country_DAO();
    }

    /**
     * Gets all countries from the database.
     *
     * @return A list of all countries.
     * @throws Exception
     */
    public List<Country> getAllCountries() throws ApplicationWideException {
        return countryDAO.getAllCountries();
    }

    public List <Country> getSumsAndAveragesForCountries() throws ApplicationWideException{
        return countryDAO.getSumsAndAveragesForCountries();
    }

}
