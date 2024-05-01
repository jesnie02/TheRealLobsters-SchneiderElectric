package BE;

public class Profile {


    private String firstName;
    private String lastName;
    private float annualSalary;
    private int countryId;

    private int hourlyResult;
    private int dailyResult;
    private boolean overheadCost;



    private ProjectRole projectRole;



    public Profile(String firstName, String lastName, float annualSalary, int countryId, String projectRole, int hourlyResult, int dailyResult, boolean overheadCost) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.annualSalary = annualSalary;
        this.countryId = countryId;
        this.projectRole = ProjectRole.valueOf(projectRole);
        this.hourlyResult = hourlyResult;
        this.dailyResult = dailyResult;
        this.overheadCost = overheadCost;
    }


    public enum ProjectRole {
        ROLE1,
        ROLE2,
        ROLE3

    }


    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public float getAnnualSalary() {
        return annualSalary;
    }

    private void setAnnualSalary(float annualSalary) {
        this.annualSalary = annualSalary;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCountryId() {
        return countryId;
    }

    private void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public ProjectRole getProjectRole() {
        return projectRole;
    }

    private void setProjectRole(ProjectRole projectRole) {
        this.projectRole = projectRole;
    }

    public int getHourlyResult() {
        return hourlyResult;
    }

    private void setHourlyResult(String hourlyResult) {
        this.hourlyResult = Integer.parseInt(hourlyResult);
    }

    public int getDailyResult() {
        return dailyResult;
    }

    private void setDailyResult(String dailyResult) {
        this.dailyResult = Integer.parseInt(dailyResult);
    }

    public boolean isOverheadCost() {
        return overheadCost;
    }

    private void setOverheadCost(boolean overheadCost) {
        this.overheadCost = overheadCost;
    }



}
