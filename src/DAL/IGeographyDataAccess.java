package DAL;

import BE.Geography;

import java.sql.SQLException;
import java.util.List;

public interface IGeographyDataAccess {


    List<Geography> getAllGeographies(int countryId) throws Exception;

    List<Geography> getSumsAndAveragesForGeographies() throws SQLException;

    List<Geography> getAllGeographie();

    List<Geography> getAllGeographiesGeographyOverview() throws Exception;

    //List<Geography> getAllGeographies(int countryId) throws Exception;

    //List<Geography> getSumsAndAveragesForGeographies() throws SQLException;

    //List<Geography> getCountryGeographyList(int countryId);

    //void createGeography(String geographyName, List<Integer> countryIds) throws Exception;

}
