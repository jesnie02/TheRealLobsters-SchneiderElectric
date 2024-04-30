package BLL;

import GUI.Controller.MultipliersController;

public class CalculaterManager {

    private MultipliersController multipliersController;

    public double getDalyRateWithMarkup(double rate, double percentage){
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
