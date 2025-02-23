package BE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectTeam {

    private String TeamName;
    private double sumOfHourlyRate, sumOfDailyRate, avgDailyRate, avgHourlyRate, sumOfAnnualSalary, avgAnnualSalary;
    private int geographyId, numberOfProfiles, TeamId, count;

    private List<Profile> profiles = new ArrayList<>();
    private Map<Profile, Double> utilizationsMap = new HashMap<>();
    private Map<Profile, Double> utilizationCostsMap = new HashMap<>();

    private Country country;
    private Geography geography;

    public ProjectTeam(String name) {
        this.TeamName = name;
    }


    public ProjectTeam(int id, String name) {
        this.TeamId = id;
        this.TeamName = name;
    }

    public int getNumberOfProfiles() {
        return numberOfProfiles;
    }

    public void setNumberOfProfiles(int numberOfProfiles) {
        this.numberOfProfiles = numberOfProfiles;
    }



    public ProjectTeam(int teamId, String teamName, double sumOfHourlyRate, double sumOfDailyRate, double avgDailyRate, double avgHourlyRate, double sumOfAnnualSalary, double avgAnnualSalary, int numberOfProfiles, int geographyId) {
        TeamId = teamId;
        TeamName = teamName;
        this.sumOfHourlyRate = sumOfHourlyRate;
        this.sumOfDailyRate = sumOfDailyRate;
        this.avgDailyRate = avgDailyRate;
        this.avgHourlyRate = avgHourlyRate;
        this.sumOfAnnualSalary = sumOfAnnualSalary;
        this.avgAnnualSalary = avgAnnualSalary;
        this.numberOfProfiles = numberOfProfiles;
        this.geographyId = geographyId;
    }

    //For testing
    public ProjectTeam (List<Profile> profiles){
        this.profiles = profiles;
    }

    public int getGeographyId() {
        return geographyId;
    }

    public void setGeographyId(int geographyId) {
        this.geographyId = geographyId;
    }

    public List<Profile> getProfiles() {
        return new ArrayList<>(profiles);
    }

    public void setProfiles(List<Profile> profiles) {
        if (profiles == null) {
            throw new IllegalArgumentException("Profiles list cannot be null");
        }
        this.profiles = new ArrayList<>(profiles);
    }

    public Map<Profile, Double> getUtilizationsMap(){
        return utilizationsMap;
    }

    public void setUtilizationsMap(Map<Profile, Double> utilizationsMap){
        this.utilizationsMap = utilizationsMap;
    }


    public Map<Profile, Double> getUtilizationCostMap() {
        return utilizationCostsMap;
    }

    public void setUtilizationCostMap(Map<Profile, Double> utilizationCostMap) {
        this.utilizationCostsMap = utilizationCostMap;
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

    public double getSumOfAnnualSalary() {
        return sumOfAnnualSalary;
    }

    public double getAvgAnnualSalary() {
        return avgAnnualSalary;
    }

    public void setSumOfAnnualSalary(double sumOfAnnualSalary) {
        this.sumOfAnnualSalary = sumOfAnnualSalary;
    }

    public double setAvgAnnualSalary(double avgAnnualSalary) {
        this.avgAnnualSalary = avgAnnualSalary;
        return this.avgAnnualSalary;
    }

    public void setSumOfHourlyRate(double sumOfHourlyRate) {
        this.sumOfHourlyRate = sumOfHourlyRate;
    }

    public void setSumOfDailyRate(double sumOfDailyRate) {
        this.sumOfDailyRate = sumOfDailyRate;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    public String toDetailedString() {
        return "ProjectTeam{" +
                "teamName='" + TeamName + '\'' +
                ", avgAnnualSalary=" + avgAnnualSalary +
                ", sumOfAnnualSalary=" + sumOfAnnualSalary +
                ", avgHourlyRate=" + avgHourlyRate +
                ", sumOfHourlyRate=" + sumOfHourlyRate +
                ", avgDailyRate=" + avgDailyRate +
                ", sumOfDailyRate=" + sumOfDailyRate +
                '}';
    }

    @Override
    public String toString() {
        return TeamName;
    }


    public void setGeography(Geography geography) {
            this.geography = geography;
    }

    public Geography getGeography() {
        return geography;
    }

    //-----------------------For testing-----------------------
    public double getSumOfHourlyRates() {
        return profiles.stream().mapToDouble(Profile::getHourlyRate).sum();
    }

    public double getSumOfDailyRates() {
        return profiles.stream().mapToDouble(Profile::getDailyRate).sum();
    }

    public double getSumOfAnnualSalaries() {
        return profiles.stream().mapToDouble(Profile::getAnnualSalary).sum();
    }
}