package DAL;

import BE.Profile;
import BE.ProfileRole;
import BE.ProjectTeam;
import BLL.ProjectTeamsManager;
import DAL.DBConnector.DBConnector;
import GUI.Utility.AlertBox;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import CustomExceptions.ApplicationWideException;

public class ProjectTeams_DAO implements IProjectTeamsDataAccess {

    private final DBConnector dbConnector;
    private ProjectTeamsManager projectTeamsManager;

    public ProjectTeams_DAO() throws ApplicationWideException {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new ApplicationWideException("Failed to initialize the database connector",e);
        }
    }



    @Override
    public List<ProjectTeam> getAllProjectTeams() throws ApplicationWideException {

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
            throw new ApplicationWideException("Failed to read all project teams",e);
        }
        return allProjectTeams;
    }

    public List<ProjectTeam> getEveryProjectTeam() throws ApplicationWideException {
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
            throw new ApplicationWideException("Failed to get all project teams",e);
        }
        return everyProjectTeam;
    }




    @Override
    public void addProfileToTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        // SQL for inserting a new project team
        String insertTeamSQL = "INSERT INTO ProjectTeams (TeamName, NumberOfProfiles, AvgOfAnnualSalary, SumOfAnnualSalary, AvgOfHourlyRate, SumOfHourlyRate, AvgOfDailyRate, SumOfDailyRate, Geography) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // SQL for inserting into ProfileProjectTeams
        String insertProfileProjectTeamsSQL = "INSERT INTO ProfileProjectTeams (ProfileId_PPT, TeamsId, Utilization) VALUES (?, ?, ?)";

        // SQL for inserting into GeographyProfile
        String insertGeographyProfileSQL = "INSERT INTO GeographyProfile (GeographyId, ProfileId) VALUES (?, ?)";

        // SQL for updating GeographyProfile
        String updateGeographyProfileSQL = "UPDATE GeographyProfile SET GeographyId = ? WHERE ProfileId = ?";

        // Declare connection outside of try block to handle rollback
        Connection conn = null;

        try {
            if (doesTeamNameExist(projectTeam.getTeamName())) {
                AlertBox.displayInfo("Team Creation", "The team name already exists. Please choose a different name.");
                return;
            }

            conn = dbConnector.getConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(insertTeamSQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement pstmtInsertProfileProjectTeams = conn.prepareStatement(insertProfileProjectTeamsSQL);
                 PreparedStatement pstmtInsertGeographyProfile = conn.prepareStatement(insertGeographyProfileSQL);
                 PreparedStatement pstmtUpdateGeographyProfile = conn.prepareStatement(updateGeographyProfileSQL)) {

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

                        // Insert each profile into ProfileProjectTeams and update GeographyProfile
                        for (Map.Entry<Profile, Double> entry : projectTeam.getUtilizationsMap().entrySet()) {
                            Profile profile = entry.getKey();
                            double utilization = entry.getValue();

                            // Insert into ProfileProjectTeams
                            pstmtInsertProfileProjectTeams.setInt(1, profile.getProfileId());
                            pstmtInsertProfileProjectTeams.setInt(2, teamId);
                            pstmtInsertProfileProjectTeams.setDouble(3, utilization);
                            pstmtInsertProfileProjectTeams.addBatch();

                            // Try to update GeographyProfile first
                            pstmtUpdateGeographyProfile.setInt(1, projectTeam.getGeographyId());
                            pstmtUpdateGeographyProfile.setInt(2, profile.getProfileId());
                            int rowsAffected = pstmtUpdateGeographyProfile.executeUpdate();

                            // If no rows were updated, insert into GeographyProfile
                            if (rowsAffected == 0) {
                                pstmtInsertGeographyProfile.setInt(1, projectTeam.getGeographyId());
                                pstmtInsertGeographyProfile.setInt(2, profile.getProfileId());
                                pstmtInsertGeographyProfile.executeUpdate();
                            }

                            // Adjust profile utilization
                            adjustUtilization(profile, utilization);
                        }
                        pstmtInsertProfileProjectTeams.executeBatch();
                    } else {
                        throw new ApplicationWideException("Failed to retrieve the generated team ID.");
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException rollbackEx) {
                        throw new ApplicationWideException("Transaction rollback failed", rollbackEx);
                    }
                }
                throw new ApplicationWideException("Failed to add profile to team", e);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to add profile to team", e);
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
    public void deleteTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        String deleteTeamSQL = "DELETE FROM ProjectTeams WHERE TeamsId = ?";
        String deleteProfileProjectTeamsSQL = "DELETE FROM ProfileProjectTeams WHERE TeamsId = ?";
        String deleteCountryProjectTeamsSQL = "DELETE FROM CountryProjectTeams WHERE TeamsId = ?";

        Connection conn = null;
        try {
            conn = dbConnector.getConnection();

            try (PreparedStatement pstmtDeleteTeam = conn.prepareStatement(deleteTeamSQL);
                 PreparedStatement pstmtDeleteProfileProjectTeams = conn.prepareStatement(deleteProfileProjectTeamsSQL);
                 PreparedStatement pstmtDeleteCountryProjectTeams = conn.prepareStatement(deleteCountryProjectTeamsSQL)) {

                conn.setAutoCommit(false);

                pstmtDeleteProfileProjectTeams.setInt(1, projectTeam.getTeamId());
                pstmtDeleteProfileProjectTeams.executeUpdate();

                pstmtDeleteCountryProjectTeams.setInt(1, projectTeam.getTeamId());
                pstmtDeleteCountryProjectTeams.executeUpdate();

                pstmtDeleteTeam.setInt(1, projectTeam.getTeamId());
                pstmtDeleteTeam.executeUpdate();

                conn.commit();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back");
                } catch (SQLException ex) {
                    throw new ApplicationWideException("Transaction rollback failed: " + ex.getMessage(), ex);
                }
            }
            throw new ApplicationWideException("SQL operation failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Connection closed");
                } catch (SQLException ex) {
                    throw new ApplicationWideException("Resource cleanup failed: " + ex.getMessage(), ex);
                }
            }
        }
    }


    private void adjustUtilization(Profile profile, double utilization) throws ApplicationWideException{
        String updateUtilizationSQL = "UPDATE Profile SET TotalUtilization = TotalUtilization - ? WHERE ProfileId = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateUtilizationSQL)) {

            pstmt.setDouble(1, utilization);
            pstmt.setInt(2, profile.getProfileId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to adjust utilization", e);
        }

    }

    public boolean doesTeamNameExist(String teamName) throws ApplicationWideException {
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
         catch (SQLException e) {
            throw new ApplicationWideException("Failed to check if team name exists",e);
        }
        return false;
    }

    public List<Profile> getProfileFromProjectTeam(int projectTeamID) throws ApplicationWideException {
        List<Profile> profiles = new ArrayList<>();
        String sql = "SELECT p.*, ppt.Utilization, STRING_AGG(pr.ProfileRoleType, ', ') AS Roles " +
                "FROM Profile p " +
                "JOIN ProfileProjectTeams ppt ON p.ProfileId = ppt.ProfileId_PPT " +
                "JOIN ProfileProfileRole ppr ON p.ProfileId = ppr.ProfileId " +
                "JOIN ProfileRole pr ON ppr.ProfileRoleId = pr.ProfileRoleId " +
                "WHERE ppt.TeamsId = ? " +
                "GROUP BY p.ProfileId, p.Fname, p.Lname, p.Overheadcost, p.AnualSalary, p.HourlySalary, p.DailyRate, p.FixedAmount, p.DailyWorkingHours, p.TotalUtilization, ppt.Utilization, p.UtilizationCost";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectTeamID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                List<ProfileRole> profileRoles = parseRoles(rs.getString("Roles"));
                        int id = rs.getInt("ProfileId");
                        String fName = rs.getString("Fname");
                        String lName = rs.getString("Lname");
                        boolean overhead = rs.getBoolean("Overheadcost");
                        double annual =rs.getDouble("AnualSalary");
                        double hourly = rs.getDouble("HourlySalary");
                        double daily = rs.getDouble("DailyRate");
                        double workingHR = rs.getDouble("DailyWorkingHours");
                        float utilization = rs.getFloat("Utilization");

                Profile profile = new Profile(id, fName, lName, overhead, annual, hourly, daily, workingHR, utilization, profileRoles);
                profiles.add(profile);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to get profiles from project team",e);
        }
        return profiles;
    }

    private List<ProfileRole> parseRoles(String rolesString) {
        List<ProfileRole> roles = new ArrayList<>();
        if (rolesString != null && !rolesString.isEmpty()) {
            String[] roleDescriptions = rolesString.split(", ");
            for (String desc : roleDescriptions) {
                roles.add(new ProfileRole(desc));
            }
        }
        return roles;
    }

    public void updateTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        String updateTeamSQL = "UPDATE ProjectTeams SET TeamName = ?, Geography = ?, SumOfAnnualSalary = ?, SumOfDailyRate = ?, SumOfHourlyRate = ?, NumberOfProfiles = ? WHERE TeamsId = ?";
        String deleteProfileProjectTeamsSQL = "DELETE FROM ProfileProjectTeams WHERE TeamsId = ?";
        String insertProfileProjectTeamsSQL = "INSERT INTO ProfileProjectTeams (ProfileId_PPT, TeamsId, Utilization) VALUES (?, ?, ?)";
        String updateSumValuesSQL = "UPDATE ProjectTeams SET SumOfAnnualSalary = ?, SumOfDailyRate = ?, SumOfHourlyRate = ? WHERE TeamsId = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmtUpdateTeam = conn.prepareStatement(updateTeamSQL);
             PreparedStatement pstmtDeleteProfileProjectTeams = conn.prepareStatement(deleteProfileProjectTeamsSQL);
             PreparedStatement pstmtInsertProfileProjectTeams = conn.prepareStatement(insertProfileProjectTeamsSQL);
             PreparedStatement pstmtUpdateSumValues = conn.prepareStatement(updateSumValuesSQL)) {

            conn.setAutoCommit(false);

            // Update the team details
            pstmtUpdateTeam.setString(1, projectTeam.getTeamName());
            pstmtUpdateTeam.setInt(2, projectTeam.getGeographyId());
            pstmtUpdateTeam.setDouble(3, projectTeam.getSumOfAnnualSalary());
            pstmtUpdateTeam.setDouble(4, projectTeam.getSumOfDailyRate());
            pstmtUpdateTeam.setDouble(5, projectTeam.getSumOfHourlyRate());
            pstmtUpdateTeam.setInt(6, projectTeam.getProfiles().size()); // Set the number of profiles
            pstmtUpdateTeam.setInt(7, projectTeam.getTeamId());
            int rowsAffected = pstmtUpdateTeam.executeUpdate();
            //System.out.println("Rows affected in ProjectTeams: " + rowsAffected);

            // Delete existing profiles from ProfileProjectTeams
            pstmtDeleteProfileProjectTeams.setInt(1, projectTeam.getTeamId());
            pstmtDeleteProfileProjectTeams.executeUpdate();

            // Insert new profile into ProfileProjectTeams
            for (Map.Entry<Profile, Double> entry : projectTeam.getUtilizationsMap().entrySet()) {
                Profile profile = entry.getKey();
                double utilization = entry.getValue();

                pstmtInsertProfileProjectTeams.setInt(1, profile.getProfileId());
                pstmtInsertProfileProjectTeams.setInt(2, projectTeam.getTeamId());
                pstmtInsertProfileProjectTeams.setDouble(3, utilization);
                rowsAffected = pstmtInsertProfileProjectTeams.executeUpdate();
                //System.out.println("Rows affected in ProfileProjectTeams: " + rowsAffected);
            }

            // Update the sum values
            pstmtUpdateSumValues.setDouble(1, projectTeam.getSumOfAnnualSalary());
            pstmtUpdateSumValues.setDouble(2, projectTeam.getSumOfDailyRate());
            pstmtUpdateSumValues.setDouble(3, projectTeam.getSumOfHourlyRate());
            pstmtUpdateSumValues.setInt(4, projectTeam.getTeamId());
            rowsAffected = pstmtUpdateSumValues.executeUpdate();
            //System.out.println("Rows affected in ProjectTeams (sum values): " + rowsAffected);

            conn.commit();
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to update team", e);
        }
    }

    @Override
    public void removeProfileFromProjectTeam(int projectTeamId, int profileId) throws ApplicationWideException {
        String deleteProfileProjectTeamsSQL = "DELETE FROM ProfileProjectTeams WHERE ProfileId_PPT = ? AND TeamsId = ?";
        String updateProfileCountSQL = "UPDATE ProjectTeams SET NumberOfProfiles = NumberOfProfiles - 1 WHERE TeamsId = ?";

        Connection conn = null;
        try {
            conn = dbConnector.getConnection();

            try (PreparedStatement pstmtDeleteProfileProjectTeams = conn.prepareStatement(deleteProfileProjectTeamsSQL);
                 PreparedStatement pstmtUpdateProfileCount = conn.prepareStatement(updateProfileCountSQL)) {

                conn.setAutoCommit(false);

                pstmtDeleteProfileProjectTeams.setInt(1, profileId);
                pstmtDeleteProfileProjectTeams.setInt(2, projectTeamId);
                int rowsAffectedProfileProjectTeams = pstmtDeleteProfileProjectTeams.executeUpdate();

                pstmtUpdateProfileCount.setInt(1, projectTeamId);
                pstmtUpdateProfileCount.executeUpdate();

                System.out.println("Deleting profile with ID: " + profileId + " from team with ID: " + projectTeamId + "DAO_SOUT"); // Added print statement
                System.out.println("Rows affected in ProfileProjectTeams: " + rowsAffectedProfileProjectTeams + "DAO_SOUT"); // Debug statement

                conn.commit();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back");
                } catch (SQLException ex) {
                    throw new ApplicationWideException("Transaction rollback failed: " + ex.getMessage(), ex);
                }
            }
            throw new ApplicationWideException("SQL operation failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Connection closed");
                } catch (SQLException ex) {
                    throw new ApplicationWideException("Resource cleanup failed: " + ex.getMessage(), ex);
                }
            }
        }
    }
}

