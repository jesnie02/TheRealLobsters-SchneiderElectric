package DAL;

import BE.Profile;
import BE.ProfileRole;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for handling Profile related data operations.
 * It communicates with the database to perform these operations.
 */
public class Profile_DAO implements IProfileDataAccess {

    private final DBConnector dbConnector;

    /**
     * Constructor for the Profile_DAO class.
     * It initializes the dbConnector variable with an instance of DBConnector.
     * @throws IOException if an I/O error occurs
     */
    public Profile_DAO() throws IOException {
        dbConnector = new DBConnector();
    }

    /**
     * Retrieves all profiles from the database.
     * @return A list of Profile objects.
     */
    @Override
    public List<Profile> getAllProfiles() {
        List<Profile> allProfiles = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM Profile";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                int profileId = rs.getInt("ProfileId");
                String fName = rs.getString("FName");
                String lName = rs.getString("LName");
                Boolean overheadCost = Boolean.parseBoolean(rs.getString("OverheadCost"));
                double annualSalary = rs.getDouble("AnualSalary");
                double hourlyRate = rs.getDouble("HourlySalary");
                double dailyRate = rs.getDouble("DailyRate");
                double dailyWorkingHours = rs.getDouble("DailyWorkingHours");
                Profile profile = new Profile(profileId, fName, lName, overheadCost, annualSalary, hourlyRate, dailyRate, dailyWorkingHours);
                allProfiles.add(profile);
                //System.out.println(allProfiles);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Handle exception
        }
        return allProfiles;
    }

    /**
     * Saves a new profile to the database.
     * @param newProfile The new profile to be saved.
     */
    public void saveProfile(Profile newProfile) { //TODO: Skal nok hedde createProfile i stedet for saveProfile
        String sqlProfile = "INSERT INTO dbo.Profile (Fname, Lname, AnualSalary, HourlySalary, " +
                "DailyRate, Overheadcost, FixedAmount, DailyWorkingHours) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlProfileRole = "INSERT INTO dbo.ProfileProfileRole (ProfileId, ProfileRoleId) VALUES (?, ?)";

        try (Connection conn = dbConnector.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtProfile = conn.prepareStatement(sqlProfile, Statement.RETURN_GENERATED_KEYS)) {
                // Save Profile
                pstmtProfile.setString(1, newProfile.getFName());
                pstmtProfile.setString(2, newProfile.getLName());
                pstmtProfile.setDouble(3, newProfile.getAnnualSalary());
                pstmtProfile.setDouble(4, newProfile.getHourlySalary());
                pstmtProfile.setDouble(5, newProfile.getDailyRate());
                pstmtProfile.setBoolean(6, newProfile.isOverheadCost());
                pstmtProfile.setDouble(7, newProfile.getFixedAmount());
                pstmtProfile.setDouble(8, newProfile.getDailyWorkingHours());
                pstmtProfile.executeUpdate();

                // Retrieve generated Profile ID
                try (ResultSet generatedKeys = pstmtProfile.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int profileId = generatedKeys.getInt(1);

                        // Save ProfileRole entries
                        try (PreparedStatement pstmtProfileRole = conn.prepareStatement(sqlProfileRole)) {
                            for (ProfileRole role : newProfile.getProfileRoles()) {
                                pstmtProfileRole.setInt(1, profileId);
                                pstmtProfileRole.setInt(2, role.getProfileRoleId());
                                pstmtProfileRole.addBatch();
                            }
                            pstmtProfileRole.executeBatch();
                        }
                    } else {
                        throw new SQLException("Creating profile failed, no ID obtained.");
                    }
                }

                // Commit transaction
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace(); //TODO: Handle exception
        }
    }

}

