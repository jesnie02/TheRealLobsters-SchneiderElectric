package BLL;

import BE.Profile;
import BE.ProjectTeam;
import DAL.ProjectTeams_DAO;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class ProjectTeamsManager {

    private final ProjectTeams_DAO teamsDAO;
    private ICalculateManager iCalculateManager;

    public ProjectTeamsManager() throws IOException {
        teamsDAO = new ProjectTeams_DAO();
        iCalculateManager = new CalculatorManager();
    }

    public List<ProjectTeam> getAllProjectTeams() throws Exception {
        return teamsDAO.getAllProjectTeams();
    }

    public void addProfileToTeam(ProjectTeam projectTeam) throws Exception {
        projectTeam.setAvgAnnualSalary(iCalculateManager.avgAnnualSalary(projectTeam.getProfiles()));
        projectTeam.setSumOfAnnualSalary(iCalculateManager.annualsalary(projectTeam.getProfiles()));
        projectTeam.setAvgHourlyRate(iCalculateManager.avgHourlyRate(projectTeam.getProfiles()));
        projectTeam.setSumOfHourlyRate(iCalculateManager.sumOfHourlyRate(projectTeam.getProfiles()));
        projectTeam.setAvgDailyRate(iCalculateManager.avgDailyRate(projectTeam.getProfiles()));
        projectTeam.setSumOfDailyRate(iCalculateManager.sumOfDailyRate(projectTeam.getProfiles()));
        teamsDAO.addProfileToTeam(projectTeam);
    }

    public List<ProjectTeam>getEveryProjectTeam() throws Exception{
        return teamsDAO.getEveryProjectTeam();
    }

    public ProjectTeam getProjectTeamByName(String team) throws Exception {
        for (ProjectTeam projectTeam : getEveryProjectTeam()) {
            if (projectTeam.getTeamName().equals(team)) {
                return projectTeam;
            }
        }
        return null;
    }

    public double getSumOfAnnualSalaryForTeam(double annualSalary, double fixedAmount){
        return iCalculateManager.getHourlyRateWithMultiplier(annualSalary, fixedAmount);

    }

    /**
     * Calculates the total annual salary for a list of profiles.
     *
     * @param profiles The list of profiles for which to calculate the total annual salary.
     * @return The total annual salary for the provided list of profiles.
     */
    public double calculateTotalAnnualSalary(List<Profile> profiles) {
        return iCalculateManager.annualsalary(profiles);
    }

    /**
     * Calculates the total daily rate for a list of profiles.
     *
     * @param profiles The list of profiles for which to calculate the total daily rate.
     * @return The total daily rate for the provided list of profiles.
     */
    public double calculateTotalDailyRate(List<Profile> profiles) {
        return iCalculateManager.sumOfDailyRate(profiles);
    }

    /**
     * Calculates the total hourly rate for a list of profiles.
     *
     * @param profiles The list of profiles for which to calculate the total hourly rate.
     * @return The total hourly rate for the provided list of profiles.
     */
    public double calculateTotalHourlyRate(List<Profile> profiles) {
        return iCalculateManager.sumOfHourlyRate(profiles);
    }
}