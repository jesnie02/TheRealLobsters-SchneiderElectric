package DAL;

import BE.Country;
import BE.Geography;
import CustomExceptions.ApplicationWideException;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Geography_DAO implements IGeographyDataAccess {

    private final DBConnector dbConnector;

    public Geography_DAO() throws ApplicationWideException {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new ApplicationWideException("Failed to initialize the database connector",e);
        }
    }



    @Override
    public List<Geography> getAllGeographie() throws ApplicationWideException{
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
            throw new ApplicationWideException("Failed to read all geographies",e);
        }
        return allGeographies;
    }





    @Override
    public List<Geography> getSumsAndAveragesForGeographies() throws ApplicationWideException {
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
            throw new ApplicationWideException("Failed to get sum and average",e);
        }
        return allGeographies;
    }

    @Override
    public List<Geography> getAllGeographiesGeographyOverview() throws ApplicationWideException {
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
            throw new ApplicationWideException("Failed to get all geographies",e);
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

    /*public void createGeography(String geographyName, List<Integer> countryIds) throws Exception {
        String sql = "INSERT INTO Geography (GeographyName) VALUES (?);";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, geographyName);
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int geographyId = generated;
                    String sql2 = "INSERT INTO GeographyCountry (GeographyId, CountryId) VALUES (?, ?);";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                        for (int countryId : countryIds) {
                            pstmt2.setInt(1, geographyId);
                            pstmt2.setInt(2, countryId);
                            pstmt2.addBatch();
                        }
                        pstmt2.executeBatch();
                    }
                }
            }
        }
    }*/


    @Override
    public void saveGeography(Geography geography) throws ApplicationWideException {
        String insertGeographySql = "INSERT INTO Geography (GeographyName) VALUES (?);";
        String insertGeographyCountrySql = "INSERT INTO GeographyCountry (GeographyId, CountryId) VALUES (?, ?);";

        Connection conn = null;
        try {
            conn = dbConnector.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtGeography = conn.prepareStatement(insertGeographySql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement pstmtGeographyCountry = conn.prepareStatement(insertGeographyCountrySql)) {

                // Insert to Geography table
                pstmtGeography.setString(1, geography.getGeographyName());
                pstmtGeography.executeUpdate();

                // Retrieve GeographyId
                int geographyId;
                try (ResultSet generatedKeys = pstmtGeography.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        geographyId = generatedKeys.getInt(1);
                    } else {
                        throw new ApplicationWideException("Creating geography failed, no ID obtained.");
                    }
                }

                // Insert to GeographyCountry for each selected country
                for (Country country : geography.getCountries()) {
                    pstmtGeographyCountry.setInt(1, geographyId);
                    pstmtGeographyCountry.setInt(2, country.getCountryId());
                    pstmtGeographyCountry.addBatch();
                }
                pstmtGeographyCountry.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException rollbackEx) {
                        throw new ApplicationWideException("Rollback failed", rollbackEx);
                    }
                }
                throw new ApplicationWideException("Failed to create Geography", e);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to create Geography", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new ApplicationWideException("Failed to reset connection settings and close the connection", e);
                }
            }
        }
    }

}
