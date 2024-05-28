package BLL;

import BE.Profile;
import BE.ProjectTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorManagerTest {

    private CalculatorManager calculatorManager;

    @BeforeEach
    void setUp() {
        calculatorManager = new CalculatorManager();
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
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

        ProjectTeam team = new ProjectTeam(profiles);

        double expected = 20.0; // (20 + 15 + 25) / 3 = 20

        // Act
        double actual = calculatorManager.avgHourlyRate(team);

        // Assert
        assertEquals(expected, actual);

    }

    @Test
    void avgHourlyRate_returnsZeroForProfilesWithZeroHourlyRate() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(100.0, 0.0, 0.0));
        profiles.add(new Profile(200.0, 0.0, 0.0));
        profiles.add(new Profile(300.0, 0.0, 0.0));

        ProjectTeam team = new ProjectTeam(profiles);

        double expected = 0.0;

        // Act
        double actual = calculatorManager.avgHourlyRate(team);

        // Assert
        assertEquals(expected, actual);
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    void avgDailyRate_returnsCorrectAverageForValidProfiles() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(200000, 14124, 1344, 20));
        profiles.add(new Profile(20000, 4354, 200, 15));
        profiles.add(new Profile(30000, 2443, 200, 25));

        ProjectTeam team = new ProjectTeam(profiles);

        double expected = 1744.0 / 3; // Total daily rate sum / number of profiles

        // Act
        double actual = calculatorManager.avgDailyRate(team);

        // Assert
        assertEquals(expected, actual, 0.01);
    }


    @Test
    void avgDailyRate_returnsZeroForProfilesWithZeroDailyRate() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(100.0, 0.0, 0.0));
        profiles.add(new Profile(200.0, 0.0, 0.0));
        profiles.add(new Profile(300.0, 0.0, 0.0));

        ProjectTeam team = new ProjectTeam(profiles);

        double expected = 0.0;

        // Act
        double actual = calculatorManager.avgDailyRate(team);

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


        double expected = 45;

        // Act
        double actual = calculatorManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile);

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

        // Act
        double actual = calculatorManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile);

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

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile));
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
    void getHourlyRateWithMultiplier_throwsExceptionForNegativeHourlyRate() {
        //arrange
        double hourlyRate = -100;
        double percentage = 20;
        //act and assert
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.getHourlyRateWithMultiplier(hourlyRate, percentage));
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    void avgAnnualSalary_returnsCorrectAverageForValidProfiles() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(50000, 10000, 25, 200));
        profiles.add(new Profile(60000, 15000, 30, 250));
        profiles.add(new Profile(70000, 20000, 35, 300));

        ProjectTeam team = new ProjectTeam(profiles);

        double expected = (50000 + 60000 + 70000) / 3.0; // Annual salary + fixed amount / number of profiles

        // Act
        double actual = calculatorManager.avgAnnualSalary(team);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void avgAnnualSalary_returnsZeroForEmptyProfiles() {
        // Arrange
        ProjectTeam team = new ProjectTeam(new ArrayList<>());

        double expected = 0.0;

        // Act
        double actual = calculatorManager.avgAnnualSalary(team);

        // Assert
        assertEquals(expected, actual);

    }

    @Test
    void avgAnnualSalary_returnsZeroForProfilesWithZeroAnnualSalary() {
        // Arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(0.0, 0.0, 0.0, 0.0));
        profiles.add(new Profile(0.0, 0.0, 0.0, 0.0));
        profiles.add(new Profile(0.0, 0.0, 0.0, 0.0));

        ProjectTeam team = new ProjectTeam(profiles);

        double expected = 0.0;

        // Act
        double actual = calculatorManager.avgAnnualSalary(team);

        // Assert
        assertEquals(expected, actual);
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    void calculateAndSetHourlyRateWithUtilization_returnsCorrectValueForValidInput() {
        // Arrange
        double annualSalaryProfile = 50000;
        double overheadMultiplierProfile = 20;
        double annualFixedAmountProfile = 10000;
        double effectiveHoursProfile = 2000;
        double utilizationPercentage = 80; // 80%
        // 50000 + 10000 = 60000
        // 60000 / 2000 = 30
        // 30 * (1 + 20/100) = 36
        // 36 * (80/100) = 28.8
        double expected = 28.8; // ((50000 + 10000) / 2000) * (1 + 20/100) * (80/100) = 28.8

        // Act
        double actual = calculatorManager.calculateAndSetHourlyRateWithUtilization(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile,
                utilizationPercentage);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void calculateAndSetHourlyRateWithUtilization_returnsZeroForZeroAnnualSalary() {
        // Arrange
        double annualSalaryProfile = 0;
        double overheadMultiplierProfile = 20;
        double annualFixedAmountProfile = 10000;
        double effectiveHoursProfile = 2000;
        double utilizationPercentage = 80; // 80%

        double expected = 0;

        // Act
        double actual = calculatorManager.calculateAndSetHourlyRateWithUtilization(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile,
                utilizationPercentage);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void calculateAndSetHourlyRateWithUtilization_throwsExceptionForNegativeAnnualSalary() {
        // Arrange
        double annualSalaryProfile = -50000;
        double overheadMultiplierProfile = 20;
        double annualFixedAmountProfile = 10000;
        double effectiveHoursProfile = 2000;
        double utilizationPercentage = 80; // 80%

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateAndSetHourlyRateWithUtilization(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile,
                utilizationPercentage));
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    void calculateAndSetDailyRateWithUtilization_returnsCorrectValueForValidInput() {
        // Arrange
        double dailyWorkingHours = 8;
        double hourlyRate = 20;
        double expected = 160; // 8 hours * 20 rate = 160

        // Act
        double actual = calculatorManager.calculateAndSetDailyRateWithUtilization(dailyWorkingHours, hourlyRate);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void calculateAndSetDailyRateWithUtilization_returnsZeroForZeroHourlyRate() {
        // Arrange
        double dailyWorkingHours = 8;
        double hourlyRate = 0;
        double expected = 0; // 8 hours * 0 rate = 0

        // Act
        double actual = calculatorManager.calculateAndSetDailyRateWithUtilization(dailyWorkingHours, hourlyRate);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void calculateAndSetDailyRateWithUtilization_returnsZeroForZeroWorkingHours() {
        // Arrange
        double dailyWorkingHours = 0;
        double hourlyRate = 20;
        double expected = 0; // 0 hours * 20 rate = 0

        // Act
        double actual = calculatorManager.calculateAndSetDailyRateWithUtilization(dailyWorkingHours, hourlyRate);

        // Assert
        assertEquals(expected, actual);
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    void calculateAndSetProfileRatesEUR_returnsCorrectValuesForValidInput() {

        // Arrange
        double annualSalary = 50000;
        double fixedAmount = 10000;
        double hourlyRate = 25;
        double dailyRate = 200;
        double conversionRate = 1.2; // Assuming 1 EUR = 1.2 of the selected currency

        Map<String, Double> expected = new HashMap<>();
        expected.put("annualSalaryEUR", (annualSalary + fixedAmount) / conversionRate);
        expected.put("hourlyRateEUR", hourlyRate / conversionRate);
        expected.put("dailyRateEUR", dailyRate / conversionRate);

        // Act
        Map<String, Double> actual = calculatorManager.calculateAndSetProfileRatesEUR(
                annualSalary, fixedAmount, hourlyRate, dailyRate, conversionRate);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void calculateAndSetProfileRatesEUR_returnsZeroForZeroConversionRate() {

        // Arrange
        double annualSalary = 50000;
        double fixedAmount = 10000;
        double hourlyRate = 25;
        double dailyRate = 200;
        double conversionRate = 0;

        Map<String, Double> expected = new HashMap<>();
        expected.put("annualSalaryEUR", 0.0);
        expected.put("hourlyRateEUR", 0.0);
        expected.put("dailyRateEUR", 0.0);

        // Act
        Map<String, Double> actual = calculatorManager.calculateAndSetProfileRatesEUR(
                annualSalary, fixedAmount, hourlyRate, dailyRate, conversionRate);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void calculateAndSetProfileRatesEUR_throwsExceptionForNegativeRates() {
        // Arrange
        double fixedAmount = 10000;
        double hourlyRate = 25;
        double dailyRate = 200;
        double conversionRate = 1.2;

        // Act and Assert for negative annualSalary
        double annualSalary = -50000;
        double finalAnnualSalary = annualSalary;
        double finalFixedAmount = fixedAmount;
        double finalHourlyRate = hourlyRate;
        double finalDailyRate = dailyRate;
        double finalConversionRate = conversionRate;
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateAndSetProfileRatesEUR(
                finalAnnualSalary, finalFixedAmount, finalHourlyRate, finalDailyRate, finalConversionRate));

        // Act and Assert for negative fixedAmount
        annualSalary = 50000;
        fixedAmount = -10000;
        double finalAnnualSalary1 = annualSalary;
        double finalFixedAmount1 = fixedAmount;
        double finalHourlyRate1 = hourlyRate;
        double finalDailyRate1 = dailyRate;
        double finalConversionRate1 = conversionRate;
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateAndSetProfileRatesEUR(
                finalAnnualSalary1, finalFixedAmount1, finalHourlyRate1, finalDailyRate1, finalConversionRate1));

        // Act and Assert for negative hourlyRate
        fixedAmount = 10000;
        hourlyRate = -25;
        double finalAnnualSalary2 = annualSalary;
        double finalFixedAmount2 = fixedAmount;
        double finalHourlyRate2 = hourlyRate;
        double finalDailyRate2 = dailyRate;
        double finalConversionRate2 = conversionRate;
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateAndSetProfileRatesEUR(
                finalAnnualSalary2, finalFixedAmount2, finalHourlyRate2, finalDailyRate2, finalConversionRate2));

        // Act and Assert for negative dailyRate
        hourlyRate = 25;
        dailyRate = -200;
        double finalAnnualSalary3 = annualSalary;
        double finalFixedAmount3 = fixedAmount;
        double finalHourlyRate3 = hourlyRate;
        double finalDailyRate3 = dailyRate;
        double finalConversionRate3 = conversionRate;
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateAndSetProfileRatesEUR(
                finalAnnualSalary3, finalFixedAmount3, finalHourlyRate3, finalDailyRate3, finalConversionRate3));

        // Act and Assert for negative conversionRate
        dailyRate = 200;
        conversionRate = -1.2;
        double finalAnnualSalary4 = annualSalary;
        double finalFixedAmount4 = fixedAmount;
        double finalHourlyRate4 = hourlyRate;
        double finalDailyRate4 = dailyRate;
        double finalConversionRate4 = conversionRate;
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateAndSetProfileRatesEUR(
                finalAnnualSalary4, finalFixedAmount4, finalHourlyRate4, finalDailyRate4, finalConversionRate4));
    }

    //--------------------------------------------------------------------------------------------------------

    @Test
    void calculateRatesWithUtilizationForUpdateTeam_returnsCorrectValuesForValidInput() {
        // Arrange
        Profile profile = new Profile(50000, 10000, 25, 200); // Assuming these are the annualSalary, fixedAmount, hourlyRate, and dailyRate respectively
        double utilization = 80; // 80%

        double expectedHourlyRate = profile.getHourlySalary() * (utilization / 100);
        double expectedDailyRate = profile.getDailyRate() * (utilization / 100);
        double expectedAnnualSalary = profile.getAnnualSalary() * (utilization / 100);

        Map<String, Double> expected = new HashMap<>();
        expected.put("hourlyRate", expectedHourlyRate);
        expected.put("dailyRate", expectedDailyRate);
        expected.put("annualSalary", expectedAnnualSalary);

        // Act
        Map<String, Double> actual = calculatorManager.calculateRatesWithUtilizationForUpdateTeam(profile, utilization);

        // Assert
        assertEquals(expected, actual);
        // Also assert that the profile's rates have been updated
        assertEquals(expectedHourlyRate, profile.getHourlyRate());
        assertEquals(expectedDailyRate, profile.getDailyRate());
        assertEquals(expectedAnnualSalary, profile.getAnnualSalary());
    }

    @Test
    void calculateRatesWithUtilizationForUpdateTeam_returnsOriginalValuesForZeroUtilization() {
        // Arrange
        Profile profile = new Profile(50000, 10000, 25, 200);
        double utilization = 0; // 0%

        double expectedHourlyRate = profile.getHourlySalary();
        double expectedDailyRate = profile.getDailyRate();
        double expectedAnnualSalary = profile.getAnnualSalary();

        Map<String, Double> expected = new HashMap<>();
        expected.put("hourlyRate", expectedHourlyRate);
        expected.put("dailyRate", expectedDailyRate);
        expected.put("annualSalary", expectedAnnualSalary);

        // Act
        Map<String, Double> actual = calculatorManager.calculateRatesWithUtilizationForUpdateTeam(profile, utilization);

        // Assert
        assertEquals(expected, actual);
        // Also assert that the profile's rates have been updated
        assertEquals(expectedHourlyRate, profile.getHourlyRate());
        assertEquals(expectedDailyRate, profile.getDailyRate());
        assertEquals(expectedAnnualSalary, profile.getAnnualSalary());
    }

    @Test
    void calculateRatesWithUtilizationForUpdateTeam_throwsExceptionForNegativeUtilization() {
        // Arrange
        Profile profile = new Profile(50000, 10000, 25, 200);
        double utilization = -10; // -10%

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> calculatorManager.calculateRatesWithUtilizationForUpdateTeam(profile, utilization));
    }

}
