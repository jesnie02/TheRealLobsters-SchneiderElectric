package BE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profile {

    private int profileId, countryId;
    private String fName, lName, roles;
    private boolean overheadCost;
    private double annualSalary, hourlySalary, dailyRate, fixedAmount, dailyWorkingHours, totalUtilization, overheadMultiplier, effectiveWorkingHours;
    private double utilizationTime, utilizationCost;

    private List<Country> country;
    private List<ProjectTeam> projectTeams;
    private List<ProfileRole> profileRoles;


    // For testing purposes
    public Profile(double annualSalary, double fixedAmount) {
        this.annualSalary = annualSalary;
        this.fixedAmount = fixedAmount;
    }

    public Profile(int profileId, String fName, String lName, boolean overheadCost, double annualSalary, double hourlySalary, double dailyRate, double dailyWorkingHours, double totalUtilization, double utilizationCost, double effectiveWorkingHours, double overheadMultiplier) {
        this.profileId = profileId;
        this.fName = fName;
        this.lName = lName;
        this.overheadCost = overheadCost;
        this.annualSalary = annualSalary;
        this.hourlySalary = hourlySalary;
        this.dailyRate = dailyRate;
        this.dailyWorkingHours = dailyWorkingHours;
        this.totalUtilization = totalUtilization;
        this.utilizationCost = utilizationCost;
        this.effectiveWorkingHours = effectiveWorkingHours;
        this.overheadMultiplier = overheadMultiplier;
    }

    public Profile(String fName, String lName, boolean overheadCost, double annualSalary, double hourlySalary, double dailyRate) {

        this.fName = fName;
        this.lName = lName;
        this.overheadCost = overheadCost;
        this.annualSalary = annualSalary;
        this.hourlySalary = hourlySalary;
        this.dailyRate = dailyRate;
        this.profileRoles = new ArrayList<>();
    }

    //Create Profile
    public Profile(String firstName, String lastName, boolean overheadCost, double annualSalary, double hourlyResult, double dailyResult, double fixedAmount, double dailyWorkingHours, List<ProfileRole> profileRoles, double effectiveWorkingHours, double overheadMultiplier) {
        this.fName = firstName;
        this.lName = lastName;
        this.overheadCost = overheadCost;
        this.annualSalary = annualSalary;
        this.hourlySalary = hourlyResult;
        this.dailyRate = dailyResult;
        this.fixedAmount = fixedAmount;
        this.dailyWorkingHours = dailyWorkingHours;
        this.profileRoles = profileRoles;
        this.effectiveWorkingHours = effectiveWorkingHours;
        this.overheadMultiplier = overheadMultiplier;
    }

    public Profile(String firstName, String lastName, boolean overheadCost, double annualSalary, double hourlyResult, double dailyResult, double fixedAmount, double dailyWorkingHours, double utilizationTime, double utilizationCost, List<ProfileRole> profileRoles) {
        this.fName = firstName;
        this.lName = lastName;
        this.overheadCost = overheadCost;
        this.annualSalary = annualSalary;
        this.hourlySalary = hourlyResult;
        this.dailyRate = dailyResult;
        this.fixedAmount = fixedAmount;
        this.dailyWorkingHours = dailyWorkingHours;
        this.utilizationTime = utilizationTime;
        this.utilizationCost = utilizationCost;
        this.profileRoles = profileRoles;
    }

    /**
     * For unit testing purposes
     */

    public Profile(double annualSalary, double fixedAmount, double dailyRate) {
        this.annualSalary = annualSalary;
        this.fixedAmount = fixedAmount;
        this.dailyRate = dailyRate;
        this.profileRoles = new ArrayList<>();
    }

    /**
     * For unit testing purposes
     */

    public Profile(double annualSalary, double fixedAmount, double dailyRate, double hourlyRate) {
        this.annualSalary = annualSalary;
        this.fixedAmount = fixedAmount;
        this.dailyRate = dailyRate;
        this.hourlySalary = hourlyRate;
        this.profileRoles = new ArrayList<>();
    }

//    public Profile(int id, String fName, String lName, boolean overhead, double annual, double hourly, double daily, double workingHR, double utilizationTime, double utilizationCost, List<ProfileRole> profileRoles) {
//        this.profileId = id;
//        this.fName = fName;
//        this.lName = lName;
//        this.overheadCost = overhead;
//        this.annualSalary = annual;
//        this.hourlySalary = hourly;
//        this.dailyRate = daily;
//        this.dailyWorkingHours = workingHR;
//        this.utilizationTime = utilizationTime;
//        this.profileRoles = profileRoles;
//    }

    public Profile(int id, String fName, String lName, boolean overhead, double annual, double hourly, double daily, double workingHR, double utilizationTime, double utilizationCost, List<ProfileRole> profileRoles) {
        this.profileId = id;
        this.fName = fName;
        this.lName = lName;
        this.overheadCost = overhead;
        this.annualSalary = annual;
        this.hourlySalary = hourly;
        this.dailyRate = daily;
        this.dailyWorkingHours = workingHR;
        this.utilizationTime = utilizationTime;
        this.utilizationCost = utilizationCost;
        this.profileRoles = profileRoles;
    }


    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public String getFName() {
        return fName;
    }

    public double getUtilizationTime() {
        return utilizationTime;
    }

    public void setUtilizationTime(double utilizationTime) {
        this.utilizationTime = utilizationTime;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
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

    public void setFixedAmount(double fixedAmount) {
        this.fixedAmount = fixedAmount;
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

    public List<Country> getCountry() {
        return country;
    }

    public List<ProjectTeam> getProjectTeams() {
        return projectTeams;
    }

    public double getHourlyRate() {
        return hourlySalary;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlySalary = hourlyRate;
    }

    public void setProjectTeams(List<ProjectTeam> projectTeams) {
        this.projectTeams = projectTeams;
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

    public List<ProfileRole> getProfileRoles() {
        return profileRoles;
    }

    public void setProfileRoles(List<ProfileRole> profileRoles) {
        this.profileRoles = profileRoles;
    }

    public double getTotalUtilization() {
        return totalUtilization;
    }

    public void setTotalUtilization(double totalUtilization) {
        this.totalUtilization = totalUtilization;
    }

    public double getOverheadMultiplier() {
        return overheadMultiplier;
    }

    public void setOverheadMultiplier(double overheadMultiplier) {
        this.overheadMultiplier = overheadMultiplier;
    }

    public double getEffectiveWorkingHours() {
        return effectiveWorkingHours;
    }

    public void setEffectiveWorkingHours(double effectiveWorkingHours) {
        this.effectiveWorkingHours = effectiveWorkingHours;
    }

    public double getUtilizationCost() {
        return utilizationCost;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileId=" + profileId +
                ", countryId=" + countryId +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", roles='" + roles + '\'' +
                ", overheadCost=" + overheadCost +
                ", annualSalary=" + annualSalary +
                ", hourlySalary=" + hourlySalary +
                ", dailyRate=" + dailyRate +
                ", fixedAmount=" + fixedAmount +
                ", dailyWorkingHours=" + dailyWorkingHours +
                ", totalUtilization=" + totalUtilization +
                ", overheadMultiplier=" + overheadMultiplier +
                ", effectiveWorkingHours=" + effectiveWorkingHours +
                ", utilizationTime=" + utilizationTime +
                ", utilizationCost=" + utilizationCost +
                ", country=" + country +
                ", projectTeams=" + projectTeams +
                ", profileRoles=" + profileRoles +
                '}';
    }

    public String getRolesString() {
        StringBuilder rolesString = new StringBuilder();
        for (ProfileRole role : profileRoles) {
            rolesString.append(role.getProfileRoleType()).append(", ");
        }
        return rolesString.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;
        return profileId == profile.profileId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }
}


