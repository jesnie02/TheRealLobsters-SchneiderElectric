package GUI.Model;

import BE.Profile;
import BLL.ProfileManager;
import GUI.Controller.util.CurrencyReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Model class for handling Profile related data operations.
 * It communicates with the ProfileManager to perform these operations.
 */
public class ProfileModel {

    private ProfileManager profileManager;

    /**
     * Constructor for the ProfileModel class.
     * It initializes the profileManager variable with an instance of ProfileManager.
     */
    public ProfileModel()  {
        try {
            profileManager = new ProfileManager();
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: Handle this exception
        }
    }


    public ObservableList<String> getCountryAndCurrencyCodes() {
        URL resourceUrl = getClass().getResource("/Currency");
        if (resourceUrl == null) {
            throw new RuntimeException("File not found");
        }
        String filePath = resourceUrl.getPath();
        Map<String, String> currencyMap = CurrencyReader.readCurrencyFromFile(filePath);
        ArrayList<String> countryAndCurrencyCodes = new ArrayList<>();
        for (Map.Entry<String, String> entry : currencyMap.entrySet()) {
            countryAndCurrencyCodes.add(entry.getKey() + " - " + entry.getValue());
        }
        Collections.sort(countryAndCurrencyCodes);
        return FXCollections.observableArrayList(countryAndCurrencyCodes);
    }

    /**
     * Saves a new profile to the database.
     * @param newProfile The new profile to be saved.
     */
    public void saveProfile(Profile newProfile) {
        profileManager.saveProfile(newProfile);

    }


    /**
     * Returns a list of all profile names.
     * @return An ObservableList of profile names.
     */
    public ObservableList<String> showAllProfilesNames(){
        ObservableList<String> profileName = javafx.collections.FXCollections.observableArrayList(
                profileManager.getAllProfiles().stream()
                        .map(profile -> profile.getfName() + " " + profile.getlName())
                        .collect(Collectors.toList())
        );
        return profileName;
    }

    /**
     * Returns a list of all profiles.
     * @return An ObservableList of Profile objects.
     */
    public ObservableList<Profile> getAllProfiles() {
        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        profiles.addAll(profileManager.getAllProfiles());
        return profiles;
    }

    /**
     * Calculates and sets the hourly rate for a profile during creation.
     * @return The calculated hourly rate.
     */
    public double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile) {

        return profileManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile);
    }

    /**
     * Calculates and sets the daily rate for a profile during creation.
     * @return The calculated daily rate.
     */
    public double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate) {
        return profileManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);
    }

    /**
     * Enum for the types of rates a profile can have.
     */
    public enum RateType {
        HOURLY,
        DAILY,
    }

    /**
     * Returns the rate for a profile based on the rate type.
     * @param profileName The name of the profile.
     * @param rateType The type of rate (HOURLY or DAILY).
     * @return The rate for the profile.
     */
    public double getRateForProfile(String profileName, RateType rateType) {
        Profile profile = profileManager.getProfileByName(profileName);
        if (profile == null) {
            throw new RuntimeException("Profile not found");//TODO: Handle this exception
        } else {
            switch (rateType) {
                case HOURLY:
                    return profile.getHourlySalary();
                case DAILY:
                    return profile.getDailyRate();
                default:
                    throw new RuntimeException("Rate type not found");//TODO: Handle this exception
            }

        }
    }

}