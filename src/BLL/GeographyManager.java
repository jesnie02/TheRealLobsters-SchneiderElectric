package BLL;

import BE.Geography;
import CustomExceptions.ApplicationWideException;
import DAL.Geography_DAO;
import DAL.IGeographyDataAccess;

import java.util.List;

public class GeographyManager {

    private final IGeographyDataAccess geographyDAO;

    public GeographyManager() throws ApplicationWideException {
        geographyDAO = new Geography_DAO();
    }

    public List <Geography> getSumsAndAveragesForGeographies() throws ApplicationWideException{
        return geographyDAO.getSumsAndAveragesForGeographies();
    }

    public List<Geography> getAllGeographiesGeographyOverview() throws ApplicationWideException {
        return geographyDAO.getAllGeographiesGeographyOverview();
    }

    public List<Geography> getAllGeographies() throws ApplicationWideException {
        return geographyDAO.getAllGeographie();
    }

    public void saveGeography(Geography geography) throws ApplicationWideException {
        geographyDAO.saveGeography(geography);
    }

    public void deleteGeography(Geography geography) throws ApplicationWideException {
        geographyDAO.deleteGeography(geography.getGeographyId());

    }

    public void updateGeography(Geography geography) throws ApplicationWideException {
        geographyDAO.updateGeography(geography);
    }

}
