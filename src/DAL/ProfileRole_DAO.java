package DAL;

import BE.ProfileRole;
import DAL.DBConnector.DBConnector;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    }
}
