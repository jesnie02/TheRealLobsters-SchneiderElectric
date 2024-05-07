package DAL;

import BE.Profile;
import DAL.DBConnector.DBConnector;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Profile_DAO implements IProfileDataAccess {

    private final DBConnector dbConnector;

    public Profile_DAO() throws IOException {
        dbConnector = new DBConnector();
    }
    @Override
    public List<Profile> getAllProfiles() {
        List<Profile> allProfiles = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM Profile";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                int profileId = rs.getInt("ProfileId");
                int country = rs.getInt("Country");
                String projectTeam = rs.getString("ProjectTeams");
                String projectRole = rs.getString("ProjectRole");
                String fName = rs.getString("FName");
                String lName = rs.getString("LName");
                Boolean overheadCost = Boolean.parseBoolean(rs.getString("OverheadCost"));
                double annualSalary = rs.getDouble("AnualSalary");
                double hourlyRate = rs.getDouble("HourlySalary");
                double dailyRate = rs.getDouble("DailyRate");
                double dailyWorkingHours = rs.getDouble("DailyWorkingHours");
                Profile profile = new Profile(profileId, country, projectTeam, Profile.ProjectRole.valueOf(projectRole), fName, lName, overheadCost, annualSalary, hourlyRate, dailyRate, dailyWorkingHours);
                allProfiles.add(profile);
                //System.out.println(allProfiles);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Handle exception
        }
        return allProfiles;
    }

    /**
     * This method saves a new profile to the database. useing the Profile object from the GUI.
     * SQL statement below is used to save the data in the database table profile.
     */
    public void saveProfile(Profile newProfile) {
        String sql = "INSERT INTO dbo.Profile (Fname, Lname, AnualSalary, Country, ProjectRole, HourlySalary, DailyRate, Overheadcost, FixedAmount, DailyWorkingHours) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = dbConnector.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newProfile.getfName());
            pstmt.setString(2, newProfile.getlName());
            pstmt.setDouble(3, newProfile.getAnnualSalary());
            pstmt.setInt(4, newProfile.getCountryId());
            pstmt.setString(5, newProfile.getProjectRole().name());
            pstmt.setDouble(6, newProfile.getHourlySalary());
            pstmt.setDouble(7, newProfile.getDailyRate());
            pstmt.setBoolean(8, newProfile.isOverheadCost());
            pstmt.setDouble(9, newProfile.getFixedAmount());
            pstmt.setDouble(10, newProfile.getDailyWorkingHours());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO: Handle exception
        }
    }
}

