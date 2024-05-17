package GUI.Model;

import BE.Profile;
import BLL.ProfileManager;
import CustomExceptions.ApplicationWideException;
import GUI.Controller.util.CurrencyReader;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


public class ProfileModel {

    private ProfileManager profileManager;

    public ProfileModel() throws ApplicationWideException {
            profileManager = new ProfileManager();
    }


    public ObservableList<String> getCountryAndCurrencyCodes() {
        URL resourceUrl = getClass().getResource("/Currency");
        String filePath = resourceUrl.getPath();
        Map<String, String> currencyMap = CurrencyReader.readCurrencyFromFile(filePath);
        ArrayList<String> countryAndCurrencyCodes = new ArrayList<>();
        for (Map.Entry<String, String> entry : currencyMap.entrySet()) {
            countryAndCurrencyCodes.add(entry.getKey() + " - " + entry.getValue());
        }
        Collections.sort(countryAndCurrencyCodes);
        return FXCollections.observableArrayList(countryAndCurrencyCodes);
    }


    public void saveProfile(Profile newProfile) {
        try {
            profileManager.saveProfile(newProfile);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }

    }


    public ObservableList<String> showAllProfilesNames(){
        ObservableList<String> profileName = null;
        try {
            profileName = FXCollections.observableArrayList(
                    profileManager.getAllProfiles().stream()
                            .map(profile -> profile.getFName() + " " + profile.getLName())
                            .collect(Collectors.toList())
            );
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return profileName;
    }


    public ObservableList<Profile> getAllProfiles() {
        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        try {
            profiles.addAll(profileManager.getAllProfiles());
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return profiles;
    }


    public double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile) {

        return profileManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile);
    }





    public double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate) {
        return profileManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);
    }


    // Enum for the types of rates a profile can have.

    public enum RateType {
        HOURLY,
        DAILY,
    }


    public double getRateForProfile(String profileName, RateType rateType) {
        Profile profile = null;
        try {
            profile = profileManager.getProfileByName(profileName);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        if (rateType == RateType.HOURLY) {
            return profile.getHourlyRate();
        } else {
            return profile.getDailyRate();
        }
    }

}