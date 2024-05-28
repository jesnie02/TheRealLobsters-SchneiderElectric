package GUI.Model;

import BE.Profile;
import BE.ProjectTeam;
import BLL.ProjectTeamsManager;
import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.util.List;
import java.util.stream.Collectors;

public class ProjectTeamsModel {

    private final ProjectTeamsManager projectTeamsManager;

    public enum ProjectTeamRateType {
        AVGHOURLY,
        AVGDAILY
    }

    public ProjectTeamsModel() throws ApplicationWideException {
        projectTeamsManager = new ProjectTeamsManager();
    }


    public void addProfileToTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        projectTeamsManager.addProfileToTeam(projectTeam);
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

    public ObservableList<Profile> getProfileForTeam(int teamId) {
        List<Profile> profiles = null;
        try {
            profiles = projectTeamsManager.getProcessedProfilesForTeam(teamId);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return FXCollections.observableArrayList(profiles);
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

    public double getProfileCostUtilizationForTeam(int profileId, int teamId) throws ApplicationWideException {
        return projectTeamsManager.getProfileCostUtilizationForTeam(profileId, teamId);
    }

    public void removeProfileFromTeam(int projectTeam, int profile) {
        try {
            projectTeamsManager.removeProfileFromProjectTeam(projectTeam, profile);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

}