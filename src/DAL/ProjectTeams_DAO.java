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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import CustomExceptions.ApplicationWideException;

public class ProjectTeams_DAO implements IProjectTeamsDataAccess {

    private final DBConnector dbConnector;
    private ProjectTeamsManager projectTeamsManager;

    public ProjectTeams_DAO() throws ApplicationWideException {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new ApplicationWideException("Failed to initialize the database connector", e);
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
            throw new ApplicationWideException("Failed to read all project teams", e);
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
            throw new ApplicationWideException("Failed to get all project teams", e);
        }
        return everyProjectTeam;
    }



    @Override
    public void addProfileToTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        String insertTeamSQL = "INSERT INTO ProjectTeams (TeamName, NumberOfProfiles, AvgOfAnnualSalary, SumOfAnnualSalary, AvgOfHourlyRate, SumOfHourlyRate, AvgOfDailyRate, SumOfDailyRate, Geography) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertProfileProjectTeamsSQL = "INSERT INTO ProfileProjectTeams (ProfileId_PPT, TeamsId, Utilization, UtilizationCost) VALUES (?, ?, ?, ?)";
        String insertGeographyProfileSQL = "INSERT INTO GeographyProfile (GeographyId, ProfileId) VALUES (?, ?)";
        String updateGeographyProfileSQL = "UPDATE GeographyProfile SET GeographyId = ? WHERE ProfileId = ?";

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

                conn.setAutoCommit(false);

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

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int teamId = generatedKeys.getInt(1);

                        for (Profile profile : projectTeam.getProfiles()) {
                            double utilization = projectTeam.getUtilizationsMap().get(profile);
                            double utilizationCost = projectTeam.getUtilizationCostMap().get(profile);

                            try {
                                // Insert into ProfileProjectTeams
                                pstmtInsertProfileProjectTeams.setInt(1, profile.getProfileId());
                                pstmtInsertProfileProjectTeams.setInt(2, teamId);
                                pstmtInsertProfileProjectTeams.setDouble(3, utilization);
                                pstmtInsertProfileProjectTeams.setDouble(4, utilizationCost);
                                pstmtInsertProfileProjectTeams.addBatch();
                            } catch (SQLException e) {
                                throw new ApplicationWideException("Failed to insert into ProfileProjectTeams", e);
                            }

                            try {
                                // Update GeographyProfile
                                pstmtUpdateGeographyProfile.setInt(1, projectTeam.getGeographyId());
                                pstmtUpdateGeographyProfile.setInt(2, profile.getProfileId());
                                int rowsAffected = pstmtUpdateGeographyProfile.executeUpdate();

                                // If no rows were updated, insert into GeographyProfile
                                if (rowsAffected == 0) {
                                    pstmtInsertGeographyProfile.setInt(1, projectTeam.getGeographyId());
                                    pstmtInsertGeographyProfile.setInt(2, profile.getProfileId());
                                    pstmtInsertGeographyProfile.executeUpdate();
                                }
                            } catch (SQLException e) {
                                throw new ApplicationWideException("Failed to update or insert into GeographyProfile", e);
                            }

                            try {
                                // Adjust profile utilization and utilization cost
                                adjustUtilization(profile, utilization);
                                adjustUtilizationCost(profile, utilizationCost);
                            } catch (ApplicationWideException e) {
                                throw new ApplicationWideException("Failed to adjust profile utilization and utilization cost", e);
                            }
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
        String selectUtilizationsSQL = "SELECT ProfileId_PPT, Utilization, UtilizationCost FROM ProfileProjectTeams WHERE TeamsId = ?";
        String deleteProfileProjectTeamsSQL = "DELETE FROM ProfileProjectTeams WHERE TeamsId = ?";
        String deleteCountryProjectTeamsSQL = "DELETE FROM CountryProjectTeams WHERE TeamsId = ?";
        String deleteGeographyProfileSQL = "DELETE FROM GeographyProfile WHERE GeographyId = ?";
        String deleteTeamSQL = "DELETE FROM ProjectTeams WHERE TeamsId = ?";
        String updateProfileUtilizationSQL = "UPDATE Profile SET TotalUtilization = TotalUtilization + ?, UtilizationCost = UtilizationCost + ? WHERE ProfileId = ?";

        Connection conn = null;
        try {
            conn = dbConnector.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtSelectUtilizations = conn.prepareStatement(selectUtilizationsSQL);
                 PreparedStatement pstmtDeleteProfileProjectTeams = conn.prepareStatement(deleteProfileProjectTeamsSQL);
                 PreparedStatement pstmtDeleteCountryProjectTeams = conn.prepareStatement(deleteCountryProjectTeamsSQL);
                 PreparedStatement pstmtDeleteGeographyProfile = conn.prepareStatement(deleteGeographyProfileSQL);
                 PreparedStatement pstmtDeleteTeam = conn.prepareStatement(deleteTeamSQL);
                 PreparedStatement pstmtUpdateProfileUtilization = conn.prepareStatement(updateProfileUtilizationSQL)) {

                int teamId = projectTeam.getTeamId();

                // Retrieve the Utilization and UtilizationCost for each profile
                pstmtSelectUtilizations.setInt(1, teamId);
                ResultSet rs = pstmtSelectUtilizations.executeQuery();
                while (rs.next()) {
                    int profileId = rs.getInt("ProfileId_PPT");
                    double utilization = rs.getDouble("Utilization");
                    double utilizationCost = rs.getDouble("UtilizationCost");

                    // Add the Utilization and UtilizationCost to the profile's totals
                    pstmtUpdateProfileUtilization.setDouble(1, utilization);
                    pstmtUpdateProfileUtilization.setDouble(2, utilizationCost);
                    pstmtUpdateProfileUtilization.setInt(3, profileId);
                    pstmtUpdateProfileUtilization.executeUpdate();
                }

                // Delete from ProfileProjectTeams
                pstmtDeleteProfileProjectTeams.setInt(1, teamId);
                pstmtDeleteProfileProjectTeams.executeUpdate();

                // Delete from CountryProjectTeams
                pstmtDeleteCountryProjectTeams.setInt(1, teamId);
                pstmtDeleteCountryProjectTeams.executeUpdate();

                // Delete from GeographyProfile
                pstmtDeleteGeographyProfile.setInt(1, projectTeam.getGeographyId());
                pstmtDeleteGeographyProfile.executeUpdate();

                // Delete from ProjectTeams
                pstmtDeleteTeam.setInt(1, teamId);
                pstmtDeleteTeam.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        throw new ApplicationWideException("Transaction rollback failed: " + ex.getMessage(), ex);
                    }
                }
                throw new ApplicationWideException("SQL operation failed: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to delete team", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    throw new ApplicationWideException("Resource cleanup failed: " + ex.getMessage(), ex);
                }
            }
        }
    }




    private void adjustUtilization(Profile profile, double utilization) throws ApplicationWideException {
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

    private void adjustUtilizationCost(Profile profile, double utilizationCost) throws ApplicationWideException {
        String updateUtilizationCostSQL = "UPDATE Profile SET UtilizationCost = UtilizationCost - ? WHERE ProfileId = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateUtilizationCostSQL)) {

            pstmt.setDouble(1, utilizationCost);
            pstmt.setInt(2, profile.getProfileId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to adjust utilization cost", e);
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
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to check if team name exists", e);
        }
        return false;
    }

    public List<Profile> getProfileFromProjectTeam(int projectTeamID) throws ApplicationWideException {
        List<Profile> profiles = new ArrayList<>();
        String sql = "SELECT p.*, ppt.Utilization, ppt.UtilizationCost AS uCost, STRING_AGG(pr.ProfileRoleType, ', ') AS Roles " +
                "FROM Profile p " +
                "JOIN ProfileProjectTeams ppt ON p.ProfileId = ppt.ProfileId_PPT " +
                "JOIN ProfileProfileRole ppr ON p.ProfileId = ppr.ProfileId " +
                "JOIN ProfileRole pr ON ppr.ProfileRoleId = pr.ProfileRoleId " +
                "WHERE ppt.TeamsId = ? " +
                "GROUP BY p.ProfileId, p.Fname, p.Lname, p.Overheadcost, p.AnualSalary, p.HourlySalary, p.DailyRate, p.FixedAmount, p.DailyWorkingHours, p.TotalUtilization, ppt.Utilization, ppt.UtilizationCost, p.UtilizationCost, p.EffectiveWorkingHours, p.OverheadMultiplierPercentage";

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
                double annual = rs.getDouble("AnualSalary");
                double hourly = rs.getDouble("HourlySalary");
                double daily = rs.getDouble("DailyRate");
                double workingHR = rs.getDouble("DailyWorkingHours");
                double utilizationTime = rs.getDouble("Utilization");
                double utilizationCost = rs.getDouble("uCost");

                Profile profile = new Profile(id, fName, lName, overhead, annual, hourly, daily, workingHR, utilizationTime, utilizationCost, profileRoles);
                profiles.add(profile);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to get profiles from project team", e);
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
        Connection conn = null;
        try {
            conn = dbConnector.getConnection();
            conn.setAutoCommit(false);

            updateTeamDetails(conn, projectTeam);
            Set<Integer> existingProfileIds = getExistingProfileIds(conn, projectTeam.getTeamId());

            deleteProfilesNotInList(conn, projectTeam.getTeamId(), projectTeam.getProfiles());
            insertOrUpdateProfiles(conn, projectTeam);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new ApplicationWideException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new ApplicationWideException("Failed to update team", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    throw new ApplicationWideException("Failed to reset connection settings and close the connection", ex);
                }
            }
        }
    }


    private void deleteProfilesNotInList(Connection conn, int teamId, List<Profile> profiles) throws SQLException {
        String deleteSQL = "DELETE FROM ProfileProjectTeams WHERE TeamsId = ? AND ProfileId_PPT NOT IN (";
        for (int i = 0; i < profiles.size(); i++) {
            deleteSQL += "?";
            if (i < profiles.size() - 1) {
                deleteSQL += ",";
            }
        }
        deleteSQL += ")";

        try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSQL)) {
            pstmtDelete.setInt(1, teamId);
            for (int i = 0; i < profiles.size(); i++) {
                pstmtDelete.setInt(i + 2, profiles.get(i).getProfileId());
            }
            pstmtDelete.executeUpdate();
        }
    }

    private void insertOrUpdateProfiles(Connection conn, ProjectTeam projectTeam) throws SQLException, ApplicationWideException {
        String updateSQL = "UPDATE ProfileProjectTeams SET Utilization = ?, UtilizationCost = ? WHERE ProfileId_PPT = ? AND TeamsId = ?";
        String insertSQL = "INSERT INTO ProfileProjectTeams (ProfileId_PPT, TeamsId, Utilization, UtilizationCost) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSQL);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL)) {

            for (Profile profile : projectTeam.getProfiles()) {
                double utilization = projectTeam.getUtilizationsMap().get(profile);
                double utilizationCost = projectTeam.getUtilizationCostMap().get(profile);

                pstmtUpdate.setDouble(1, utilization);
                pstmtUpdate.setDouble(2, utilizationCost);
                pstmtUpdate.setInt(3, profile.getProfileId());
                pstmtUpdate.setInt(4, projectTeam.getTeamId());

                int affectedRows = pstmtUpdate.executeUpdate();

                if (affectedRows == 0) {
                    pstmtInsert.setInt(1, profile.getProfileId());
                    pstmtInsert.setInt(2, projectTeam.getTeamId());
                    pstmtInsert.setDouble(3, utilization);
                    pstmtInsert.setDouble(4, utilizationCost);
                    pstmtInsert.executeUpdate();

                    // Adjust profile utilization and utilization cost for new profiles
                    adjustUtilization(profile, utilization);
                    adjustUtilizationCost(profile, utilizationCost);
                }
            }
        }
    }


    private Set<Integer> getExistingProfileIds(Connection conn, int teamId) throws SQLException {
        Set<Integer> existingProfileIds = new HashSet<>();
        String selectProfileIdsSQL = "SELECT ProfileId_PPT FROM ProfileProjectTeams WHERE TeamsId = ?";
        try (PreparedStatement pstmtSelectProfileIds = conn.prepareStatement(selectProfileIdsSQL)) {
            pstmtSelectProfileIds.setInt(1, teamId);
            try (ResultSet rs = pstmtSelectProfileIds.executeQuery()) {
                while (rs.next()) {
                    existingProfileIds.add(rs.getInt("ProfileId_PPT"));
                }
            }
        }
        return existingProfileIds;
    }

    public void updateTeamDetails(Connection conn, ProjectTeam projectTeam) throws SQLException {
        String updateTeamSQL = "UPDATE ProjectTeams SET " +
                "TeamName = ?, Geography = ?, SumOfAnnualSalary = ?, SumOfDailyRate = ?, SumOfHourlyRate = ?, NumberOfProfiles = ? " +
                "WHERE TeamsId = ?";

        try (PreparedStatement pstmtUpdateTeam = conn.prepareStatement(updateTeamSQL)) {

            // Update team details
            pstmtUpdateTeam.setString(1, projectTeam.getTeamName());
            pstmtUpdateTeam.setInt(2, projectTeam.getGeographyId());
            pstmtUpdateTeam.setDouble(3, projectTeam.getSumOfAnnualSalary());
            pstmtUpdateTeam.setDouble(4, projectTeam.getSumOfDailyRate());
            pstmtUpdateTeam.setDouble(5, projectTeam.getSumOfHourlyRate());
            pstmtUpdateTeam.setInt(6, projectTeam.getProfiles().size());
            pstmtUpdateTeam.setInt(7, projectTeam.getTeamId());
            pstmtUpdateTeam.executeUpdate();

            // Update averages
            updateAverages(conn, projectTeam);
        }
    }

    public void updateAverages(Connection conn, ProjectTeam projectTeam) throws SQLException {
        String updateAveragesSQL = "UPDATE ProjectTeams SET " +
                "AvgOfAnnualSalary = SumOfAnnualSalary / NULLIF(NumberOfProfiles, 0), " +
                "AvgOfDailyRate = SumOfDailyRate / NULLIF(NumberOfProfiles, 0), " +
                "AvgOfHourlyRate = SumOfHourlyRate / NULLIF(NumberOfProfiles, 0) " +
                "WHERE TeamsId = ?";

        try (PreparedStatement pstmtUpdateAverages = conn.prepareStatement(updateAveragesSQL)) {
            pstmtUpdateAverages.setInt(1, projectTeam.getTeamId());
            pstmtUpdateAverages.executeUpdate();
        }
    }

    @Override
    public void removeProfileFromProjectTeam(int projectTeamId, int profileId) throws ApplicationWideException {
        String selectUtilizationAndCostSQL = "SELECT Utilization, UtilizationCost FROM ProfileProjectTeams WHERE ProfileId_PPT = ? AND TeamsId = ?";
        String deleteProfileProjectTeamsSQL = "DELETE FROM ProfileProjectTeams WHERE ProfileId_PPT = ? AND TeamsId = ?";
        String updateProfileCountSQL = "UPDATE ProjectTeams SET NumberOfProfiles = NumberOfProfiles - 1 WHERE TeamsId = ?";
        String updateProfileUtilizationSQL = "UPDATE Profile SET TotalUtilization = TotalUtilization + ?, UtilizationCost = UtilizationCost + ? WHERE ProfileId = ?";

        Connection conn = null;
        try {
            conn = dbConnector.getConnection();

            try (PreparedStatement pstmtSelectUtilizationAndCost = conn.prepareStatement(selectUtilizationAndCostSQL);
                 PreparedStatement pstmtDeleteProfileProjectTeams = conn.prepareStatement(deleteProfileProjectTeamsSQL);
                 PreparedStatement pstmtUpdateProfileCount = conn.prepareStatement(updateProfileCountSQL);
                 PreparedStatement pstmtUpdateProfileUtilization = conn.prepareStatement(updateProfileUtilizationSQL)) {

                conn.setAutoCommit(false);

                // Retrieve the Utilization and UtilizationCost
                pstmtSelectUtilizationAndCost.setInt(1, profileId);
                pstmtSelectUtilizationAndCost.setInt(2, projectTeamId);
                ResultSet rs = pstmtSelectUtilizationAndCost.executeQuery();
                if (rs.next()) {
                    double utilization = rs.getDouble("Utilization");
                    double utilizationCost = rs.getDouble("UtilizationCost");

                    // Add the Utilization and UtilizationCost to the profile's totals
                    pstmtUpdateProfileUtilization.setDouble(1, utilization);
                    pstmtUpdateProfileUtilization.setDouble(2, utilizationCost);
                    pstmtUpdateProfileUtilization.setInt(3, profileId);
                    pstmtUpdateProfileUtilization.executeUpdate();
                }

                // Delete the profile from the ProfileProjectTeams table
                pstmtDeleteProfileProjectTeams.setInt(1, profileId);
                pstmtDeleteProfileProjectTeams.setInt(2, projectTeamId);
                pstmtDeleteProfileProjectTeams.executeUpdate();

                // Update the profile count in the ProjectTeams table
                pstmtUpdateProfileCount.setInt(1, projectTeamId);
                pstmtUpdateProfileCount.executeUpdate();

                conn.commit();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
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
                } catch (SQLException ex) {
                    throw new ApplicationWideException("Resource cleanup failed: " + ex.getMessage(), ex);
                }
            }
        }
    }

}
