package BLL;

import BE.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
     * Test for getDailyRateWithMultiplier method
     * Test for valid input with multiplication of percentage
     */
    void getDailyRateWithMultiplier_returnsCorrectValueForValidInput() {
        //arrange
        double dayRate = 100;
        double percentage = 20;
        double expected = 120;
        //act
        double actual = calculatorManager.getDailyRateWithMultiplier(dayRate, percentage);
        //assert
        assertEquals(expected, actual);
    }

    @Test
    /**
     * Test for getDailyRateWithMultiplier method
     * Test for valid input with multiplication of zero
     */
    void getDailyRateWithMultiplier_returnsZeroForZeroDayRate() {
        //arrange
        double dayRate = 20;
        double percentage = 0.0;
        //act
        double actual = calculatorManager.getDailyRateWithMultiplier(dayRate, percentage);
        //assert
        assertEquals(20, actual);

        dayRate = 0;
        percentage = 50.0;

        actual = calculatorManager.getDailyRateWithMultiplier(dayRate, percentage);

        assertEquals(0.0, actual);
    }

    @Test
    /**
     * Test for getDailyRateWithMultiplier method
     * Test for valid input with multiplication of negative percentage
     */
    void getDailyRateWithMultiplier_throwsExceptionForNegativeDayRate() {
        //arrange
        double dayRate = -100;
        double percentage = 20;
        //act and assert
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getDailyRateWithMultiplier(dayRate, percentage));
    }

    //--------------------------------------------------------------------------------------------------------


    @Test
    void calculateAndSetHourlyRateCreateProfile_returnsCorrectValueForValidInput() {
        // Arrange
        double annualSalaryProfile = 50000;
        double overheadMultiplierProfile = 50;
        double annualFixedAmountProfile = 10000;
        double effectiveHoursProfile = 2000;
        double utilizationPercentageProfile = 80;

        double expected = 56.25;

        // Act
        double actual = calculatorManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile,
                utilizationPercentageProfile);

        // Assert
        assertEquals(expected, actual);
    }


    @Test
    void calculateAndSetHourlyRateCreateProfile_returnsZeroForZeroAnnualSalary() {
        // Arrange
        double annualSalaryProfile = 0;
        double overheadMultiplierProfile = 1.5;
        double annualFixedAmountProfile = 10000;
        double effectiveHoursProfile = 2000;
        double utilizationPercentageProfile = 80;

        // Act
        double actual = calculatorManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile,
                utilizationPercentageProfile);

        // Assert
        assertEquals(0, actual);
    }

    @Test
    void calculateAndSetHourlyRateCreateProfile_throwsExceptionForNegativeAnnualSalary() {
        // Arrange
        double annualSalaryProfile = -50000;
        double overheadMultiplierProfile = 1.5;
        double annualFixedAmountProfile = 10000;
        double effectiveHoursProfile = 2000;
        double utilizationPercentageProfile = 80;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile,
                utilizationPercentageProfile));
    }

    //--------------------------------------------------------------------------------------------------------



}
