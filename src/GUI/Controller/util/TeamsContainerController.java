package GUI.Controller.util;

import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import BE.ProjectTeam;
import BE.Geography;
import GUI.Model.GeographyModel;
import GUI.Controller.TeamsController;
import GUI.Model.ProjectTeamsModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamsContainerController {

    private ProjectTeamsModel projectTeamsModel;
    private GeographyModel geographyModel;
    private Map<Integer, VBox> teamBoxes = new HashMap<>();
    private Map<Integer, Geography> geographyMap = new HashMap<>();

    public TeamsContainerController() {
        initializeModels();
    }

    private void initializeModels() {
        try {
            projectTeamsModel = new ProjectTeamsModel();
            geographyModel = new GeographyModel();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    // Initialize method to create VBox for each team
    public void initialize() {
        geographyMap = fetchGeographyMap();
        List<ProjectTeam> teams = fetchAllProjectTeams();
        teams.forEach(team -> {
            Geography geography = geographyMap.get(team.getGeographyId());
            team.setGeography(geography);  // Set the geography for the team
            createAndStoreTeamVBox(team, geography);
        });
    }

    private Map<Integer, Geography> fetchGeographyMap() {
        try {
            return geographyModel.getGeographyMap();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
            return new HashMap<>();
        }
    }

    private List<ProjectTeam> fetchAllProjectTeams() {
        return projectTeamsModel.getAllProjectTeamsData();
    }

    private void createAndStoreTeamVBox(ProjectTeam team, Geography geography) {
        VBox teamBox = createTeamVBox(team, geography);
        teamBoxes.put(team.getTeamId(), teamBox);
    }

    private VBox createTeamVBox(ProjectTeam team, Geography geography) {
        VBox vbox = new VBox(10);
        vbox.getStyleClass().add("team-vbox");

        // Labels creation with null-safe geography name retrieval
        Label lblTeamName = createLabel(team.getTeamName(), "top-label");
        String geoName = geography != null ? geography.getGeographyName() : "Unknown";

        Label lblGeography = createLabel("Location: " + geoName, "other-label");
        Label lblMembers = createLabel("Members: " + team.getNumberOfProfiles(), "other-label");
        Label lblCost = createLabel("Average Cost: " + String.format("%.2f", team.getAvgAnnualSalary()), "other-label");
        vbox.getChildren().addAll(lblTeamName, lblGeography, lblMembers, lblCost);
        vbox.setOnMouseClicked(event -> openTeamDetailView(team, geography));
        return vbox;
    }

    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        if (styleClass != null) {
            label.getStyleClass().add(styleClass);
        }
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private void openTeamDetailView(ProjectTeam team, Geography geography) {
        TeamsController.getInstance().showTeamDetails(team, geography);
    }

    public VBox getTeamVBox(int teamId) {
        return teamBoxes.get(teamId);
    }

    public Geography getGeographyForTeam(ProjectTeam team) {
        return geographyMap.get(team.getGeographyId());
    }

    // New method to add a project team dynamically
    public void addProjectTeam(ProjectTeam team) {
        Geography geography = geographyMap.get(team.getGeographyId());
        team.setGeography(geography);  // Set the geography for the team
        createAndStoreTeamVBox(team, geography);
    }
}
