package BLL;

import BE.Countries;

import java.io.IOException;
import java.util.List;

public class CountryManager {

    private final DAL.Country_DAO countryDAO;

    public CountryManager() throws IOException {
        countryDAO = new DAL.Country_DAO();
    }

    public List<Countries> getAllCountries() throws Exception {
        return countryDAO.getAllCountries();
    }

}
