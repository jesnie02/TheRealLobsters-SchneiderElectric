package BLL;

public class MultiplierManager {


    private CalculatorManager calculatorManager;


    public MultiplierManager(CalculatorManager calculatorManager){
        this.calculatorManager = calculatorManager;
    }

    public double calculateDayRateWithMultiplier(double rate, double percentage){
        return calculatorManager.getDailyRateWithMultiplier(rate, percentage);
    }

    public double calculateHourlyRateWithMultiplier(double rate, double percentage){
        return calculatorManager.getHourlyRateWithMultiplier(rate, percentage);
    }


}
