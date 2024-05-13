package BLL;

import BE.Profile;

import java.util.List;

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
     * @param profiles The list of profiles.
     * @return The average annual salary.
     */
    @Override
    public double avgAnnualSalary(List<Profile> profiles) {
        return annualSalaryWithFixedAmount(profiles) / profiles.size();
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
     * @param profiles The list of profiles.
     * @return The average hourly rate.
     */
    @Override
    public double avgHourlyRate(List<Profile> profiles) {
        return sumOfHourlyRate(profiles) / profiles.size();
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
     * @param profiles The list of profiles.
     * @return The average daily rate.
     */
    @Override
    public double avgDailyRate(List<Profile> profiles) {
        return sumOfDailyRate(profiles) / profiles.size();
    }


    /**
     * Calculates and sets the hourly rate for a profile during creation.
     * @param annualSalaryProfile The annual salary of the profile.
     * @param overheadMultiplierProfile The overhead multiplier of the profile.
     * @param annualFixedAmountProfile The annual fixed amount of the profile.
     * @param effectiveHoursProfile The effective hours of the profile.
     * @param utilizationPercentageProfile The utilization percentage of the profile.
     * @return The calculated hourly rate.
     */
    @Override
    public double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile,
            double utilizationPercentageProfile) {

        if (annualSalaryProfile == 0) {
            return 0; // Return zero if the annual salary is zero
        }

        if (annualSalaryProfile < 0) {
            throw new IllegalArgumentException("Annual salary cannot be negative");
        }

        double actualAnnualSalary = (annualSalaryProfile + annualFixedAmountProfile);
        double actualEffectivehours = (effectiveHoursProfile * (utilizationPercentageProfile / 100));
        double result = (actualAnnualSalary / actualEffectivehours) * (1 + overheadMultiplierProfile / 100);
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
}


