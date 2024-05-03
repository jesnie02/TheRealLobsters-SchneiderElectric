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
     * Test for getSumOfAnnualSalaryForTeam method
     * Test for correct addition of annual salary's of the profiles in the team
     */
    void getSumOfAnnualSalaryForTeam_returnsCorrectValueForValidInput() {
        //arrange
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(100, 50));
        profiles.add(new Profile(200, 0));
        profiles.add(new Profile(300, 0));

        double annualSalary = 0;
        for (Profile profile :profiles){
            annualSalary += profile.getAnnualSalary();
        }
        double fixedAmount = 0;
        for (Profile profile :profiles){
            fixedAmount += profile.getFixedAmount();
        }
        double expected = 650;

        //act
        double actual = calculatorManager.getSumOfAnnualSalaryForTeam(expected, fixedAmount);
        //assert
        assertEquals(expected, actual);
    }


}
