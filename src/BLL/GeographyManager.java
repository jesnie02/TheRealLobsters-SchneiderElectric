package BLL;

import BE.Country;
import BE.Geography;
import CustomExceptions.ApplicationWideException;
import DAL.Geography_DAO;
import DAL.IGeographyDataAccess;

import java.util.Arrays;
import java.util.List;

public class GeographyManager {

    private final IGeographyDataAccess geographyDAO;

    public GeographyManager() throws ApplicationWideException {
        geographyDAO = new Geography_DAO();
    }

    public List <Geography> getSumsAndAveragesForGeographies() throws Exception{
        return geographyDAO.getSumsAndAveragesForGeographies();
    }

    public List<Geography> getAllGeographiesGeographyOverview() throws Exception {
        return geographyDAO.getAllGeographiesGeographyOverview();
    }

    public List<Geography> getAllGeographies() throws Exception {
        return geographyDAO.getAllGeographie();
    }

    public void saveGeography(Geography geography) {
        geographyDAO.saveGeography(geography);
    }

    /*
    public List<Geography> getRegionsByCountryId(int countryId) throws Exception {
        return geographyDAO.getCountryGeographyList(countryId);
    }

     */

}
