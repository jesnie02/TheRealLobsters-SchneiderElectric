package BLL;

import java.util.function.ToDoubleBiFunction;

public class CalculatorManager implements ICalculateManager{

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
     *
     * @param dayRate
     * @param percentage = gets the value from the slider in view.
     * @return
     */
    public double getDalyRateWithMultiplier(double dayRate, double percentage) throws IllegalArgumentException{
        if (dayRate < 0 || percentage < 0) {
            throw new IllegalArgumentException("Day rate cannot be negative");
        }
        double calculation = dayRate * (1 + percentage / 100);
        return calculation;
    }

    @Override
    /**
     * @param hourlyRate
     * @param percentage = gets the value from the slider in view.
     */
    public double getHourlyRateWithMultiplier(double hourlyRate, double percentage) {
        double calculation = hourlyRate * (1 + percentage / 100);
        return calculation;
    }


    @Override
    /**
     *
     * @param hourlyRate
     * @param fixedAmount
     * @return
     */
    public double getHourlyRateWithFixedAmount(double hourlyRate, double fixedAmount) throws IllegalArgumentException{

        //TODO: Check if i can add to zero
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        double calculation = hourlyRate + fixedAmount;
        return calculation;
    }
}
