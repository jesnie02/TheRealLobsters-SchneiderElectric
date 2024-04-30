package DAL;

import BE.Team;

import java.util.List;

public interface ITeamsDataAccess {
    List<Team> getAllProjectTeams() throws Exception;
}
