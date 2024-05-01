package BLL;

import BE.Profile;
import DAL.Profile_DAO;

import java.io.IOException;

public class ProfileManager {

    private Profile_DAO profileDAO;
    private CalculatorManager calculatorManager;

    public ProfileManager() throws IOException {
        calculatorManager = new CalculatorManager();
        profileDAO = new Profile_DAO();
    }

    public double calculateHourlyRateWithFixedAmount(double hourlyRate, double fixedAmount) {
        return calculatorManager.getHourlyRateWithFixedAmount(hourlyRate, fixedAmount);
    }


    public void saveProfile(Profile newProfile) {
        profileDAO.saveProfile(newProfile);
    }
}
