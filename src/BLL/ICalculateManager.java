package BLL;

public interface ICalculateManager {


    double getDailyRate(double annualSalary);
    double getHourlyRate(double dailyRate);

    double getDalyRateWithMultiplier(double rate, double percentage);
    double getHourlyRateWithMultiplier(double hourlyRate, double percentage);
    /**
     *
     * @param hourlyRate
     * @param fixedAmount
     * @return
     */
    double getHourlyRateWithFixedAmount(double hourlyRate, double fixedAmount);
}
