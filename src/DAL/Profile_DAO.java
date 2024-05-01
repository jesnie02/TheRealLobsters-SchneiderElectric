package DAL;

import BE.Profile;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Profile_DAO {

    private DBConnector dbConnector ;



    public Profile_DAO() throws IOException {
       dbConnector = new DBConnector();
    }

    public void saveProfile(Profile newProfile) {
        String sql = "INSERT INTO dbo.Profile (Fname, Lname, AnualSalary, Country, ProjectRole, HourlySalary, DailyRate, Overheadcost) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = dbConnector.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newProfile.getFirstName());
            pstmt.setString(2, newProfile.getLastName());
            pstmt.setDouble(3, newProfile.getAnnualSalary());
            pstmt.setInt(4, newProfile.getCountryId());
            pstmt.setString(5, newProfile.getProjectRole().name());
            pstmt.setInt(6, newProfile.getHourlyResult());
            pstmt.setInt(7, newProfile.getDailyResult());
            pstmt.setBoolean(8, newProfile.isOverheadCost());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO: Handle exception
        }
    }
    }

