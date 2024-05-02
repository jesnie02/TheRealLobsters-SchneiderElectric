package BLL;

public interface ICalculateManager {


    double getDailyRate(double annualSalary);
    double getHourlyRate(double dailyRate);

    double getDalyRateWithMultiplier(double rate, double percentage);
    double getHourlyRateWithMultiplier(double hourlyRate, double percentage);

    double getHourlyRateWithFixedAmountCreateProfile(double hourlyRate, double fixedAmount);

    double getHourlyRateWithMultiplierCreateProfile(double hourlyRate, double percentage) throws IllegalArgumentException;

    double getHourlyRateWithEffectiveHoursCreateProfile(double hourlyRate, double effectiveHours) throws IllegalArgumentException;

    double getHourlyRateWithUtilizationCreateProfile(double hourlyRate, double utilization) throws IllegalArgumentException;
}
