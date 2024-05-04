package GUI.Model;

import BE.ProjectTeam;
import BLL.ProjectTeamsManager;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class ProjectTeamsModel {

    private final ProjectTeamsManager projectTeamsManager;

    public ProjectTeamsModel() throws Exception {
        projectTeamsManager = new ProjectTeamsManager();
    }

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



}