package BLL;

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
    public double getSumOfAnnualSalaryForTeam(double annualSalary, double fixedAmount) {
        return 0;
    }


}
