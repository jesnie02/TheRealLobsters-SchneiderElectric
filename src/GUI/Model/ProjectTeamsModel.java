package GUI.Model;

import BE.Profile;
import BE.ProjectTeam;
import BLL.ProjectTeamsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectTeamsModel {

    private final ProjectTeamsManager projectTeamsManager;

    public ProjectTeamsModel() throws Exception {
        projectTeamsManager = new ProjectTeamsManager();
    }

    /**
     * Gets all project teams from the database.
     *
     * @return A list of all project teams.
     * @throws Exception
     */
    public ObservableList<String> getAllProjectTeams() throws Exception {
        ObservableList<String> teamNames = javafx.collections.FXCollections.observableArrayList(
                projectTeamsManager.getAllProjectTeams().stream()
                        .map(ProjectTeam::getTeamName)
                        .sorted()
                        .collect(Collectors.toList())
        );
        return teamNames;
    }

    public void addProfileToTeam(ProjectTeam projectTeam) throws Exception {
        projectTeamsManager.addProfileToTeam(projectTeam);
    }

    public ObservableList<String> getEveryProjectTeam() throws Exception {
        ObservableList<String> teamNames = javafx.collections.FXCollections.observableArrayList(
                projectTeamsManager.getEveryProjectTeam().stream()
                        .map(ProjectTeam::getTeamName)
                        .sorted()
                        .collect(Collectors.toList())
        );
        return teamNames;
    }



    public ObservableList<ProjectTeam> getAllProjectTeamsData() throws Exception {
        List<ProjectTeam> list = projectTeamsManager.getEveryProjectTeam();
        ObservableList<ProjectTeam> observableList = FXCollections.observableArrayList(list);
        return observableList;
    }

    public enum ProjectTeamRateType {
        AVGHOURLY,
        AVGDAILY
    }

    public double getRateForProjectTeam(String projectTeamName, ProjectTeamRateType rateType) throws Exception {
        ProjectTeam projectTeam = projectTeamsManager.getProjectTeamByName(projectTeamName);
        if (projectTeam == null) {
            throw new IllegalArgumentException("Project team not found");
        } else {
            switch (rateType) {
                case AVGHOURLY:
                    return projectTeam.getAvgHourlyRate();
                case AVGDAILY:
                    return projectTeam.getAvgDailyRate();
                default:
                    throw new IllegalArgumentException("Invalid rate type");
            }
        }
    }


    /**
     * Calculates the total annual salary for a list of profiles.
     *
     * @param profiles The list of profiles for which to calculate the total annual salary.
     * @return The total annual salary for the provided list of profiles.
     * @throws Exception If an error occurs during the calculation.
     */
    public double calculateTotalAnnualSalary(ObservableList<Profile> profiles) throws Exception {
        return projectTeamsManager.calculateTotalAnnualSalary(profiles);
    }


    /**
     * Calculates the total daily rate for a list of profiles.
     *
     * @param profiles The list of profiles for which to calculate the total daily rate.
     * @return The total daily rate for the provided list of profiles.
     * @throws Exception If an error occurs during the calculation.
     */
    public double calculateTotalDailyRate(ObservableList<Profile> profiles) throws Exception {
        return projectTeamsManager.calculateTotalDailyRate(profiles);
    }

    /**
     * Calculates the total hourly rate for a list of profiles.
     *
     * @param profiles The list of profiles for which to calculate the total hourly rate.
     * @return The total hourly rate for the provided list of profiles.
     * @throws Exception If an error occurs during the calculation.
     */
    public double calculateTotalHourlyRate(ObservableList<Profile> profiles) throws Exception {
        return projectTeamsManager.calculateTotalHourlyRate(profiles);
    }

    public ObservableList<Profile> getProfileForTeam(int teamId) {
        List<Profile> profiles = projectTeamsManager.getProcessedProfilesForTeam(teamId);
        return FXCollections.observableArrayList(profiles);
    }


    public double calculateAndSetHourlyRateWithUtilization(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile, double utilizationPercentage) {

        return projectTeamsManager.calculateAndSetHourlyRateWithUtilization(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile, utilizationPercentage);
    }

    /**
     * Calculates and sets the daily rate for a profile during creation.
     * @return The calculated daily rate.
     */
    public double calculateAndSetDailyRateWithUtilization(double dailyWorkingHours, double hourlyRate) {
        return projectTeamsManager.calculateAndSetDailyRateWithUtilization(dailyWorkingHours, hourlyRate);
    }




}