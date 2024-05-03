package BLL;

public class MultiplierManager {


    private ICalculateManager iCalculateManager;


    public MultiplierManager(ICalculateManager calculatorManager){
        this.iCalculateManager = calculatorManager;
    }

    public double calculateDayRateWithMultiplier(double rate, double percentage){
        return iCalculateManager.getDailyRateWithMultiplier(rate, percentage);
    }

    public double calculateHourlyRateWithMultiplier(double rate, double percentage){
        return iCalculateManager.getHourlyRateWithMultiplier(rate, percentage);
    }


}
