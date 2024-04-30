package BLL;

import BE.Team;

import java.io.IOException;
import java.util.List;

public class TeamsManager {

    private final DAL.Teams_DAO teamsDAO;

    public TeamsManager() throws IOException {
        teamsDAO = new DAL.Teams_DAO();
    }

    public List<Team> getAllProjectTeams() throws Exception {
        return teamsDAO.getAllProjectTeams();
    }
}
