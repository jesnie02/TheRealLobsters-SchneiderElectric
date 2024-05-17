package GUI.Model;

import BE.ProfileRole;

import BLL.ProfileRoleManager;
import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileRoleModel{

    private static ProfileRoleManager profileRoleManager;




    public ProfileRoleModel() {
        try {
            profileRoleManager = new ProfileRoleManager();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    public ObservableList<ProfileRole> getProfileRoles() {
        ObservableList<ProfileRole> profileRoles = FXCollections.observableArrayList();
        try {
            profileRoles.addAll(profileRoleManager.getAllProfileRoles());
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return profileRoles;
    }

    public ObservableList<String> seeAllProfileRoles(){
        ObservableList<String> profileRoles = FXCollections.observableArrayList();
        try {
            for (ProfileRole profileRole : profileRoleManager.getAllProfileRoles()) {
                profileRoles.add(profileRole.getProfileRoleType());
            }
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return profileRoles;
    }

    public ObservableList<String> getProfileRolesId() {
        ObservableList<String> profileRoles = FXCollections.observableArrayList();
        try {
            for (ProfileRole profileRole : profileRoleManager.getAllProfileRoles()) {
                profileRoles.add(profileRole.getProfileRoleId() + " " + profileRole.getProfileRoleType());
            }
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        return profileRoles;
    }

    public void saveRole(String role) {
        try {
            profileRoleManager.saveRole(new ProfileRole(role));
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }
}
