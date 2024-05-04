package DAL;

import BE.Country;
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

    public List<ProjectTeam> getEveryProjectTeam(){
        List<ProjectTeam> everyProjectTeam = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

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
                ProjectTeam projectTeam = new ProjectTeam(id, teamName, sumOfHourlyRate, sumOfDailyRate, avgDailyRate, avgHourlyRate, sumOfAnnualSalary, avgAnnualSalary);
                everyProjectTeam.add(projectTeam);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Handle exception
        }
        return everyProjectTeam;
    }

    @Override
    public void addProfileToTeam(ProjectTeam projectTeam) {
        String sql = "INSERT INTO ProjectTeams (TeamName, CountryId, NumberOfProfiles, AvgOfAnnualSalary, SumOfAnnualSalary, AvgOfHourlyRate, SumOfHourlyRate, AvgOfDailyRate, SumOfDailyRate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projectTeam.getTeamName());
            pstmt.setInt(2, projectTeam.getCountry().getCountryId());
            pstmt.setInt(3, projectTeam.getProfiles().size());
            pstmt.setDouble(4, projectTeam.getAvgAnnualSalary());
            pstmt.setDouble(5, projectTeam.getSumOfAnnualSalary());
            pstmt.setDouble(6, projectTeam.getAvgHourlyRate());
            pstmt.setDouble(7, projectTeam.getSumOfHourlyRate());
            pstmt.setDouble(8, projectTeam.getAvgDailyRate());
            pstmt.setDouble(9, projectTeam.getSumOfDailyRate());
            pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace(); //TODO: Handle exception
        }
    }
}

