package BLL;

public class CalculatorManager implements ICalculateManager{

    @Override
    /**
     *
     * @param dayRate = daily rate
     * @param percentage
     * @return
     */
    public double getDalyRateWithMultiplier(double dayRate, double percentage){
        double calculation = dayRate * (1 + percentage / 100);
        return calculation;
    }

    @Override
    /**
     *
     * @param hourlyRate
     * @param fixedAmount
     * @return
     */
    public double getHourlyRateWithFixedAmount(double hourlyRate, double fixedAmount){
        double calculation = hourlyRate + fixedAmount;
        return calculation;
    }
}
