package DAL;

import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;

import java.util.List;

public interface IProjectTeamsDataAccess {

    List<ProjectTeam> getAllProjectTeams() throws ApplicationWideException;
    void addProfileToTeam(ProjectTeam projectTeam) throws ApplicationWideException;

    void deleteTeam(ProjectTeam projectTeam) throws Exception;

    void updateTeam(ProjectTeam projectTeam) throws Exception;

}
