package DAL;

import BE.Geography;
import CustomExceptions.ApplicationWideException;

import java.sql.SQLException;
import java.util.List;

public interface IGeographyDataAccess {



    List<Geography> getSumsAndAveragesForGeographies() throws ApplicationWideException;

    List<Geography> getAllGeographie() throws ApplicationWideException;

    List<Geography> getAllGeographiesGeographyOverview() throws ApplicationWideException;

    void saveGeography(Geography geography) throws ApplicationWideException;


}
