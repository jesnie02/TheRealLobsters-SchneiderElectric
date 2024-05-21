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
    private static final ObservableList<ProfileRole> roles = FXCollections.observableArrayList();
    private static ProfileRoleModel instance;


    public ProfileRoleModel() {
        try {
            profileRoleManager = new ProfileRoleManager();
            loadRoles();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    public static synchronized ProfileRoleModel getInstance() {
        if (instance == null) {
            instance = new ProfileRoleModel();
        }
        return instance;
    }


   private void loadRoles() {
        if (roles.isEmpty()) {
            try {
                roles.addAll(profileRoleManager.getAllProfileRoles());
            } catch (ApplicationWideException e) {
                ExceptionHandler.handleException(e);
            }
        }
    }

    public ObservableList<ProfileRole> getProfileRoles() {
        return roles;
    }





    public void saveRole(String roleName) {
        try {
            ProfileRole newRole = new ProfileRole(roleName);
            profileRoleManager.saveRole(newRole);
            roles.add(newRole);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }
}
