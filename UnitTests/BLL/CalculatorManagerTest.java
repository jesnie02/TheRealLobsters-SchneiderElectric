package BLL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorManagerTest {

    private CalculatorManager calculatorManager;

    @BeforeEach
    void setUp() {
        calculatorManager = new CalculatorManager();
    }

    @Test
    void getDalyRateWithMultiplier_returnsCorrectValueForValidInput() {
        double dayRate = 100.0;
        double percentage = 20.0;
        double expected = 120.0;

        double actual = calculatorManager.getDalyRateWithMultiplier(dayRate, percentage);

        assertEquals(expected, actual);
    }

    @Test
    void getDalyRateWithMultiplier_returnsZeroForZeroDayRate() {
        double dayRate = 0.0;
        double percentage = 20.0;

        double actual = calculatorManager.getDalyRateWithMultiplier(dayRate, percentage);

        assertEquals(0.0, actual);
    }

    @Test
    void getDalyRateWithMultiplier_throwsExceptionForNegativeDayRate() {
        double dayRate = -100.0;
        double percentage = 20.0;

        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getDalyRateWithMultiplier(dayRate, percentage));
    }

    @Test
    void getHourlyRateWithFixedAmount_returnsCorrectValueForValidInput() {
        double hourlyRate = 20.0;
        double fixedAmount = 100.0;
        double expected = 120.0;

        double actual = calculatorManager.getHourlyRateWithFixedAmount(hourlyRate, fixedAmount);

        assertEquals(expected, actual);
    }

    @Test
    void getHourlyRateWithFixedAmount_returnsZeroForZeroHourlyRate() {
        double hourlyRate = 0.0;
        double fixedAmount = 100.0;
        double expected = 100.0;

        double actual = calculatorManager.getHourlyRateWithFixedAmount(hourlyRate, fixedAmount);

        assertEquals(expected, actual);
    }

    @Test
    void getHourlyRateWithFixedAmount_throwsExceptionForNegativeHourlyRate() {
        double hourlyRate = -20.0;
        double fixedAmount = 100.0;

        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getHourlyRateWithFixedAmount(hourlyRate, fixedAmount));
    }
}