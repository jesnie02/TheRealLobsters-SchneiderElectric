package DAL;

import BE.Country;
import BE.Profile;
import BE.ProjectTeam;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectTeams_DAO implements IProjectTeamsDataAccess {

    private final DBConnector dbConnector;

    public ProjectTeams_DAO() throws IOException {
        dbConnector = new DBConnector();
    }


    /**
     * Gets all project teams from the database.
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ProjectTeam> getAllProjectTeams() throws Exception {

        List<ProjectTeam> allProjectTeams = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM ProjectTeams";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProjectTeam projectTeam = new ProjectTeam(
                        rs.getInt("TeamsId"),
                        rs.getString("TeamName")
                );
                allProjectTeams.add(projectTeam);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allProjectTeams;
    }

    public List<ProjectTeam> getEveryProjectTeam() {
        List<ProjectTeam> everyProjectTeam = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM ProjectTeams";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("TeamsId");
                String teamName = rs.getString("TeamName");
                double sumOfHourlyRate = rs.getDouble("SumOfHourlyRate");
                double sumOfDailyRate = rs.getDouble("SumOfDailyRate");
                double avgDailyRate = rs.getDouble("AvgOfDailyRate");
                double avgHourlyRate = rs.getDouble("AvgOfHourlyRate");
                double sumOfAnnualSalary = rs.getDouble("SumOfAnnualSalary");
                double avgAnnualSalary = rs.getDouble("AvgOfAnnualSalary");
                int numberOfProfiles = rs.getInt("NumberOfProfiles");
                int countryId = rs.getInt("CountryId");
                ProjectTeam projectTeam = new ProjectTeam(id, teamName, sumOfHourlyRate, sumOfDailyRate, avgDailyRate, avgHourlyRate, sumOfAnnualSalary, avgAnnualSalary, numberOfProfiles, countryId);
                everyProjectTeam.add(projectTeam);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Handle exception
        }
        return everyProjectTeam;
    }

    @Override
    public void addProfileToTeam(ProjectTeam projectTeam) {
        // SQL for inserting a new project team
        String insertTeamSQL = "INSERT INTO ProjectTeams (TeamName, CountryId, NumberOfProfiles, AvgOfAnnualSalary, SumOfAnnualSalary, AvgOfHourlyRate, SumOfHourlyRate, AvgOfDailyRate, SumOfDailyRate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // SQL for updating profiles to set the ProjectTeamId
        String updateProfileSQL = "UPDATE Profile SET ProjectTeams = ? WHERE ProfileId = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtUpdateProfile = null;
        ResultSet generatedKeys = null;

        try {
            conn = dbConnector.getConnection();
            // Disable auto-commit for transaction management
            conn.setAutoCommit(false);

            // Insert the new team
            pstmt = conn.prepareStatement(insertTeamSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, projectTeam.getTeamName());
            pstmt.setInt(2, projectTeam.getCountry().getCountryId());
            pstmt.setInt(3, projectTeam.getProfiles().size());
            pstmt.setDouble(4, projectTeam.getAvgAnnualSalary());
            pstmt.setDouble(5, projectTeam.getSumOfAnnualSalary());
            pstmt.setDouble(6, projectTeam.getAvgHourlyRate());
            pstmt.setDouble(7, projectTeam.getSumOfHourlyRate());
            pstmt.setDouble(8, projectTeam.getAvgDailyRate());
            pstmt.setDouble(9, projectTeam.getSumOfDailyRate());
            pstmt.executeUpdate();

            // Retrieve the generated team ID
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int teamId = generatedKeys.getInt(1);
                pstmtUpdateProfile = conn.prepareStatement(updateProfileSQL);

                // Update each profile to set the ProjectTeamId
                for (Profile profile : projectTeam.getProfiles()) {
                    pstmtUpdateProfile.setInt(1, teamId);
                    pstmtUpdateProfile.setInt(2, profile.getProfileId());
                    pstmtUpdateProfile.addBatch();
                }
                pstmtUpdateProfile.executeBatch();
            }

            // Commit the transaction
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    System.err.println("Transaction rollback failed: " + ex.getMessage());
                }
            }
            e.printStackTrace(); // TODO: Replace with more robust error handling
        } finally {
            // Clean up resources
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmtUpdateProfile != null) pstmtUpdateProfile.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }
    }




}

