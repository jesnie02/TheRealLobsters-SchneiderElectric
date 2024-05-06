package DAL;

import BE.Geography;

import java.util.List;

public interface IGeographyDataAccess {

    List<Geography> getAllGeographies() throws Exception;

    List<Geography> getSumsAndAveragesForGeographies();
}
