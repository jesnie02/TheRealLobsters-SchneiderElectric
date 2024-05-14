package BE;

public class Country {

    private int CountryId;
    private String CountryName;
    private double sumOfHourlyRate;
    private double sumOfDailyRate;
    private double avgDailyRate;
    private double avgHourlyRate;
    private double sumOfAnnualSalary;
    private int profileCount;


    public Country(int countryId, String countryName, double totalHourlyRate, double avgHourlyRate, double totalDailyRate, double avgDailyRate, int profileCount) {
        this.CountryId = countryId;
        this.CountryName = countryName;
        this.sumOfHourlyRate = totalHourlyRate;
        this.sumOfDailyRate = totalDailyRate;
        this.avgDailyRate = avgDailyRate;
        this.avgHourlyRate = avgHourlyRate;
        this.profileCount = profileCount;
    }

    public int getProfileCount() {
        return profileCount;
    }

    public void setProfileCount(int profileCount) {
        this.profileCount = profileCount;
    }

    public double getSumOfHourlyRate() {
        return sumOfHourlyRate;
    }

    public void setSumOfHourlyRate(double sumOfHourlyRate) {
        this.sumOfHourlyRate = sumOfHourlyRate;
    }

    public double getSumOfDailyRate() {
        return sumOfDailyRate;
    }

    public void setSumOfDailyRate(double sumOfDailyRate) {
        this.sumOfDailyRate = sumOfDailyRate;
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

    public void setSumOfAnnualSalary(double sumOfAnnualSalary) {
        this.sumOfAnnualSalary = sumOfAnnualSalary;
    }

    public double getAvgAnnualSalary() {
        return avgAnnualSalary;
    }

    public void setAvgAnnualSalary(double avgAnnualSalary) {
        this.avgAnnualSalary = avgAnnualSalary;
    }

    public double avgAnnualSalary;

    public Country(int id, String name) {
        this.CountryId = id;
        this.CountryName = name;
    }

    public Country(String name) {
        this.CountryName = name;
    }

    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    @Override
   public String toString() {
        return CountryName;
   }

}