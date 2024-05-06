package BE;

import java.util.List;

public class Profile {

    private int profileId, countryId;
    private ProjectRole projectRole;
    private String projectTeam, fName, lName;
    private boolean overheadCost;
    private double annualSalary, hourlySalary, dailyRate, fixedAmount, dailyWorkingHours;
    private List<Country> country;
    private List<ProjectTeam> projectTeams;


    // For testing purposes
    public Profile(double annualSalary, double fixedAmount) {
        this.annualSalary = annualSalary;
        this.fixedAmount = fixedAmount;
    }

    public Profile(int profileId, int countryId, String projectTeam, ProjectRole projectRole, String fName, String lName, boolean overheadCost, double annualSalary, double hourlySalary, double dailyRate, double dailyWorkingHours) {
        this.profileId = profileId;
        this.countryId = countryId;
        this.projectTeam = projectTeam;
        this.projectRole = projectRole;
        this.fName = fName;
        this.lName = lName;
        this.overheadCost = overheadCost;
        this.annualSalary = annualSalary;
        this.hourlySalary = hourlySalary;
        this.dailyRate = dailyRate;
        this.dailyWorkingHours = dailyWorkingHours;
    }

    public Profile(int countryId, String projectTeam, ProjectRole projectRole, String fName, String lName, boolean overheadCost, double annualSalary, double hourlySalary, double dailyRate) {
        this.countryId = countryId;
        this.projectTeam = projectTeam;
        this.projectRole = projectRole;
        this.fName = fName;
        this.lName = lName;
        this.overheadCost = overheadCost;
        this.annualSalary = annualSalary;
        this.hourlySalary = hourlySalary;
        this.dailyRate = dailyRate;
    }

    //Create Profile
    public Profile(int countryId, String projectRole, String firstName, String lastName, boolean overheadCost, double annualSalary, double hourlyResult, double dailyResult, double fixedAmount, double dailyWorkingHours) {
        this.countryId = countryId;
        this.projectRole = ProjectRole.valueOf(projectRole);
        this.fName = firstName;
        this.lName = lastName;
        this.overheadCost = overheadCost;
        this.annualSalary = annualSalary;
        this.hourlySalary = hourlyResult;
        this.dailyRate = dailyResult;
        this.fixedAmount = fixedAmount;
        this.dailyWorkingHours = dailyWorkingHours;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getProjectTeam() {
        return projectTeam;
    }

    public void setProjectTeam(String projectTeam) {
        this.projectTeam = projectTeam;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public boolean isOverheadCost() {
        return overheadCost;
    }

    public void setOverheadCost(boolean overheadCost) {
        this.overheadCost = overheadCost;
    }

    public double getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    public double getFixedAmount() {
        return fixedAmount;
    }


    public double getHourlySalary() {
        return hourlySalary;
    }

    public void setHourlySalary(double hourlySalary) {
        this.hourlySalary = hourlySalary;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public List<Country> getCountry(){
        return country;
    }

    public List<ProjectTeam> getProjectTeams(){
        return projectTeams;
    }

    public double getHourlyRate() {
        return hourlySalary;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlySalary = hourlyRate;
    }

    public enum ProjectRole{
        DESIGN,
        DEVELOPER,
        ENGINEER,
        MANAGER,
        ROLE3,
        ROLE2
    }

    public void setProjectTeams(List<ProjectTeam> projectTeams) {
        this.projectTeams = projectTeams;
    }

    public ProjectRole getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(ProjectRole projectRole) {
        this.projectRole = projectRole;
    }

    public String getFullName() {
        return fName + " " + lName;
    }

    public double getDailyWorkingHours() {
        return dailyWorkingHours;
    }

    public void setDailyWorkingHours(double dailyWorkingHours) {
        this.dailyWorkingHours = dailyWorkingHours;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileId=" + profileId +
                ", countryId=" + countryId +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", overheadCost=" + overheadCost +
                ", annualSalary=" + annualSalary +
                ", hourlySalary=" + hourlySalary +
                ", dailyRate=" + dailyRate +
                '}';
    }
}
