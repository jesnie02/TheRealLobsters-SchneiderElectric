package BLL;

import BE.Profile;
import BE.ProfileRole;
import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;
import DAL.ProjectTeams_DAO;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectTeamsManager {

    private final ProjectTeams_DAO teamsDAO;
    private ICalculateManager iCalculateManager;

    public ProjectTeamsManager() throws IOException {
        teamsDAO = new ProjectTeams_DAO();
        iCalculateManager = new CalculatorManager();
    }

    /**
     * Gets all project teams from the database.
     *
     * @return A list of all project teams.
     * @throws Exception
     */
    public List<ProjectTeam> getAllProjectTeams() throws Exception {
        return teamsDAO.getAllProjectTeams();
    }

    public void addProfileToTeam(ProjectTeam projectTeam) throws Exception {
        projectTeam.setAvgAnnualSalary(iCalculateManager.avgAnnualSalary(projectTeam.getProfiles()));
        projectTeam.setSumOfAnnualSalary(iCalculateManager.annualSalaryWithFixedAmount(projectTeam.getProfiles()));
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
        return iCalculateManager.annualSalaryWithFixedAmount(profiles);
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

    public List<Profile> getProcessedProfilesForTeam(int teamId) {
        List<Profile> rawProfiles = teamsDAO.getProfileFromProjectTeam(teamId);
        return processProfiles(rawProfiles);
    }

    private List<Profile> processProfiles(List<Profile> rawProfiles) {
        for (Profile profile : rawProfiles) {
            List<ProfileRole> roles = parseRoles(profile.getRolesString());  // Assume getter for roles string
            profile.setProfileRoles(roles);
        }
        return rawProfiles;
    }

    private List<ProfileRole> parseRoles(String rolesString) {
        List<ProfileRole> roles = new ArrayList<>();
        if (rolesString != null && !rolesString.isEmpty()) {
            String[] roleDescriptions = rolesString.split(", ");
            for (String desc : roleDescriptions) {
                roles.add(new ProfileRole(desc));
            }
        }
        return roles;
    }


    public double calculateAndSetDailyRateWithUtilization(double dailyWorkingHours, double hourlyRate) {
        return iCalculateManager.calculateAndSetDailyRateWithUtilization(dailyWorkingHours, hourlyRate);
    }

    public double calculateAndSetHourlyRateWithUtilization(double annualSalaryProfile, double overheadMultiplierProfile, double annualFixedAmountProfile, double effectiveHoursProfile, double utilizationPercentage) {
        return iCalculateManager.calculateAndSetHourlyRateWithUtilization(annualSalaryProfile, overheadMultiplierProfile, annualFixedAmountProfile, effectiveHoursProfile, utilizationPercentage);
    }

    public List<ProjectTeam> getTop10ProjectTeamsByAnnualSalary() throws Exception {
        List<ProjectTeam> projectTeams = teamsDAO.getEveryProjectTeam();
        return projectTeams.stream()
                .sorted(Comparator.comparingDouble(ProjectTeam::getSumOfAnnualSalary).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public void deleteTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        teamsDAO.deleteTeam(projectTeam);
    }
}