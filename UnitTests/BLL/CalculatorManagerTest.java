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

    //--------------------------------------------------------------------------------------------------------

    @Test
    /**
     * Test for getDalyRateWithMultiplier method
     * Test for valid input with multiplication of percentage
     */
    void getDalyRateWithMultiplier_returnsCorrectValueForValidInput() {
        double dayRate = 100.0;
        double percentage = 20.0;
        double expected = 120.0;

        double actual = calculatorManager.getDalyRateWithMultiplier(dayRate, percentage);

        assertEquals(expected, actual);
    }

    @Test
    /**
     * Test for getDalyRateWithMultiplier method
     * Test for valid input with multiplication of zero
     */
    void getDalyRateWithMultiplier_returnsZeroForZeroDayRate() {
        double dayRate = 0.0;
        double percentage = 20.0;

        double actual = calculatorManager.getDalyRateWithMultiplier(dayRate, percentage);

        assertEquals(0.0, actual);

        dayRate = 0;
        percentage = 50.0;

        actual = calculatorManager.getDalyRateWithMultiplier(dayRate, percentage);

        assertEquals(0.0, actual);
    }

    @Test
    /**
     * Test for getDalyRateWithMultiplier method
     * Test for negative day rate
     */
    void getDalyRateWithMultiplier_throwsExceptionForNegativeDayRate() {
        double dayRate = -100.0;
        double percentage = 20.0;

        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getDalyRateWithMultiplier(dayRate, percentage));
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    /**
     * Test for getHourlyRateWithFixedAmountCreateProfile method
     * Test for valid input with addition of fixed amount
     */
    void getHourlyRateWithFixedAmount_CreateProfile_returnsCorrectValueForValidInput() {
        double hourlyRate = 20.0;
        double fixedAmount = 100.0;
        double expected = 120.0;

        double actual = calculatorManager.getHourlyRateWithFixedAmountCreateProfile(hourlyRate, fixedAmount);

        assertEquals(expected, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithFixedAmountCreateProfile method
     * Test for valid input with addition of zero
     */
    void getHourlyRateWithFixedAmount_returnsZeroForHourlyRateCreateProfile() {
        double hourlyRate = 0.0;
        double fixedAmount = 100.0;
        double expected = 100.0;

        double actual = calculatorManager.getHourlyRateWithFixedAmountCreateProfile(hourlyRate, fixedAmount);

        assertEquals(expected, actual);

        hourlyRate = 20.0;
        fixedAmount = 0.0;
        expected = 20.0;

        actual = calculatorManager.getHourlyRateWithFixedAmountCreateProfile(hourlyRate, fixedAmount);

        assertEquals(expected, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithFixedAmountCreateProfile method
     * Test for negative hourly rate
     */
    void getHourlyRateWithFixedAmount_throwsExceptionForNegativeHourlyRateCreateProfile() {
        double hourlyRate = -20.0;
        double fixedAmount = 100.0;

        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getHourlyRateWithFixedAmountCreateProfile(hourlyRate, fixedAmount));
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    /**
     * Test for getHourlyRateWithMultiplierCreateProfile method
     * Test for valid input with multiplication of percentage
     */
    void getHourlyRateWithMultiplierCreateProfile_returnsCorrectValueForValidInput() {
        double hourlyRate = 100.0;
        double percentage = 20.0;
        double expected = 120.0;

        double actual = calculatorManager.getHourlyRateWithMultiplierCreateProfile(hourlyRate, percentage);

        assertEquals(expected, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithMultiplierCreateProfile method
     * Test for valid input with multiplication of zero
     */
    void getHourlyRateWithMultiplierCreateProfile_returnsZeroForHourlyRateCreateProfile() {
        double hourlyRate = 20.0;
        double percentage = 0.0;

        double actual = calculatorManager.getHourlyRateWithMultiplierCreateProfile(hourlyRate, percentage);

        assertEquals(20, actual);

        hourlyRate = 0.0;
        percentage = 20.0;

        actual = calculatorManager.getHourlyRateWithMultiplierCreateProfile(hourlyRate, percentage);

        assertEquals(0.0, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithMultiplierCreateProfile method
     * Test for negative hourly rate
     */
    void getHourlyRateWithMultiplierCreateProfile_throwsExceptionForNegativeHourlyRate() {
        double hourlyRate = -100.0;
        double percentage = 20.0;

        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getHourlyRateWithMultiplierCreateProfile(hourlyRate, percentage));
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    /**
     * Test for getHourlyRateWithEffectiveHoursCreateProfile method
     * Test for valid input with division of annual salary with effective hours
     */
    void getHourlyRateWithEffectiveHoursCreateProfile_returnsCorrectValueForValidInput() {
        double annualSalary = 40000.0;
        double effectiveHours = 2000.0;
        double expected = 20.0;

        double actual = calculatorManager.getHourlyRateWithEffectiveHoursCreateProfile(annualSalary, effectiveHours);

        assertEquals(expected, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithEffectiveHoursCreateProfile method
     * Test for valid input with division of zero
     */
    void getHourlyRateWithEffectiveHoursCreateProfile_returnsZeroForHourlyRateCreateProfile() {
        double annualSalary = 0;
        double effectiveHours = 2000;

        double actual = calculatorManager.getHourlyRateWithEffectiveHoursCreateProfile(annualSalary, effectiveHours);
        assertEquals(0.0, actual);

        annualSalary = 40000.0;
        effectiveHours = 0.0;

        actual = calculatorManager.getHourlyRateWithEffectiveHoursCreateProfile(annualSalary, effectiveHours);
        assertEquals(40000.0, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithEffectiveHoursCreateProfile method
     * Test for negative annual salary or effective hours
     */
    void getHourlyRateWithEffectiveHoursCreateProfile_throwsExceptionForNegativeHourlyRate() {
        double hourlyRate1 = -100.0;
        double effectiveHours1 = 20.0;

        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getHourlyRateWithEffectiveHoursCreateProfile(hourlyRate1, effectiveHours1));

        double hourlyRate2 = 100.0;
        double effectiveHours2 = -20.0;

        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getHourlyRateWithEffectiveHoursCreateProfile(hourlyRate2, effectiveHours2));
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    /**
     * Test for getHourlyRateWithUtilizationCreateProfile method
     * Test for valid input with multiplication of utilization
     */
    void getHourlyRateWithUtilizationCreateProfile_returnsCorrectValue() {
        double hourlyRate = 25;
        double utilization = 60;
        double expected = 15;

        double actual = calculatorManager.getHourlyRateWithUtilizationCreateProfile(hourlyRate, utilization);

        assertEquals(expected, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithUtilizationCreateProfile method
     * Test for valid input with multiplication of zero
     */
    void getHourlyRateWithUtilizationCreateProfile_returnsZeroForHourlyRateCreateProfile() {
        double hourlyRate = 0.0;
        double utilization = 20.0;

        double actual = calculatorManager.getHourlyRateWithUtilizationCreateProfile(hourlyRate, utilization);

        assertEquals(0.0, actual);

        hourlyRate = 20.0;
        utilization = 0.0;

        actual = calculatorManager.getHourlyRateWithUtilizationCreateProfile(hourlyRate, utilization);

        assertEquals(20, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithUtilizationCreateProfile method
     * Test for negative hourly rate
     */
    void getHourlyRateWithUtilizationCreateProfile_throwsExceptionForNegative() {
        double hourlyRate = -100.0;
        double utilization = 20.0;

        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getHourlyRateWithUtilizationCreateProfile(hourlyRate, utilization));
    }

    //--------------------------------------------------------------------------------------------------------
}