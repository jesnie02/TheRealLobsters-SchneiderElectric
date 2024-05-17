package DAL;

import BE.Country;
import CustomExceptions.ApplicationWideException;
import DAL.DBConnector.DBConnector;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Country_DAO implements ICountryDataAccess {

    private final DBConnector dbConnector;

    public Country_DAO() throws ApplicationWideException {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new ApplicationWideException("Failed to initialize the database connector",e);
        }

    }

    /**
     * Gets all countries from the database.
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Country> getAllCountries() throws Exception {

        List<Country> allCountries = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM Country";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                Country country = new Country(
                        rs.getInt("CountryId"),
                        rs.getString("CountryName")
                );
                allCountries.add(country);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allCountries;
    }

    public List<Country> getSumsAndAveragesForCountries() {
        List<Country> allCountries = new ArrayList<>();
        String sql = """
        SELECT
                c.CountryId,
                c.CountryName,
                g.GeographyId,
                g.GeographyName,
                SUM(p.HourlySalary) AS TotalHourlyRate,
                AVG(p.HourlySalary) AS AvgHourlyRate,
                SUM(p.DailyRate) AS TotalDailyRate,
                AVG(p.DailyRate) AS AvgDailyRate,
                COUNT(p.ProfileId) AS ProfileCount
            FROM
                Geography g
            JOIN
                GeographyCountry gc ON g.GeographyId = gc.GeographyId
            JOIN
                Country c ON gc.CountryId = c.CountryId
            JOIN
                GeographyProfile gp ON g.GeographyId = gp.GeographyId
            JOIN
                Profile p ON gp.ProfileId = p.ProfileId
            GROUP BY
                c.CountryId, c.CountryName, g.GeographyId, g.GeographyName;
            """;
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                Country country = new Country(
                        rs.getInt("CountryId"),
                        rs.getString("CountryName"),
                        rs.getDouble("TotalHourlyRate"),
                        rs.getDouble("AvgHourlyRate"),
                        rs.getDouble("TotalDailyRate"),
                        rs.getDouble("AvgDailyRate"),
                        rs.getInt("ProfileCount")
                );
                allCountries.add(country);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allCountries;
    }
}