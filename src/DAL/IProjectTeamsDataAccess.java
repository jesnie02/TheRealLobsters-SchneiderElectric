package DAL;

import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;

import java.util.List;

public interface IProjectTeamsDataAccess {

    void addProfileToTeam(ProjectTeam projectTeam) throws ApplicationWideException;

    void deleteTeam(ProjectTeam projectTeam) throws Exception;

    void updateTeam(ProjectTeam projectTeam) throws Exception;

    void removeProfileFromProjectTeam(int profileId, int projectTeamId) throws ApplicationWideException;

    double getProfileCostUtilizationForTeam(int profileId, int teamId) throws ApplicationWideException;
}
