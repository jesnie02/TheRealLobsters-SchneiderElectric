package DAL;

import BE.Team;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Teams_DAO implements ITeamsDataAccess {

    private final DBConnector dbConnector;

    public Teams_DAO() throws IOException {
        dbConnector = new DBConnector();
    }


    @Override
    public List<Team> getAllProjectTeams() throws Exception {

        List<Team> allTeams = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM ProjectTeams";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                Team team = new Team(
                        rs.getInt("TeamsId"),
                        rs.getString("TeamName")
                );
                allTeams.add(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allTeams;
    }
}
