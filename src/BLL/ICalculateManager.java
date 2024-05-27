package BLL;

import BE.Profile;
import BE.ProjectTeam;

import java.util.List;
import java.util.Map;

public interface ICalculateManager {


    double getDailyRateWithMultiplier(double rate, double percentage);
    double getHourlyRateWithMultiplier(double hourlyRate, double percentage);

    double avgAnnualSalary(ProjectTeam teams);
    double annualSalaryWithFixedAmount(List<Profile> profiles);

    double sumOfHourlyRate(List<Profile> profiles);
    double avgHourlyRate(ProjectTeam teams);
    double avgDailyRate(ProjectTeam teams);
    double sumOfDailyRate(List<Profile> profiles);


    double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile);

    double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate);



    double calculateAndSetHourlyRateWithUtilization(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile, double utilizationPercentage);


    double calculateAndSetDailyRateWithUtilization(double dailyWorkingHours, double hourlyRate);

    Map<String, Double> calculateAndSetProfileRatesEUR(double annualSalary, double fixedAmount, double hourlyRate, double dailyRate, double conversionRate);

    Map<String, Double> calculateRatesWithUtilizationForUpdateTeam(Profile profile, double utilization);
}
