package GUI.Model;

import BE.Profile;
import BE.ProjectTeam;
import BLL.ProjectTeamsManager;
import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectTeamsModel {

    private final ProjectTeamsManager projectTeamsManager;

    public ProjectTeamsModel() throws ApplicationWideException {
        projectTeamsManager = new ProjectTeamsManager();
    }


    public ObservableList<String> getAllProjectTeams() {
        ObservableList<String> teamNames = null;
        try {
            teamNames = FXCollections.observableArrayList(
                    projectTeamsManager.getAllProjectTeams().stream()
                            .map(ProjectTeam::getTeamName)
                            .sorted()
                            .collect(Collectors.toList())
            );
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return teamNames;
    }

    public void addProfileToTeam(ProjectTeam projectTeam) {
        try {
            projectTeamsManager.addProfileToTeam(projectTeam);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    public ObservableList<String> getEveryProjectTeam() {
        ObservableList<String> teamNames = null;
        try {
            teamNames = FXCollections.observableArrayList(
                    projectTeamsManager.getEveryProjectTeam().stream()
                            .map(ProjectTeam::getTeamName)
                            .sorted()
                            .collect(Collectors.toList())
            );
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return teamNames;
    }



    public ObservableList<ProjectTeam> getAllProjectTeamsData() {
        List<ProjectTeam> list = null;
        try {
            list = projectTeamsManager.getEveryProjectTeam();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        ObservableList<ProjectTeam> observableList = FXCollections.observableArrayList(list);
        return observableList;
    }

    public ObservableList<ProjectTeam> getTop10ProjectTeamsByAnnualSalary() {
        List<ProjectTeam> list = null;
        try {
            list = projectTeamsManager.getTop10ProjectTeamsByAnnualSalary();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        ObservableList<ProjectTeam> observableList = FXCollections.observableArrayList(list);
        return observableList;
    }

    public enum ProjectTeamRateType {
        AVGHOURLY,
        AVGDAILY
    }

    public double getRateForProjectTeam(String projectTeamName, ProjectTeamRateType rateType) {
        ProjectTeam projectTeam = null;
        try {
            projectTeam = projectTeamsManager.getProjectTeamByName(projectTeamName);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }

        if (rateType == ProjectTeamRateType.AVGHOURLY)
            return projectTeam.getAvgHourlyRate();

        else return projectTeam.getAvgDailyRate();
    }

    public double calculateTotalAnnualSalary(ObservableList<Profile> profiles) {
        return projectTeamsManager.calculateTotalAnnualSalary(profiles);
    }



    public double calculateTotalDailyRate(ObservableList<Profile> profiles)  {
        return projectTeamsManager.calculateTotalDailyRate(profiles);
    }


    public double calculateTotalHourlyRate(ObservableList<Profile> profiles) {
        return projectTeamsManager.calculateTotalHourlyRate(profiles);
    }

    public ObservableList<Profile> getProfileForTeam(int teamId) {
        List<Profile> profiles = null;
        try {
            profiles = projectTeamsManager.getProcessedProfilesForTeam(teamId);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return FXCollections.observableArrayList(profiles);
    }

    public double calculateAndSetHourlyRateWithUtilization(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile, double utilizationPercentage) {

        return projectTeamsManager.calculateAndSetHourlyRateWithUtilization(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile, utilizationPercentage);
    }


    public double calculateAndSetDailyRateWithUtilization(double dailyWorkingHours, double hourlyRate) {
        return projectTeamsManager.calculateAndSetDailyRateWithUtilization(dailyWorkingHours, hourlyRate);
    }

    public void deleteTeam(ProjectTeam projectTeam) {
        try {
            projectTeamsManager.deleteTeam(projectTeam);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void updateTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        projectTeamsManager.updateTeam(projectTeam);
    }

    public void removeProfileFromTeam(int projectTeam, int profile) {
        try {
            projectTeamsManager.removeProfileFromProjectTeam(projectTeam, profile);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }
}