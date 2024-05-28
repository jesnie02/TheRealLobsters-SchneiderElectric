package BLL;

import BE.Profile;
import BE.ProfileRole;
import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;
import DAL.ProjectTeams_DAO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectTeamsManager {

    private final ProjectTeams_DAO teamsDAO;
    private ICalculateManager iCalculateManager;

    public ProjectTeamsManager() throws ApplicationWideException {
        teamsDAO = new ProjectTeams_DAO();
        iCalculateManager = new CalculatorManager();
    }


    public void addProfileToTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        double avgAnnualSalary = iCalculateManager.avgAnnualSalary(projectTeam);
        projectTeam.setAvgAnnualSalary(avgAnnualSalary);
        double avgHourlyRate = iCalculateManager.avgHourlyRate(projectTeam);
        projectTeam.setAvgHourlyRate(avgHourlyRate);
        double avgDailyRate = iCalculateManager.avgDailyRate(projectTeam);
        projectTeam.setAvgDailyRate(avgDailyRate);
        teamsDAO.addProfileToTeam(projectTeam);
    }

    public List<ProjectTeam>getEveryProjectTeam() throws ApplicationWideException{
        return teamsDAO.getEveryProjectTeam();
    }

    public ProjectTeam getProjectTeamByName(String team) throws ApplicationWideException {
        for (ProjectTeam projectTeam : getEveryProjectTeam()) {
            if (projectTeam.getTeamName().equals(team)) {
                return projectTeam;
            }
        }
        return null;
    }

    public double getProfileCostUtilizationForTeam(int profileId, int teamId) throws ApplicationWideException {
        return teamsDAO.getProfileCostUtilizationForTeam(profileId, teamId);
    }

    public List<Profile> getProcessedProfilesForTeam(int teamId) throws ApplicationWideException {
        List<Profile> rawProfiles = teamsDAO.getProfileFromProjectTeam(teamId);
        return processProfiles(rawProfiles);
    }

    private List<Profile> processProfiles(List<Profile> rawProfiles) {
        for (Profile profile : rawProfiles) {
            List<ProfileRole> roles = parseRoles(profile.getRolesString());
            profile.setProfileRoles(roles);
        }
        return rawProfiles;
    }

    private List<ProfileRole> parseRoles(String rolesString) {
        List<ProfileRole> roles = new ArrayList<>();
        if (rolesString != null && !rolesString.isEmpty()) {
            String[] roleDescriptions = rolesString.split(", ");
            for (String desc : roleDescriptions) {
                roles.add(new ProfileRole(desc));
            }
        }
        return roles;
    }

    public List<ProjectTeam> getTop10ProjectTeamsByAnnualSalary() throws ApplicationWideException {
        List<ProjectTeam> projectTeams = teamsDAO.getEveryProjectTeam();
        return projectTeams.stream()
                .sorted(Comparator.comparingDouble(ProjectTeam::getSumOfAnnualSalary).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public void deleteTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        teamsDAO.deleteTeam(projectTeam);
    }

    public void updateTeam(ProjectTeam projectTeam) throws ApplicationWideException {
        teamsDAO.updateTeam(projectTeam);
    }

    public void removeProfileFromProjectTeam(int projectTeam, int profile) throws ApplicationWideException {
        teamsDAO.removeProfileFromProjectTeam(projectTeam, profile);
    }

}