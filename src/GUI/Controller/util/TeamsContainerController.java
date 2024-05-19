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
        Map<Integer, Geography> geographyMap = fetchGeographyMap();
        List<ProjectTeam> teams = fetchAllProjectTeams();
        teams.forEach(team -> createAndStoreTeamVBox(team, geographyMap));
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

    private void createAndStoreTeamVBox(ProjectTeam team, Map<Integer, Geography> geographyMap) {
        Geography geography = geographyMap.get(team.getGeographyId());
        if (geography == null) {
            System.out.println("Geography not found for team ID: " + team.getTeamId() + " with geography ID: " + team.getGeographyId());
        }
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
        //System.out.println("kakao" +geography);
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
        System.out.println("ost" + geography);
        TeamsController.getInstance().showTeamDetails(team, geography);

    }


    public VBox getTeamVBox(int teamId) {
        return teamBoxes.get(teamId);
    }


}

