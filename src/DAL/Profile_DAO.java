package DAL;

import BE.Profile;
import BE.ProfileRole;
import CustomExceptions.ApplicationWideException;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Profile_DAO implements IProfileDataAccess {

    private final DBConnector dbConnector;


    public Profile_DAO() throws ApplicationWideException {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new ApplicationWideException("Failed to initialize the database connector",e);
        }
    }

    /**
     * Retrieves all profiles from the database.
     * @return A list of Profile objects.
     */
    @Override
    public List<Profile> getAllProfiles() throws ApplicationWideException {
        List<Profile> allProfiles = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM Profile";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int profileId = rs.getInt("ProfileId");
                String fName = rs.getString("FName");
                String lName = rs.getString("LName");
                Boolean overheadCost = rs.getBoolean("OverheadCost");
                double annualSalary = rs.getDouble("AnualSalary");
                double hourlyRate = rs.getDouble("HourlySalary");
                double dailyRate = rs.getDouble("DailyRate");
                double dailyWorkingHours = rs.getDouble("DailyWorkingHours");
                double totalUtilization = rs.getDouble("TotalUtilization");
                double fixedAmount = rs.getDouble("FixedAmount");
                double effectiveHours = rs.getDouble("EffectiveWorkingHours");
                double overheadMultiplier = rs.getDouble("OverheadMultiplierPercentage");

                Profile profile = new Profile(profileId, fName, lName, overheadCost, annualSalary, hourlyRate, dailyRate, dailyWorkingHours, totalUtilization, effectiveHours, overheadMultiplier);
                profile.setFixedAmount(fixedAmount);  // Ensure this line is included

                allProfiles.add(profile);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to read all profiles", e);
        }
        return allProfiles;
    }



    //Saves a new profile to the database.
    @Override
    public void createProfile(Profile newProfile) throws ApplicationWideException {
        String sqlProfile = "INSERT INTO dbo.Profile (Fname, Lname, AnualSalary, HourlySalary, " +
                "DailyRate, Overheadcost, FixedAmount, DailyWorkingHours, EffectiveWorkingHours, OverheadMultiplierPercentage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlProfileRole = "INSERT INTO dbo.ProfileProfileRole (ProfileId, ProfileRoleId) VALUES (?, ?)";

        try (Connection conn = dbConnector.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement pstmtProfile = conn.prepareStatement(sqlProfile, Statement.RETURN_GENERATED_KEYS)) {

                pstmtProfile.setString(1, newProfile.getFName());
                pstmtProfile.setString(2, newProfile.getLName());
                pstmtProfile.setDouble(3, newProfile.getAnnualSalary());
                pstmtProfile.setDouble(4, newProfile.getHourlySalary());
                pstmtProfile.setDouble(5, newProfile.getDailyRate());
                pstmtProfile.setBoolean(6, newProfile.isOverheadCost());
                pstmtProfile.setDouble(7, newProfile.getFixedAmount());
                pstmtProfile.setDouble(8, newProfile.getDailyWorkingHours());
                pstmtProfile.setDouble(9, newProfile.getEffectiveWorkingHours());
                pstmtProfile.setDouble(10, newProfile.getOverheadMultiplier());
                pstmtProfile.executeUpdate();


                try (ResultSet generatedKeys = pstmtProfile.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int profileId = generatedKeys.getInt(1);


                        try (PreparedStatement pstmtProfileRole = conn.prepareStatement(sqlProfileRole)) {
                            for (ProfileRole role : newProfile.getProfileRoles()) {
                                pstmtProfileRole.setInt(1, profileId);
                                pstmtProfileRole.setInt(2, role.getProfileRoleId());
                                pstmtProfileRole.addBatch();
                            }
                            pstmtProfileRole.executeBatch();
                        }
                    } else {
                        throw new ApplicationWideException("Creating profile failed, no ID obtained.");
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to create profile", e);
        }
    }

    @Override
    public boolean updateProfile(Profile profile) throws ApplicationWideException {
        String sqlProfile = "UPDATE Profile SET Fname = ?, Lname = ?, AnualSalary = ?, FixedAmount = ?, DailyWorkingHours = ?, HourlySalary = ?, DailyRate = ?, EffectiveWorkingHours = ?, OverheadMultiplierPercentage = ?  WHERE ProfileId = ?";

        try (Connection conn = dbConnector.getConnection()) {
            try (PreparedStatement pstmtProfile = conn.prepareStatement(sqlProfile)) {
                pstmtProfile.setString(1, profile.getFName());
                pstmtProfile.setString(2, profile.getLName());
                pstmtProfile.setDouble(3, profile.getAnnualSalary());
                pstmtProfile.setDouble(4, profile.getFixedAmount());
                pstmtProfile.setDouble(5, profile.getDailyWorkingHours());
                pstmtProfile.setDouble(6, profile.getHourlySalary());
                pstmtProfile.setDouble(7, profile.getDailyRate());
                pstmtProfile.setDouble(8, profile.getEffectiveWorkingHours());
                pstmtProfile.setDouble(9, profile.getOverheadMultiplier());
                pstmtProfile.setInt(10, profile.getProfileId());

                int affectedRows = pstmtProfile.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to update profile", e);
        }
    }


    @Override
    public void deleteProfile(int profileId) throws ApplicationWideException {
        String[] sqlQueries = {
                "DELETE FROM dbo.ProfileProfileRole WHERE ProfileId = ?",
                "DELETE FROM dbo.GeographyProfile WHERE ProfileId = ?",
                "DELETE FROM dbo.ProfileProjectTeams WHERE ProfileId_PPT = ?",
                "DELETE FROM dbo.Profile WHERE ProfileId = ?"
        };

        try (Connection conn = dbConnector.getConnection()) {
            conn.setAutoCommit(false);

            for (String sql : sqlQueries) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, profileId);
                    pstmt.executeUpdate();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to delete profile", e);
        }
    }

}

