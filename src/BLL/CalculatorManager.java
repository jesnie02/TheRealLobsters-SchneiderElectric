package BLL;

import GUI.Controller.MultipliersController;
import GUI.Model.MultiplierModel;

public class CalculatorManager implements ICalculateManager{

    private MultiplierModel multiplierModel;
    private double hourlyRate;
    private double fixedAmount;

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
    public void setHourlyRateAndFixedAmount(double hourlyRate, double fixedAmt) {
        this.hourlyRate = hourlyRate;
        this.fixedAmount = fixedAmt;
    }

    public double getCalculatedHourlyRateWithFixedAmount() {
        double calculatedHourlyRateWithFixedAmountResult = hourlyRate * fixedAmount;
        return calculatedHourlyRateWithFixedAmountResult;
    }


}
