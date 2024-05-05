package BLL;

import BE.Country;

import java.io.IOException;
import java.util.List;

public class CountryManager {

    private final DAL.Country_DAO countryDAO;

    public CountryManager() throws IOException {
        countryDAO = new DAL.Country_DAO();
    }

    public List<Country> getAllCountries() throws Exception {
        return countryDAO.getAllCountries();
    }

    public List <Country> getSumsAndAveragesForCountries() throws Exception{
        return countryDAO.getSumsAndAveragesForCountries();
    }

}
