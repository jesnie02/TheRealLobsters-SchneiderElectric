package GUI.Model;

import BE.Profile;
import BLL.ProfileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
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

    public double calculateHourlyRateWithFixedAmount(double hourlyRate, double fixedAmount) {
        return profileManager.calculateHourlyRateWithFixedAmount(hourlyRate, fixedAmount);
    }

    public ObservableList<String> showAllProfilesNames(){
        ObservableList<String> profileName = javafx.collections.FXCollections.observableArrayList(
                profileManager.getAllProfiles().stream()
                        .map(profile -> profile.getfName() + " " + profile.getlName())
                        .collect(Collectors.toList())
        );
        return profileName;
    }
}