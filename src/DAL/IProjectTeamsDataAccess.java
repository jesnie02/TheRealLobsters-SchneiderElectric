package DAL;

import BE.ProjectTeam;

import java.util.List;

public interface IProjectTeamsDataAccess {
    List<ProjectTeam> getAllProjectTeams() throws Exception;
}
