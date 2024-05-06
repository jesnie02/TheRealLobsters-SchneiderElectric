package BLL;

import BE.Profile;

import java.util.List;

public class CalculatorManager implements ICalculateManager {


    @Override
    /**
     * @param dayRate = is loaded from the profile (dayRate) or team (avgDay) selected.
     * @param percentage = gets the value from the slider in view.
     * Multiplikation of dayRate with percentage
     * only throw exception if dayRate is negative
     * @return
     */
    public double getDailyRateWithMultiplier(double dayRate, double percentage) throws IllegalArgumentException {
        if (dayRate < 0) {
            throw new IllegalArgumentException("Day rate cannot be negative");
        }
        double calculation = dayRate * (1 + percentage / 100);
        return calculation;
    }

    @Override
    /**
     * @param hourlyRate = is loaded from the profile (hourlyRate) or team (avgHourly) selected.
     * @param percentage = gets the value from the slider in view.
     */
    public double getHourlyRateWithMultiplier(double hourlyRate, double percentage) {
        double calculation = hourlyRate * (1 + percentage / 100); //TODO: Add a test for this method @Mads
        return calculation;
    }

    @Override
    public double annualSalaryWithFixedAmount(List<Profile> profiles) {
        double annualSalary = 0;
        for (Profile profile : profiles) {
            annualSalary += (profile.getAnnualSalary() + profile.getFixedAmount());
        }
        return annualSalary;
    }

    @Override
    public double avgAnnualSalary(List<Profile> profiles) {
        return annualSalaryWithFixedAmount(profiles) / profiles.size();
    }

    @Override
    public double sumOfHourlyRate(List<Profile> profiles) {
        double sumOfHourlyRate = 0;
        for (Profile profile : profiles) {
            sumOfHourlyRate += profile.getHourlyRate();
        }
        return sumOfHourlyRate;
    }

    @Override
    public double avgHourlyRate(List<Profile> profiles) {
        return sumOfHourlyRate(profiles) / profiles.size();
    }

    @Override
    public double sumOfDailyRate(List<Profile> profiles) {
        double sumOfDailyRate = 0;
        for (Profile profile : profiles) {
            sumOfDailyRate += profile.getDailyRate();
        }
        return sumOfDailyRate;
    }


    @Override
    public double avgDailyRate(List<Profile> profiles) {
        return sumOfDailyRate(profiles) / profiles.size();
    }

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

    @Override
    public double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate) {
        double result = dailyWorkingHours * hourlyRate;
        return result;
    }
}


