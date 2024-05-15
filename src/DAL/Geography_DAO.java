package DAL;

import BE.Country;
import BE.Geography;
import DAL.DBConnector.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Geography> getAllGeographie() {
        List<Geography> allGeographies = new ArrayList<>();
        String sql = "SELECT * FROM Geography;";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
           SUM(p.DailyRate) AS TotalDailyRate,
           AVG(p.DailyRate) AS AvgDailyRate,
           SUM(p.HourlySalary) AS TotalHourlyRate,
           AVG(p.HourlySalary) AS AvgHourlyRate,
           COUNT(p.ProfileId) AS ProfileCount
       FROM
           Geography g
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
                        rs.getDouble("TotalDailyRate"),
                        rs.getDouble("TotalHourlyRate"),
                        rs.getDouble("AvgDailyRate"),
                        rs.getDouble("AvgHourlyRate"),
                        rs.getInt("ProfileCount")
                );
                allGeographies.add(geography);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return allGeographies;
    }

    @Override
    public List<Geography> getAllGeographiesGeographyOverview() throws Exception {
        Map<Integer, Geography> allGeographies = new HashMap<>();
        String sql = """
    SELECT g.GeographyId, g.GeographyName, c.CountryName, COUNT(DISTINCT gp.ProfileId) AS ProfileCount, COUNT(DISTINCT ppt.TeamsId) AS TeamCount
    FROM Geography g
    LEFT JOIN GeographyCountry gc ON g.GeographyId = gc.GeographyId
    LEFT JOIN Country c ON gc.CountryId = c.CountryId
    LEFT JOIN GeographyProfile gp ON g.GeographyId = gp.GeographyId
    LEFT JOIN Profile p ON gp.ProfileId = p.ProfileId
    LEFT JOIN ProfileProjectTeams ppt ON p.ProfileId = ppt.ProfileId_PPT
    GROUP BY g.GeographyId, g.GeographyName, c.CountryName;
    """;
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int geographyId = rs.getInt("GeographyId");
                Geography geography = allGeographies.get(geographyId);
                if (geography == null) {
                    geography = new Geography(
                            geographyId,
                            rs.getString("GeographyName"),
                            rs.getInt("ProfileCount"),
                            rs.getInt("TeamCount")
                    );
                    allGeographies.put(geographyId, geography);
                }
                Country country = new Country(rs.getString("CountryName"));
                geography.getCountries().add(country);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(allGeographies.values());
    }

    /*@Override
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
