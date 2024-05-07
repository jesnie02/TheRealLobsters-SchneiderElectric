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
     * Test for valid input with multiplication of percentage of both gross margin and markup
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
     * Test for valid input with multiplication of zero of both gross margin and markup
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
    void sumOfDailyRate_returnsCorrectSumForValidProfiles() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(200000, 14124, 1344));
        profiles.add(new Profile(20000, 4354, 200));
        profiles.add(new Profile(30000, 2443, 200));

        double expected = 1744;

        // Act
        double actual = calculatorManager.sumOfDailyRate(profiles);

        // Assert
        assertEquals(expected, actual);
    }


    @Test
    void sumOfDailyRate_returnsZeroForProfilesWithZeroDailyRate() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(100.0, 0.0, 0.0));
        profiles.add(new Profile(200.0, 0.0, 0.0));
        profiles.add(new Profile(300.0, 0.0, 0.0));

        double expected = 0.0;

        // Act
        double actual = calculatorManager.sumOfDailyRate(profiles);

        // Assert
        assertEquals(expected, actual);
    }
    //--------------------------------------------------------------------------------------------------------

    @Test
    void annualSalaryWithFixedAmount_returnsCorrectSumForValidProfiles() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(400000, 300, 4000, 402));
        profiles.add(new Profile(2000, 900, 20, 20));
        profiles.add(new Profile(2000, 900, 20, 20));

        double expected = 406100;

        double actual = calculatorManager.annualSalaryWithFixedAmount(profiles);

        assertEquals(expected, actual);
    }

    @Test
    void annualSalaryWithFixedAmount_returnsZeroForEmptyProfiles() {
        List<Profile> profiles = new ArrayList<>();

        double expected = 0.0;

        double actual = calculatorManager.annualSalaryWithFixedAmount(profiles);

        assertEquals(expected, actual);
    }

    @Test
    void annualSalaryWithFixedAmount_returnsZeroForProfilesWithZeroAnnualsalary() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(0.0, 0.0, 0.0, 0.0));
        profiles.add(new Profile(0.0, 0.0, 0.0, 0.0));
        profiles.add(new Profile(0.0, 0.0, 0.0, 0.0));

        double expected = 0.0;

        double actual = calculatorManager.annualSalaryWithFixedAmount(profiles);

        assertEquals(expected, actual);
    }

   //--------------------------------------------------------------------------------------------------------

    @Test
    void sumOfHourlyRate_returnsCorrectSumForValidProfiles() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(200000, 14124, 1344, 20));
        profiles.add(new Profile(20000, 4354, 200, 15));
        profiles.add(new Profile(30000, 2443, 200, 25));

        double expected = 60;

        // Act
        double actual = calculatorManager.sumOfHourlyRate(profiles);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void sumOfHourlyRate_returnsZeroForEmptyProfiles() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();

        double expected = 0.0;

        // Act
        double actual = calculatorManager.sumOfHourlyRate(profiles);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void sumOfHourlyRate_returnsZeroForProfilesWithZeroHourlyRate() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(100.0, 0.0, 0.0));
        profiles.add(new Profile(200.0, 0.0, 0.0));
        profiles.add(new Profile(300.0, 0.0, 0.0));

        double expected = 0.0;

        // Act
        double actual = calculatorManager.sumOfHourlyRate(profiles);

        // Assert
        assertEquals(expected, actual);
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    void avgHourlyRate_returnsCorrectAverageForValidProfiles() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(200000, 14124, 1344, 20));
        profiles.add(new Profile(20000, 4354, 200, 15));
        profiles.add(new Profile(30000, 2443, 200, 25));

        double expected = 20;

        // Act
        double actual = calculatorManager.avgHourlyRate(profiles);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void avgHourlyRate_returnsZeroForProfilesWithZeroHourlyRate() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(100.0, 0.0, 0.0, 0.0));
        profiles.add(new Profile(200.0, 0.0, 0.0, 0.0));
        profiles.add(new Profile(300.0, 0.0, 0.0, 0.0));

        double expected = 0.0;

        // Act
        double actual = calculatorManager.avgHourlyRate(profiles);

        // Assert
        assertEquals(expected, actual);
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    void avgDailyRate_returnsCorrectAverageForValidProfiles() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(200000, 14124, 1344));
        profiles.add(new Profile(20000, 4354, 200));
        profiles.add(new Profile(30000, 2443, 200));

        double expected = 581.33;

        // Act
        double actual = calculatorManager.avgDailyRate(profiles);

        // Assert
        // Allows the test to pass if the actual value is within 0.01 of the expected value
        assertEquals(expected, actual, 0.01);
    }


    @Test
    void avgDailyRate_returnsZeroForProfilesWithZeroDailyRate() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(100.0, 0.0, 0.0));
        profiles.add(new Profile(200.0, 0.0, 0.0));
        profiles.add(new Profile(300.0, 0.0, 0.0));

        double expected = 0.0;

        // Act
        double actual = calculatorManager.avgDailyRate(profiles);

        // Assert
        assertEquals(expected, actual);
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
    void calculateAndSetHourlyRateCreateProfile_returnsZeroForZeroAnnualsalary() {
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
    void calculateAndSetHourlyRateCreateProfile_throwsExceptionForNegativeAnnualsalary() {
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

    @Test
    void calculateAndSetDailyRateCreateProfile_returnsCorrectValueForValidInput() {
        // Arrange
        double dailyWorkingHours = 8;
        double hourlyRate = 20;
        double expected = 160;

        // Act
        double actual = calculatorManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void calculateAndSetDailyRateCreateProfile_returnsZeroForZeroHourlyRate() {
        // Arrange
        double dailyWorkingHours = 8;
        double hourlyRate = 0;

        // Act
        double actual = calculatorManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);

        // Assert
        assertEquals(0, actual);
    }

    @Test
    void calculateAndSetDailyRateCreateProfile_returnsZeroForZeroWorkingHours() {
        // Arrange
        double dailyWorkingHours = 0;
        double hourlyRate = 20;

        // Act
        double actual = calculatorManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);

        // Assert
        assertEquals(0, actual);
    }


    //--------------------------------------------------------------------------------------------------------

    @Test
    /**
     * Test for getHourlyRateWithMultiplier method
     * Test for valid input with multiplication of percentage of both gross margin and markup
     */
    void getHourlyRateWithMultiplier_returnsCorrectValueForValidInput() {
        //arrange
        double hourlyRate = 100;
        double percentage = 50;
        double expected = 150;
        //act
        double actual = calculatorManager.getHourlyRateWithMultiplier(hourlyRate, percentage);
        //assert
        assertEquals(expected, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithMultiplier method
     * Test for valid input with multiplication of zero of both gross margin and markup
     */
    void getHourlyRateWithMultiplier_returnsZeroForZeroHourlyRate() {
        //arrange
        double hourlyRate = 20;
        double percentage = 0.0;
        //act
        double actual = calculatorManager.getHourlyRateWithMultiplier(hourlyRate, percentage);
        //assert
        assertEquals(20, actual);

        hourlyRate = 0;
        percentage = 50.0;

        actual = calculatorManager.getHourlyRateWithMultiplier(hourlyRate, percentage);

        assertEquals(0.0, actual);
    }

    @Test
    /**
     * Test for getHourlyRateWithMultiplier method
     * Test for valid input with multiplication of negative percentage
     */
    void getHourlyRateWithMultiplier_throwsExceptionForNegativeHourlyRate() {
        //arrange
        double hourlyRate = -100;
        double percentage = 20;
        //act and assert
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getHourlyRateWithMultiplier(hourlyRate, percentage));
    }

    //--------------------------------------------------------------------------------------------------------


}
