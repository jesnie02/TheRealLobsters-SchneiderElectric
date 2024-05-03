package BLL;

public interface ICalculateManager {




    double getDailyRateWithMultiplier(double rate, double percentage);
    double getHourlyRateWithMultiplier(double hourlyRate, double percentage);

    double getSumOfAnnualSalaryForTeam(double annualSalary, double fixedAmount);
}
