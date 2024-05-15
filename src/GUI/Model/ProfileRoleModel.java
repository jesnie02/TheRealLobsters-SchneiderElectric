package GUI.Model;

import BE.ProfileRole;

import BLL.ProfileRoleManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileRoleModel{

    private static ProfileRoleManager profileRoleManager;




    public ProfileRoleModel() throws IOException {
        profileRoleManager = new ProfileRoleManager();
    }

    public ObservableList<ProfileRole> getProfileRoles() {
        ObservableList<ProfileRole> profileRoles = FXCollections.observableArrayList();
        profileRoles.addAll(profileRoleManager.getAllProfileRoles());
        return profileRoles;
    }

    public ObservableList<String> seeAllProfileRoles(){
        ObservableList<String> profileRoles = FXCollections.observableArrayList();
        for (ProfileRole profileRole : profileRoleManager.getAllProfileRoles()) {
            profileRoles.add(profileRole.getProfileRoleType());
        }
        return profileRoles;
    }

    public ObservableList<String> getProfileRolesId() {
        ObservableList<String> profileRoles = FXCollections.observableArrayList();
        for (ProfileRole profileRole : profileRoleManager.getAllProfileRoles()) {
            profileRoles.add(profileRole.getProfileRoleId() + " " + profileRole.getProfileRoleType());
        }
        return profileRoles;
    }

    public void saveRole(String role) throws Exception {
        profileRoleManager.saveRole(new ProfileRole(role));
    }
}
