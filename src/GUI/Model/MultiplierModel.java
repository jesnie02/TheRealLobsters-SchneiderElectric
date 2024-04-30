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

    public double getGrossMarginValue(){
        return multipliersController.getGMValue();
    }

    public double getMarkupValue() {
        return multipliersController.getMUValue();
    }

    public double getResultOfDayRWithMultiplier(double dayRate, double percentage) {
        return multiplierManager.calculateDayRateWithMultiplier(dayRate, percentage);
    }
}
