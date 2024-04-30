package GUI.Model;

import BLL.ProfileManager;

public class ProfileModel {

    private String hourlyResult;
    private String fixedAmount;
    private ProfileManager profileManager;

    public ProfileModel() {
        profileManager = new ProfileManager();
    }

    public void setHourlyResultAndFixedAmount(String hourlyResult, String fixedAmount) {
        this.hourlyResult = hourlyResult;
        this.fixedAmount = fixedAmount;
        profileManager.setHourlyResultAndFixedAmount(hourlyResult, fixedAmount);
        System.out.println(hourlyResult + fixedAmount);
    }

    public double fetchAndReturnCalculatedHourlyRateWithFixedAmount() {
        return profileManager.fetchAndReturnCalculatedHourlyRateWithFixedAmount();
    }
}