package GUI.Model;

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
                        .map(BE.Team::getTeamName)
                        .sorted()
                        .collect(Collectors.toList())
        );
        //System.out.println(teamNames);
        return teamNames;
    }
}
