package GUI.Model;

import BLL.CalculatorManager;
import BLL.MultiplierManager;
import GUI.Controller.MultipliersController;

public class MultiplierModel {

    private MultipliersController multipliersController;
    private MultiplierManager multiplierManager;




    public MultiplierModel(MultipliersController multipliersController){
        this.multipliersController = multipliersController;
        this.multiplierManager = new MultiplierManager(new CalculatorManager());
    }


    public double getResultOfDayRWithMultiplier(double dayRate, double percentage) {
        return multiplierManager.calculateDayRateWithMultiplier(dayRate, percentage);
    }

    public double getResultOfHourlyRateWithMultiplier(double hourlyRate, double percentage){
        return multiplierManager.calculateHourlyRateWithMultiplier(hourlyRate, percentage);
    }
}
