package GUI.Model;

import BE.Profile;
import BLL.ProfileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileModel {

    private ProfileManager profileManager;


    public ProfileModel()  {
        try {
            profileManager = new ProfileManager();
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: Handle this exception
        }
    }

    public void saveProfile(Profile newProfile) {
        profileManager.saveProfile(newProfile);

    }

    public ObservableList<String> getRoleList() {
        ObservableList<String> roleList = FXCollections.observableArrayList();
        for (Profile.ProjectRole role : Profile.ProjectRole.values()) {
            roleList.add(role.name());
        }
        return roleList;
    }


    public ObservableList<String> showAllProfilesNames(){
        ObservableList<String> profileName = javafx.collections.FXCollections.observableArrayList(
                profileManager.getAllProfiles().stream()
                        .map(profile -> profile.getfName() + " " + profile.getlName())
                        .collect(Collectors.toList())
        );
        return profileName;
    }

    public ObservableList<Profile> getAllProfiles() {
        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        profiles.addAll(profileManager.getAllProfiles());
        return profiles;
    }

    public double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile,
            double utilizationPercentageProfile) {

        return profileManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile,
                utilizationPercentageProfile);
    }

    public double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate) {
        return profileManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);
    }


    public enum RateType {
        HOURLY,
        DAILY,
    }

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