package BLL;

import BE.Country;
import BE.Geography;
import DAL.Geography_DAO;
import DAL.IGeographyDataAccess;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GeographyManager {

    private final IGeographyDataAccess geographyDAO;

    public GeographyManager() throws Exception {
        geographyDAO = new Geography_DAO();
    }

    public List<Geography> getAllGeographies() throws Exception {
        return geographyDAO.getAllGeographies();
    }

    public List <Geography> getSumsAndAveragesForGeographies() throws Exception{
        return geographyDAO.getSumsAndAveragesForGeographies();
    }

}
