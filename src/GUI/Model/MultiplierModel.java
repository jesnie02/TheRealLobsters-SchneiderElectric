package GUI.Model;

import GUI.Controller.MultipliersController;

public class MultiplierModel {

    MultipliersController multipliersController;

    public double getGrossMarginValue(){
        double percentage = multipliersController.getGMValue();
        return percentage;
    }

    public double getMarkupValue() {
        double percentage = multipliersController.getMUValue();
        return percentage;
    }


    private void setController(){
       multipliersController = new MultipliersController();
    }


}
