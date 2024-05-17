package DAL;

import BE.ProjectTeam;

import java.util.List;

public interface IProjectTeamsDataAccess {

    List<ProjectTeam> getAllProjectTeams() throws Exception;
    void addProfileToTeam(ProjectTeam projectTeam);

    void deleteTeam(ProjectTeam projectTeam) throws Exception;

}
