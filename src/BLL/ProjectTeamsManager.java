package BLL;

import BE.ProjectTeam;
import DAL.ProjectTeams_DAO;

import java.io.IOException;
import java.util.List;

public class ProjectTeamsManager {

    private final ProjectTeams_DAO teamsDAO;

    public ProjectTeamsManager() throws IOException {
        teamsDAO = new ProjectTeams_DAO();
    }

    public List<ProjectTeam> getAllProjectTeams() throws Exception {
        return teamsDAO.getAllProjectTeams();
    }

    public void addProfileToTeam(ProjectTeam projectTeam) throws Exception {
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
}
