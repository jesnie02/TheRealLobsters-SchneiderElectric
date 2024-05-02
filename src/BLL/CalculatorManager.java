package BLL;

public class CalculatorManager implements ICalculateManager{

    //BigDecimal

    @Override
    public double getDailyRate(double annualSalary) {
        return 0;
    }

    @Override
    public double getHourlyRate(double dailyRate) {
        return 0;
    }

    @Override
    /**
     * @param dayRate = is loaded from the profile or team (avgDay) selected.
     * @param percentage = gets the value from the slider in view.
     * Multiplikation of dayRate with percentage
     * only throw exception if dayRate is negative
     * @return
     */
    public double getDalyRateWithMultiplier(double dayRate, double percentage) throws IllegalArgumentException{
        if (dayRate < 0 ) {
            throw new IllegalArgumentException("Day rate cannot be negative");
        }
        double calculation = dayRate * (1 + percentage / 100); //TODO: Add a test
        return calculation;
    }

    @Override
    /**
     * @param hourlyRate = is loaded from the profile or team (avgHourly) selected.
     * @param percentage = gets the value from the slider in view.
     */
    public double getHourlyRateWithMultiplier(double hourlyRate, double percentage) {
        double calculation = hourlyRate * (1 + percentage / 100); //TODO: Add a test for this method @Mads
        return calculation;
    }

    @Override
    /**
     *
     * @param hourlyRate
     * @param fixedAmount
     * Additon of fixed amount to hourly rate
     * only throw exception if hourlyRate is negative
     * @return
     */
    public double getHourlyRateWithFixedAmountCreateProfile(double hourlyRate, double fixedAmount) throws IllegalArgumentException{
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        double calculation = hourlyRate + fixedAmount;
        return calculation;
    }

    @Override
    /**
     *
     * @param hourlyRate
     * @param percentage
     * Multiplikation of hourlyRate with percentage
     * only throw exception if hourlyRate is negative
     * @return
     */
    public double getHourlyRateWithMultiplierCreateProfile(double hourlyRate, double percentage) throws IllegalArgumentException{
      if (hourlyRate < 0 || percentage < 0) {
                throw new IllegalArgumentException("Hourly rate cannot be negative");
            }
        if (percentage == 0) {
            return hourlyRate;
        }
            double calculation = hourlyRate * (1 + percentage / 100);
            return calculation;
    }

    @Override
    /**
     *
     * @param annualSalary
     * @param effectiveHours
     * Division of annual salary with effective hours
     * only throw exception if annualSalary or effectiveHours is negative
     * @return
     */
    public double getHourlyRateWithEffectiveHoursCreateProfile(double annualSalary, double effectiveHours) throws IllegalArgumentException{
        if (annualSalary < 0 || effectiveHours < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }

        if (effectiveHours == 0) {
            return annualSalary;
        }
        double calculation = annualSalary / effectiveHours;
        return calculation;
    }

    @Override
    /**
     *
     * @param hourlyRate
     * @param utilization
     * Multiplikation of hourlyRate with utilization
     * only throw exception if hourlyRate is negative
     * @return
     */
    public double getHourlyRateWithUtilizationCreateProfile(double hourlyRate, double utilization) throws IllegalArgumentException{
      if (hourlyRate < 0 ) {
                throw new IllegalArgumentException("Hourly rate cannot be negative");
            }
        if (utilization == 0) {
            return hourlyRate;
        }
        double calculation = hourlyRate * (utilization/ 100);
        return calculation;
    }
}
