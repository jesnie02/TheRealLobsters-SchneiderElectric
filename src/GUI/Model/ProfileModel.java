package GUI.Model;

import BE.Profile;
import BLL.ProfileManager;
import CustomExceptions.ApplicationWideException;
import GUI.Controller.util.CurrencyReader;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;


public class ProfileModel {

    private final ProfileManager profileManager;

    private static ProfileModel instance;
    private ObservableList<Profile> profiles;

    private final UUID id = UUID.randomUUID();

    public ProfileModel() throws ApplicationWideException {
        profileManager = new ProfileManager();
        profiles = FXCollections.observableArrayList(profileManager.getAllProfiles());

    }



    public static synchronized ProfileModel getInstance() {
        if (instance == null) {
            try {
                instance = new ProfileModel();
            } catch (ApplicationWideException e) {
                ExceptionHandler.handleException(e);
            }
        }
        return instance;
    }

     // Create a new profile to the database.
    public void createProfile(Profile newProfile) {
        try {
            profileManager.createProfile(newProfile);
            profiles.add(newProfile);

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





    public double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile) {

        return profileManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile);
    }


    public ObservableList<Profile> getAllProfiles() {
        return profiles;
    }


    public double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate) {
        return profileManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);
    }

    public Map<String, Double> calculateAndSetProfileRatesEUR(double annualSalary, double fixedAmount, double hourlyRate, double dailyRate, String currency) throws ApplicationWideException {
        return profileManager.calculateAndSetProfileRatesEUR(annualSalary, fixedAmount, hourlyRate, dailyRate, currency);
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

    public boolean deleteProfile(Profile profile) {
        try {
            profileManager.deleteProfile(profile);
            return profiles.remove(profile);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
            return false;
        }
    }

    public boolean updateProfile(Profile profile) {
        try {
            if (profileManager.updateProfile(profile)) {
                int index = profiles.indexOf(profile);
                if (index >= 0) {
                    profiles.set(index, profile);
                }
                return true;
            }
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return false;
    }

    public void updateProfileRates(Profile profile) throws ApplicationWideException {
        profileManager.updateProfileRates(profile);
    }

    public UUID getId() {
        return id;
    }
}