package BLL;

import BE.Geography;
import DAL.Geography_DAO;
import DAL.IGeographyDataAccess;

import java.util.List;

public class GeographyManager {

    private final IGeographyDataAccess geographyDAO;

    public GeographyManager() throws Exception {
        geographyDAO = new Geography_DAO();
    }



    public List <Geography> getSumsAndAveragesForGeographies() throws Exception{
        return geographyDAO.getSumsAndAveragesForGeographies();
    }

    /*
    public List<Geography> getRegionsByCountryId(int countryId) throws Exception {
        return geographyDAO.getCountryGeographyList(countryId);
    }

     */
}
