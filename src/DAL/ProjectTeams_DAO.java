package DAL;

import BE.Profile;
import BE.ProjectTeam;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectTeams_DAO implements IProjectTeamsDataAccess {

    private final DBConnector dbConnector;

    public ProjectTeams_DAO() throws IOException {
        dbConnector = new DBConnector();
    }


    @Override
    public List<ProjectTeam> getAllProjectTeams() throws Exception {

        List<ProjectTeam> allProjectTeams = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM ProjectTeams";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                ProjectTeam projectTeam = new ProjectTeam(
                        rs.getInt("TeamsId"),
                        rs.getString("TeamName")
                );
                allProjectTeams.add(projectTeam);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allProjectTeams;
    }

    @Override
    public void addProfileToTeam(ProjectTeam projectTeam) {
        String sql = "INSERT INTO ProjectTeams (TeamName) VALUES (?)";

        //String sql = "INSERT INTO ProjectTeams (TeamName, SumOfAnnualSalary, SumOfDailyRate, SumOfHourlyRate, AvgOfAnnualSalary, AvgOfDailyRate, AvgOfHourlyRate, NumberOfProfiles, CountryId) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projectTeam.getTeamName());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO: Handle exception
        }
    }
}

