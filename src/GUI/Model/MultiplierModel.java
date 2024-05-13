package GUI.Model;

import BLL.CalculatorManager;
import BLL.MultiplierManager;
import GUI.Controller.MultiplierController;


public class MultiplierModel {

    private MultiplierController multiplierController;
    private MultiplierManager multiplierManager;




    public MultiplierModel(MultiplierController multiplierController){
        this.multiplierController = multiplierController;
        this.multiplierManager = new MultiplierManager(new CalculatorManager());
    }


    public double getResultOfDayRWithMultiplier(double dayRate, double percentage) {
        return multiplierManager.calculateDayRateWithMultiplier(dayRate, percentage);
    }

    public double getResultOfHourlyRateWithMultiplier(double hourlyRate, double percentage){
       return multiplierManager.calculateHourlyRateWithMultiplier(hourlyRate, percentage);
    }
}
