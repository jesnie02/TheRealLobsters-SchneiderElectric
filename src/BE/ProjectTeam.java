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

    public ProjectTeam(int teamId, String teamName, double sumOfHourlyRate, double sumOfDailyRate, double avgDailyRate, double avgHourlyRate, double sumOfAnnualSalary, double avgAnnualSalary) {
        TeamId = teamId;
        TeamName = teamName;
        this.sumOfHourlyRate = sumOfHourlyRate;
        this.sumOfDailyRate = sumOfDailyRate;
        this.avgDailyRate = avgDailyRate;
        this.avgHourlyRate = avgHourlyRate;
        this.sumOfAnnualSalary = sumOfAnnualSalary;
        this.avgAnnualSalary = avgAnnualSalary;
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

    public double getSumOfHourlyRate() {
        return sumOfHourlyRate;
    }

    public double getSumOfDailyRate() {
        return sumOfDailyRate;
    }

    public double getAvgDailyRate() {
        return avgDailyRate;
    }

    public void setAvgDailyRate(double avgDailyRate) {
        this.avgDailyRate = avgDailyRate;
    }

    public double getAvgHourlyRate() {
        return avgHourlyRate;
    }

    public void setAvgHourlyRate(double avgHourlyRate) {
        this.avgHourlyRate = avgHourlyRate;
    }

    @Override
    public String toString() {
        return TeamName;
    }
}
