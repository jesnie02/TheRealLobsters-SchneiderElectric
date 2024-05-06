package BE;

public class Geography {


    private int geographyId;
    private String geographyName;
    private double sumOfDailyRate;
    private double sumOfHourlyRate;
    private double avgDailyRate;
    private double avgHourlyRate;
    private int profileCount;



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
}
