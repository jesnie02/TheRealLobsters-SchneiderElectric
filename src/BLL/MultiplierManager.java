package BLL;

public class MultiplierManager {


    private CalculatorManager calculatorManager;


    public MultiplierManager(CalculatorManager calculatorManager){
        this.calculatorManager = calculatorManager;
    }

    public double calculateDayRateWithMultiplier(double rate, double percentage){
        return calculatorManager.getDalyRateWithMultiplier(rate, percentage);
    }


}
