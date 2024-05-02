package BE;

import java.util.List;

public class ProjectTeam {

    private int TeamId;
    private String TeamName;
    private double sumOfHourlyRate, sumOfDailyRate, avgDailyRate, avgHourlyRate, sumOfAnnualSalary, avgAnnualSalary;
    private int count;
    private List<Profile> profiles;



    public ProjectTeam(String name) {
        this.TeamName = name;
    }


    public ProjectTeam(int id, String name) {
        this.TeamId = id;
        this.TeamName = name;
    }

    public ProjectTeam(int teamId, String teamName, double sumOfHourlyRate, double sumOfDailyRate, double avgDailyRate, double avgHourlyRate, double sumOfAnnualSalary, double avgAnnualSalary, int count, List<Profile> profiles) {
        TeamId = teamId;
        TeamName = teamName;
        this.sumOfHourlyRate = sumOfHourlyRate;
        this.sumOfDailyRate = sumOfDailyRate;
        this.avgDailyRate = avgDailyRate;
        this.avgHourlyRate = avgHourlyRate;
        this.sumOfAnnualSalary = sumOfAnnualSalary;
        this.avgAnnualSalary = avgAnnualSalary;
        this.count = count;
        this.profiles = profiles;
    }



    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public int getTeamId() {
        return TeamId;
    }

    public void setTeamId(int teamId) {
        TeamId = teamId;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    @Override
    public String toString() {
        return TeamName;
    }
}
