package BLL;

public interface ICalculateManager {

    double getDalyRateWithMultiplier(double rate, double percentage);

    /**
     *
     * @param hourlyRate
     * @param fixedAmount
     * @return
     */
    double getHourlyRateWithFixedAmount(double hourlyRate, double fixedAmount);
}
