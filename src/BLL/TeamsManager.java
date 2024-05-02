package BLL;

import BE.ProjectTeam;
import DAL.ProjectTeams_DAO;

import java.io.IOException;
import java.util.List;

public class TeamsManager {

    private final ProjectTeams_DAO teamsDAO;

    public TeamsManager() throws IOException {
        teamsDAO = new ProjectTeams_DAO();
    }

    public List<ProjectTeam> getAllProjectTeams() throws Exception {
        return teamsDAO.getAllProjectTeams();
    }
}
