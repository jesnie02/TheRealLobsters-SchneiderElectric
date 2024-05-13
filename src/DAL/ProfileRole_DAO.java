package DAL;

import BE.ProfileRole;
import DAL.DBConnector.DBConnector;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileRole_DAO implements IProfileRoleDataAccess{

    private final DBConnector dbConnector;

    public ProfileRole_DAO() throws IOException {
        dbConnector = new DBConnector();
    }

    @Override
    public List<ProfileRole> getAllProfileRoles() {
        List<ProfileRole> allProfileRoles = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM ProfileRole";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt("ProfileRoleId");
                String type = rs.getString("ProfileRoleType");
                ProfileRole profileRole = new ProfileRole(id, type);
                allProfileRoles.add(profileRole);
                //System.out.println(allProfileRoles);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace(); //TODO exception
        }
        return allProfileRoles;
    }

    @Override
    public void createProfileRole(ProfileRole profileRole) {
        String sql = "INSERT INTO ProfileRole (ProfileRoleType) VALUES (?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, profileRole.getProfileRoleType());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // TODO: Handle this exception
        }
    }
}
