package DAL;

import BE.Geography;

import java.util.List;

public interface IGeographyDataAccess {

    List<Geography> getAllGeographies(int countryId) throws Exception;

    List<Geography> getSumsAndAveragesForGeographies();

    List<Geography> getCountryGeographyList(int countryId);
}
