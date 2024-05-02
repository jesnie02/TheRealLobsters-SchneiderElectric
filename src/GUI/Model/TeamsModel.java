package GUI.Model;

import BE.ProjectTeam;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class TeamsModel {

    private final BLL.TeamsManager TeamsManager;

    public TeamsModel() throws Exception {
        TeamsManager = new BLL.TeamsManager();
    }

    public ObservableList<String> getAllProjectTeams() throws Exception {
        ObservableList<String> teamNames = javafx.collections.FXCollections.observableArrayList(
                TeamsManager.getAllProjectTeams().stream()
                        .map(ProjectTeam::getTeamName)
                        .sorted()
                        .collect(Collectors.toList())
        );
        return teamNames;
    }
}
