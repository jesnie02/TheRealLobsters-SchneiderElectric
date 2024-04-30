package DAL;

import BE.Team;

import java.util.List;

public interface ITeamsDataAccess {
    List<Team> getAllTeams() throws Exception;
}
