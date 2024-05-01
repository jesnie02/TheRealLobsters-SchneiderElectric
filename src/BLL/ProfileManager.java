package BLL;

public class ProfileManager {

    private CalculatorManager calculatorManager;

    public ProfileManager() {
        calculatorManager = new CalculatorManager();
    }

    public double calculateHourlyRateWithFixedAmount(double hourlyRate, double fixedAmount) {
        return calculatorManager.getHourlyRateWithFixedAmount(hourlyRate, fixedAmount);
    }
}