package BLL;

import GUI.Controller.MultipliersController;
import GUI.Model.MultiplierModel;

public class CalculatorManager implements ICalculateManager{

    private MultiplierModel multiplierModel;

    @Override
    /**
     *
     * @param rate = daily rate or hourly rate
     * @param percentage
     * @return
     */
    public double getDalyRateWithMultiplier(double rate, double percentage){
        if (multiplierModel.getGrossMarginValue() == 0){
            percentage = multiplierModel.getMarkupValue();
        } else {
            percentage = multiplierModel.getGrossMarginValue();
        }

        double calculation = rate * (percentage / 100);

        return calculation;
    }


    private void setController(){
        multiplierModel = new MultiplierModel();
    }
}
