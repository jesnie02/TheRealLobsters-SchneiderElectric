package BLL;

import BE.Profile;
import BE.ProjectTeam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for performing various calculations related to profiles.
 * It implements the ICalculateManager interface.
 */
public class CalculatorManager implements ICalculateManager {

    /**
     * Calculates the daily rate with a multiplier.
     * @param dayRate The daily rate.
     * @param percentage The multiplier as a percentage.
     * @return The daily rate multiplied by the percentage.
     * @throws IllegalArgumentException if the day rate is negative.
     */
    @Override
    public double getDailyRateWithMultiplier(double dayRate, double percentage) throws IllegalArgumentException {
        if (dayRate < 0) {
            throw new IllegalArgumentException("Day rate cannot be negative");
        }
        double calculation = dayRate * (1 + percentage / 100);
        return calculation;
    }

    /**
     * Calculates the hourly rate with a multiplier.
     * @param hourlyRate The hourly rate.
     * @param percentage The multiplier as a percentage.
     * @return The hourly rate multiplied by the percentage.
     */
    @Override
    public double getHourlyRateWithMultiplier(double hourlyRate, double percentage) {
        if(hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        double calculation = hourlyRate * (1 + percentage / 100);
        return calculation;
    }

    /**
     * Calculates the total annual salary with fixed amount for a list of profiles.
     * @param profiles The list of profiles.
     * @return The total annual salary with fixed amount.
     */
    @Override
    public double annualSalaryWithFixedAmount(List<Profile> profiles) {
        double annualSalary = 0;
        for (Profile profile : profiles) {
            annualSalary += (profile.getAnnualSalary() + profile.getFixedAmount());
        }
        return annualSalary;
    }

    /**
     * Calculates the average annual salary for a list of profiles.
     * @param team The project team.
     * @return The average annual salary.
     */
    public double avgAnnualSalary(ProjectTeam team) {
        if (team.getProfiles().isEmpty()) return 0;
        return team.getSumOfAnnualSalaries() / team.getProfiles().size();
    }

    /**
     * Calculates the sum of hourly rates for a list of profiles.
     * @param profiles The list of profiles.
     * @return The sum of hourly rates.
     */
    @Override
    public double sumOfHourlyRate(List<Profile> profiles) {
        double sumOfHourlyRate = 0;
        for (Profile profile : profiles) {
            sumOfHourlyRate += profile.getHourlyRate();
        }
        return sumOfHourlyRate;
    }

    /**
     * Calculates the average hourly rate for a list of profiles.
     * @param team
     * @return The average hourly rate.
     */
    public double avgHourlyRate(ProjectTeam team) {
        if (team.getProfiles().isEmpty()) {
            return 0;
        }
        return team.getSumOfHourlyRates() / team.getProfiles().size();
    }

    /**
     * Calculates the sum of daily rates for a list of profiles.
     * @param profiles The list of profiles.
     * @return The sum of daily rates.
     */
    @Override
    public double sumOfDailyRate(List<Profile> profiles) {
        double sumOfDailyRate = 0;
        for (Profile profile : profiles) {
            sumOfDailyRate += profile.getDailyRate();
        }
        return sumOfDailyRate;
    }

    /**
     * Calculates the average daily rate for a list of profiles.
     * @param team
     * @return The average daily rate.
     */
    public double avgDailyRate(ProjectTeam team) {
        if (team.getProfiles().isEmpty()) return 0;
        return team.getSumOfDailyRates() / team.getProfiles().size();
    }


    /**
     * Calculates and sets the hourly rate for a profile during creation.
     * @param annualSalaryProfile The annual salary of the profile.
     * @param overheadMultiplierProfile The overhead multiplier of the profile.
     * @param annualFixedAmountProfile The annual fixed amount of the profile.
     * @param effectiveHoursProfile The effective hours of the profile.
     * @return The calculated hourly rate.
     */
    @Override
    public double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile) {

        if (annualSalaryProfile == 0) {
            return 0; // Return zero if the annual salary is zero
        }

        if (annualSalaryProfile < 0) {
            throw new IllegalArgumentException("Annual salary cannot be negative");
        }

        double actualAnnualSalary = (annualSalaryProfile + annualFixedAmountProfile);
        double result = (actualAnnualSalary / effectiveHoursProfile) * (1 + overheadMultiplierProfile / 100);
        return result;
    }

    /**
     * Calculates and sets the daily rate for a profile during creation.
     * @param dailyWorkingHours The daily working hours of the profile.
     * @param hourlyRate The hourly rate of the profile.
     * @return The calculated daily rate.
     */
    @Override
    public double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate) {
        double result = dailyWorkingHours * hourlyRate;
        return result;
    }

    /**
     * Calculates and sets the hourly rate for a profile with utilization.
     * @param annualSalaryProfile The annual salary of the profile.
     * @param overheadMultiplierProfile The overhead multiplier of the profile.
     * @param annualFixedAmountProfile The annual fixed amount of the profile.
     * @param effectiveHoursProfile The effective hours of the profile.
     * @param utilizationPercentage The utilization percentage of the profile.
     * @return The calculated hourly rate with utilization.
     */
    @Override
    public double calculateAndSetHourlyRateWithUtilization(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile,
            double utilizationPercentage) {

        if (annualSalaryProfile == 0) {
            return 0; // Return zero if the annual salary is zero
        }

        if (annualSalaryProfile < 0) {
            throw new IllegalArgumentException("Annual salary cannot be negative");
        }

        double actualAnnualSalary = (annualSalaryProfile + annualFixedAmountProfile);
        double result = (actualAnnualSalary / effectiveHoursProfile) * (1 + overheadMultiplierProfile / 100);
        return result*(utilizationPercentage/100);
    }

    /**
     * Calculates and sets the daily rate for a profile during creation.
     * @param dailyWorkingHours The daily working hours of the profile.
     * @param hourlyRate The hourly rate of the profile.
     * @return The calculated daily rate.
     */
    @Override
    public double calculateAndSetDailyRateWithUtilization(double dailyWorkingHours, double hourlyRate) {
        double result = dailyWorkingHours * hourlyRate;
        return result;
    }

    /**
     * Calculates and sets the profile rates in EUR.
     * @param annualSalary The annual salary of the profile.
     * @param fixedAmount The fixed amount of the profile.
     * @param hourlyRate The hourly rate of the profile.
     * @param dailyRate The daily rate of the profile.
     * @param conversionRate The conversion rate from the selected currency to EUR.
     * @return A map containing the calculated rates in EUR.
     */
    @Override
    public Map<String, Double> calculateAndSetProfileRatesEUR(double annualSalary, double fixedAmount, double hourlyRate, double dailyRate, double conversionRate) {
        double annualSalaryEUR = (annualSalary + fixedAmount) / conversionRate;
        double hourlyRateEUR = hourlyRate / conversionRate;
        double dailyRateEUR = dailyRate / conversionRate;

        if (annualSalary < 0 || fixedAmount < 0 || hourlyRate < 0 || dailyRate < 0 || conversionRate < 0) {
            throw new IllegalArgumentException("Rates cannot be negative");
        }

        if (conversionRate == 0) {
            Map<String, Double> result = new HashMap<>();
            result.put("annualSalaryEUR", annualSalaryEUR = 0.0);
            result.put("hourlyRateEUR", hourlyRateEUR = 0.0);
            result.put("dailyRateEUR", dailyRateEUR = 0.0);
        }

        Map<String, Double> result = new HashMap<>();
        result.put("annualSalaryEUR", annualSalaryEUR);
        result.put("hourlyRateEUR", hourlyRateEUR);
        result.put("dailyRateEUR", dailyRateEUR);

        return result;
    }


    /**
     * Calculates the rates with utilization for a profile.
     * @param profile The profile.
     * @param utilization The utilization percentage.
     * @return A map containing the calculated rates with utilization.
     */
    @Override
    public Map<String, Double> calculateRatesWithUtilizationForUpdateTeam(Profile profile, double utilization) {
        double newHourlyRate;
        double newDailyRate;
        double newAnnualSalary;

        if (utilization < 0) {
            throw new IllegalArgumentException("Utilization cannot be negative");
        }

        if (utilization == 0) {
            newHourlyRate = profile.getHourlyRate();
            newDailyRate = profile.getDailyRate();
            newAnnualSalary = profile.getAnnualSalary();
        } else {
            double utilizationFactor = utilization / 100;
            newHourlyRate = profile.getHourlySalary() * utilizationFactor;
            newDailyRate = profile.getDailyRate() * utilizationFactor;
            newAnnualSalary = profile.getAnnualSalary() * utilizationFactor;

            profile.setHourlyRate(newHourlyRate);
            profile.setDailyRate(newDailyRate);
            profile.setAnnualSalary(newAnnualSalary);
        }

        Map<String, Double> rates = new HashMap<>();
        rates.put("hourlyRate", newHourlyRate);
        rates.put("dailyRate", newDailyRate);
        rates.put("annualSalary", newAnnualSalary);

        return rates;
    }
}


