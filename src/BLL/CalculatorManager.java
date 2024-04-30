package BLL;

import GUI.Controller.MultipliersController;
import GUI.Model.MultiplierModel;

public class CalculatorManager implements ICalculateManager{

    private MultiplierManager multiplierManager;


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



}
