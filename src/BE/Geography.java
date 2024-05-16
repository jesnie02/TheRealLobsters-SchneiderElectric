package BE;

import java.util.ArrayList;
import java.util.List;

public class Geography {


    private int geographyId;
    private String geographyName;
    private double sumOfDailyRate;
    private double sumOfHourlyRate;
    private double avgDailyRate;
    private double avgHourlyRate;
    private int profileCount;
    private int teamCount;
    private List<Country> countries;



    public Geography(int geographyId, String geographyName, double sumOfDailyRate, double sumOfHourlyRate, double avgDailyRate, double avgHourlyRate, int profileCount) {
        this.geographyId = geographyId;
        this.geographyName = geographyName;
        this.sumOfDailyRate = sumOfDailyRate;
        this.sumOfHourlyRate = sumOfHourlyRate;
        this.avgDailyRate = avgDailyRate;
        this.avgHourlyRate = avgHourlyRate;
        this.profileCount = profileCount;
    }

    public Geography(int geographyId, String geographyName) {
        this.geographyId = geographyId;
        this.geographyName = geographyName;
    }

    public Geography(int geographyId, String geographyName, int profileCount, int teamCount) {
        this.geographyId = geographyId;
        this.geographyName = geographyName;
        this.profileCount = profileCount;
        this.teamCount = teamCount;
        this.countries = new ArrayList<>();
    }

    public Geography(String geographyName, List<Country> selectedCountries) {
        this.geographyName = geographyName;
        this.countries = selectedCountries;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }


    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public Geography(String geographyName) {
        this.geographyName = geographyName;
    }

    public int getProfileCount() {
        return profileCount;
    }

    public void setProfileCount(int profileCount) {
        this.profileCount = profileCount;
    }

    public int getGeographyId() {
        return geographyId;
    }

    public void setGeographyId(int geographyId) {
        this.geographyId = geographyId;
    }

    public String getGeographyName() {
        return geographyName;
    }

    public void setGeographyName(String geographyName) {
        this.geographyName = geographyName;
    }

    public double getSumOfDailyRate() {
        return sumOfDailyRate;
    }

    public double getSumOfHourlyRate() {
        return sumOfHourlyRate;
    }

    public double getAvgDailyRate() {
        return avgDailyRate;
    }

    public double getAvgHourlyRate() {
        return avgHourlyRate;
    }


    @Override
    public String toString() {
        return geographyName;
    }


    /*
    @Override
    public String toString() {
        return "Geography{" +
                "geographyId=" + geographyId +
                ", geographyName='" + geographyName + '\'' +
                ", sumOfDailyRate=" + sumOfDailyRate +
                ", sumOfHourlyRate=" + sumOfHourlyRate +
                ", avgDailyRate=" + avgDailyRate +
                ", avgHourlyRate=" + avgHourlyRate +
                ", profileCount=" + profileCount +
                ", teamCount=" + teamCount +
                '}';
    }

     */

}
