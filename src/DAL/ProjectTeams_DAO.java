package DAL;

import BE.Country;
import BE.Profile;
import BE.ProjectTeam;
import DAL.DBConnector.DBConnector;
import GUI.Utility.AlertBox;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import GUI.Utility.AlertBox;
import CustomExceptions.ApplicationWideException;

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
                int geographyId = rs.getInt("Geography");

                ProjectTeam projectTeam = new ProjectTeam(id, teamName, sumOfHourlyRate, sumOfDailyRate, avgDailyRate, avgHourlyRate, sumOfAnnualSalary, avgAnnualSalary, numberOfProfiles, geographyId);
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
        String insertTeamSQL = "INSERT INTO ProjectTeams (TeamName, NumberOfProfiles, AvgOfAnnualSalary, SumOfAnnualSalary, AvgOfHourlyRate, SumOfHourlyRate, AvgOfDailyRate, SumOfDailyRate, Geography) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // SQL for inserting into ProfileProjectTeams
        String insertProfileProjectTeamsSQL = "INSERT INTO ProfileProjectTeams (ProfileId_PPT, TeamsId) VALUES (?, ?)";

        // Declare connection outside of try block to handle rollback
        Connection conn = null;

        try {
            if (doesTeamNameExist(projectTeam.getTeamName())) {
                AlertBox.displayInfo("Team Creation", "The team name already exists. Please choose a different name.");
                return;
            }

            conn = dbConnector.getConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(insertTeamSQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement pstmtInsertProfileProjectTeams = conn.prepareStatement(insertProfileProjectTeamsSQL)) {

                // Disable auto-commit for transaction management
                conn.setAutoCommit(false);

                // Insert the new team
                pstmt.setString(1, projectTeam.getTeamName());
                pstmt.setInt(2, projectTeam.getProfiles().size());
                pstmt.setDouble(3, projectTeam.getAvgAnnualSalary());
                pstmt.setDouble(4, projectTeam.getSumOfAnnualSalary());
                pstmt.setDouble(5, projectTeam.getAvgHourlyRate());
                pstmt.setDouble(6, projectTeam.getSumOfHourlyRate());
                pstmt.setDouble(7, projectTeam.getAvgDailyRate());
                pstmt.setDouble(8, projectTeam.getSumOfDailyRate());
                pstmt.setInt(9, projectTeam.getGeographyId());
                pstmt.executeUpdate();

                // Retrieve the generated team ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int teamId = generatedKeys.getInt(1);

                        // Insert each profile into ProfileProjectTeams
                        for (Profile profile : projectTeam.getProfiles()) {
                            pstmtInsertProfileProjectTeams.setInt(1, profile.getProfileId());
                            pstmtInsertProfileProjectTeams.setInt(2, teamId);
                            pstmtInsertProfileProjectTeams.addBatch();
                        }
                        pstmtInsertProfileProjectTeams.executeBatch();
                    }
                }
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: Replace with more robust error handling
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Transaction rollback failed: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    System.err.println("Resource cleanup failed: " + ex.getMessage());
                }
            }
        }
    }


    public boolean doesTeamNameExist(String teamName) throws SQLException {
        String checkTeamSQL = "SELECT COUNT(*) FROM ProjectTeams WHERE TeamName = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkTeamSQL)) {

            pstmt.setString(1, teamName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}

