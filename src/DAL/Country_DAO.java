package DAL;

import BE.Country;
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

    public Country_DAO() throws IOException {
        dbConnector = new DBConnector();
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
                    SUM(p.AnualSalary) AS TotalHourlyRate,
                    AVG(p.AnualSalary) AS AvgHourlyRate,
                    SUM(p.DailyRate) AS TotalDailyRate,
                    AVG(p.DailyRate) AS AvgDailyRate,
                    COUNT(p.ProfileId) AS ProfileCount
                FROM
                    Country c
                JOIN
                    CountryProfile cp ON c.CountryId = cp.CountryId
                JOIN
                    Profile p ON cp.ProfileId = p.ProfileId
                GROUP BY
                    c.CountryId, c.CountryName;
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