package DAL;

import BE.ProfileRole;
import CustomExceptions.ApplicationWideException;
import DAL.DBConnector.DBConnector;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileRole_DAO implements IProfileRoleDataAccess{

    private final DBConnector dbConnector;

    public ProfileRole_DAO() throws ApplicationWideException {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new ApplicationWideException("Failed to initialize the database connector",e);
        }
    }

    @Override
    public List<ProfileRole> getAllProfileRoles() throws ApplicationWideException {
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
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to get all profile roles",e);
        }
        return allProfileRoles;
    }

    @Override
    public void createProfileRole(ProfileRole profileRole) throws ApplicationWideException {
        String sql = "INSERT INTO ProfileRole (ProfileRoleType) VALUES (?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, profileRole.getProfileRoleType());
            pstmt.executeUpdate();

    } catch (SQLException e) {
            throw new ApplicationWideException("Failed to create profile role",e);
        }
    }}
