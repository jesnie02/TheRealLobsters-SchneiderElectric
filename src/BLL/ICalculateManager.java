package BLL;

public interface ICalculateManager {

    double getDalyRateWithMultiplier(double rate, double percentage);

    void setHourlyRateAndFixedAmount(double hourlyRate, double fixedAmt);
}
