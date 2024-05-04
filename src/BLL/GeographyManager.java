package BLL;

import BE.Geography;
import DAL.Geography_DAO;

import java.util.List;

public class GeographyManager {

    private Geography_DAO geographyDAO;

    public GeographyManager() {
        this.geographyDAO = geographyDAO;
    }

    public List<Geography> getAllGeographies() throws Exception {
        return geographyDAO.getAllGeographies();
    }

}
