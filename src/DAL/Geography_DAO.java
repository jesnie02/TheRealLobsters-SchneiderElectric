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
           SELECT g.GeographyId, g.GeographyName, c.CountryName,
                  COUNT(DISTINCT gp.ProfileId) AS ProfileCount,
                  COUNT(DISTINCT t.TeamsId) AS TeamCount,
                  COUNT(DISTINCT ppt.ProfileId_PPT) AS ProfileInTeamsCount
            FROM Geography g
            LEFT JOIN GeographyCountry gc ON g.GeographyId = gc.GeographyId
            LEFT JOIN Country c ON gc.CountryId = c.CountryId
            LEFT JOIN GeographyProfile gp ON g.GeographyId = gp.GeographyId
            LEFT JOIN Profile p ON gp.ProfileId = p.ProfileId
            LEFT JOIN ProjectTeams t ON g.GeographyId = t.Geography
            LEFT JOIN ProfileProjectTeams ppt ON t.TeamsId = ppt.TeamsId
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
                            rs.getInt("TeamCount"),
                            rs.getInt("ProfileInTeamsCount") // Add this line
                    );
                    allGeographies.put(geographyId, geography);
                }

                String countryName = rs.getString("CountryName");
                if (countryName != null && !countryName.isEmpty()) {
                    Country country = new Country(countryName);
                    geography.getCountries().add(country);
                }
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to get all geographies", e);
        }

        return new ArrayList<>(allGeographies.values());
    }

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


    @Override
    public void deleteGeography(int geographyId) throws ApplicationWideException {
        String updateProjectTeamsSql = "UPDATE ProjectTeams SET Geography = NULL WHERE Geography = ?;";
        String deleteProfileProjectTeamsSql = "DELETE FROM ProfileProjectTeams WHERE TeamsId IN (SELECT TeamsId FROM ProjectTeams WHERE Geography = ?);";
        String deleteGeographyCountrySql = "DELETE FROM GeographyCountry WHERE GeographyId = ?;";
        String deleteGeographyProfileSql = "DELETE FROM GeographyProfile WHERE GeographyId = ?;";
        String deleteGeographySql = "DELETE FROM Geography WHERE GeographyId = ?;";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmtUpdateProjectTeams = conn.prepareStatement(updateProjectTeamsSql);
             PreparedStatement pstmtProfileProjectTeams = conn.prepareStatement(deleteProfileProjectTeamsSql);
             PreparedStatement pstmtGeographyCountry = conn.prepareStatement(deleteGeographyCountrySql);
             PreparedStatement pstmtGeographyProfile = conn.prepareStatement(deleteGeographyProfileSql);
             PreparedStatement pstmtGeography = conn.prepareStatement(deleteGeographySql)) {

            conn.setAutoCommit(false); // Start transaction

            try {
                // Step 1: Set Geography to NULL in ProjectTeams
                pstmtUpdateProjectTeams.setInt(1, geographyId);
                pstmtUpdateProjectTeams.executeUpdate();

                // Step 2: Delete from ProfileProjectTeams
                pstmtProfileProjectTeams.setInt(1, geographyId);
                pstmtProfileProjectTeams.executeUpdate();

                // Step 3: Delete from GeographyCountry
                pstmtGeographyCountry.setInt(1, geographyId);
                pstmtGeographyCountry.executeUpdate();

                // Step 4: Delete from GeographyProfile
                pstmtGeographyProfile.setInt(1, geographyId);
                pstmtGeographyProfile.executeUpdate();

                // Step 5: Delete from Geography
                pstmtGeography.setInt(1, geographyId);
                pstmtGeography.executeUpdate();

                conn.commit(); // Commit transaction if all updates and deletes are successful
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction if any update or delete fails
                throw new ApplicationWideException("Failed to delete geography", e);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to delete geography", e);
        }

    }





    @Override
    public void updateGeography(Geography geography) throws ApplicationWideException {
        String updateGeographySql = "UPDATE Geography SET GeographyName = ? WHERE GeographyId = ?;";
        String deleteGeographyCountrySql = "DELETE FROM GeographyCountry WHERE GeographyId = ?;";
        String insertGeographyCountrySql = "INSERT INTO GeographyCountry (GeographyId, CountryId) VALUES (?, ?);";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmtUpdateGeography = conn.prepareStatement(updateGeographySql);
             PreparedStatement pstmtDeleteGeographyCountry = conn.prepareStatement(deleteGeographyCountrySql);
             PreparedStatement pstmtInsertGeographyCountry = conn.prepareStatement(insertGeographyCountrySql)) {

            conn.setAutoCommit(false);

            try {
                pstmtUpdateGeography.setString(1, geography.getGeographyName());
                pstmtUpdateGeography.setInt(2, geography.getGeographyId());
                pstmtUpdateGeography.executeUpdate();

                pstmtDeleteGeographyCountry.setInt(1, geography.getGeographyId());
                pstmtDeleteGeographyCountry.executeUpdate();

                for (Country country : geography.getCountries()) {
                    pstmtInsertGeographyCountry.setInt(1, geography.getGeographyId());
                    pstmtInsertGeographyCountry.setInt(2, country.getCountryId());
                    pstmtInsertGeographyCountry.addBatch();
                }
                pstmtInsertGeographyCountry.executeBatch();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new ApplicationWideException("Failed to update geography", e);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to update geography", e);
        }
    }
}
