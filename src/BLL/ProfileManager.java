package BLL;

import BE.Profile;
import DAL.Profile_DAO;

import java.io.IOException;
import java.util.List;

public class ProfileManager {

    private Profile_DAO profileDAO;
    private CalculatorManager calculatorManager;

    public ProfileManager() throws IOException {
        calculatorManager = new CalculatorManager();
        profileDAO = new Profile_DAO();
    }


    public List<Profile> getAllProfiles(){
        return profileDAO.getAllProfiles();
    }

    public void saveProfile(Profile newProfile) {
        profileDAO.saveProfile(newProfile);
    }

    public Profile getProfileByName(String name) {
    for (Profile profile : getAllProfiles()) {
        if ((profile.getfName() + " " + profile.getlName()).equals(name)) {
            return profile;
        }
    }
    return null;
    }

    public double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile,
            double utilizationPercentageProfile) {

        return calculatorManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile,
                utilizationPercentageProfile);
    }

    public double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate) {
        return calculatorManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);
    }
}
