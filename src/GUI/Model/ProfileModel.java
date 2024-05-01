package GUI.Model;

import BLL.ProfileManager;

public class ProfileModel {

    private ProfileManager profileManager;

    public ProfileModel() {
        profileManager = new ProfileManager();
    }

    public double calculateHourlyRateWithFixedAmount(double hourlyRate, double fixedAmount) {
        return profileManager.calculateHourlyRateWithFixedAmount(hourlyRate, fixedAmount);
    }
}