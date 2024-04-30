package BLL;

import GUI.Controller.MultipliersController;

public class CalculatorManager implements ICalculateManager{

    private MultipliersController multipliersController;

    @Override
    /**
     *
     * @param rate = daily rate or hourly rate
     * @param percentage
     * @return
     */
    public double getDalyRateWithMultiplier(double rate, double percentage){
        if (multipliersController.getGMValue() == 0){
            percentage = multipliersController.getMUValue();
        } else {
            percentage = multipliersController.getGMValue();
        }

        double calculation = rate * (percentage / 100);

        return calculation;
    }


    private void setController(){
        multipliersController = new MultipliersController();
    }
}
