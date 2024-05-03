package BLL;

import BE.Profile;

import java.util.List;

public class CalculatorManager implements ICalculateManager{




    @Override
    /**
     * @param dayRate = is loaded from the profile (dayRate) or team (avgDay) selected.
     * @param percentage = gets the value from the slider in view.
     * Multiplikation of dayRate with percentage
     * only throw exception if dayRate is negative
     * @return
     */
    public double getDailyRateWithMultiplier(double dayRate, double percentage) throws IllegalArgumentException{
        if (dayRate < 0 ) {
            throw new IllegalArgumentException("Day rate cannot be negative");
        }
        double calculation = dayRate * (1 + percentage / 100); //TODO: Add a test
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
    public double annualSalary(List<Profile> profiles) {
        double annualSalary = 0;
        for (Profile profile :profiles){
            annualSalary += (profile.getAnnualSalary() + profile.getFixedAmount());
        }
        return annualSalary;
    }

    @Override
    public double avgAnnualSalary(List<Profile> profiles){
        return annualSalary(profiles)/profiles.size();
    }
}
