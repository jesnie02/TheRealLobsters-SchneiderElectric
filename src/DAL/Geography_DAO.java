package DAL;

import BE.Geography;
import DAL.DBConnector.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Geography_DAO implements IGeographyDataAccess {

    private final DBConnector dbConnector;

    public Geography_DAO() throws Exception{
        dbConnector = new DBConnector();
    }


    @Override
    public List<Geography> getAllGeographies(int countryId) throws Exception {
        List<Geography> allGeographies = new ArrayList<>();
        String sql = """
            SELECT g.*
            FROM Geography g
            JOIN GeographyCountry gc ON g.GeographyId = gc.GeographyId
            WHERE gc.CountryId = ?;
            """;
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, countryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Geography geography = new Geography(
                            rs.getInt("GeographyId"),
                            rs.getString("GeographyName")
                    );
                    allGeographies.add(geography);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allGeographies;
    }





    @Override
    public List<Geography> getSumsAndAveragesForGeographies() throws SQLException {
        List<Geography> allGeographies = new ArrayList<>();
        String sql = """
            
                SELECT
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
                     g.GeographyId, g.GeographyName;
            """;
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                Geography geography = new Geography(
                        rs.getInt("GeographyId"),
                        rs.getString("GeographyName"),
                        rs.getDouble("TotalHourlyRate"),
                        rs.getDouble("AvgHourlyRate"),
                        rs.getDouble("TotalDailyRate"),
                        rs.getDouble("AvgDailyRate"),
                        rs.getInt("ProfileCount")
                );
                allGeographies.add(geography);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return allGeographies;
    }

    /*
    public List<Geography> getCountryGeographyList(int countryId) {
        List<Geography> countryGeographyList = new ArrayList<>();
        String sql = """
                SELECT g.GeographyName
                FROM Country c
                JOIN GeographyCountry gc ON c.CountryId = gc.CountryId
                JOIN Geography g ON gc.GeographyId = g.GeographyId
                WHERE c.CountryId = ?;
                """;

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, countryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String geographyName = rs.getString("GeographyName");
                    countryGeographyList.add(new Geography(geographyName));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching country geography list", e);
        }
        return countryGeographyList;
    }

     */

}
