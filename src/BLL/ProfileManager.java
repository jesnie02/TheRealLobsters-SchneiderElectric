package BLL;

public class ProfileManager {

    private String hourlyResult;
    private String fixedAmount;
    private CalculatorManager calculatorManager;
    private double calculatedHourlyRate;
    private double calculatedHourlyRateWithFixedAmountResult;

    public ProfileManager() {
        calculatorManager = new CalculatorManager();
    }

    public void setHourlyResultAndFixedAmount(String hourlyResult, String fixedAmount) {
        this.hourlyResult = hourlyResult;
        this.fixedAmount = fixedAmount;
        double hourlyRate = Double.parseDouble(hourlyResult);
        double fixedAmt = Double.parseDouble(fixedAmount);
        calculatorManager.setHourlyRateAndFixedAmount(hourlyRate, fixedAmt);
    }

    public double fetchAndReturnCalculatedHourlyRateWithFixedAmount() {
        this.calculatedHourlyRateWithFixedAmountResult = calculatorManager.getCalculatedHourlyRateWithFixedAmount();
        return calculatedHourlyRateWithFixedAmountResult;
    }
}