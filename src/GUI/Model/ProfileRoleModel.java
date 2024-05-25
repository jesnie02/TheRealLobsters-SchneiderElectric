package GUI.Model;

import BE.ProfileRole;
import BLL.ProfileRoleManager;
import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProfileRoleModel{

    private static ProfileRoleManager profileRoleManager;
    private final ObservableList<ProfileRole> roles = FXCollections.observableArrayList();

    // Singleton
    private static ProfileRoleModel instance;


    public ProfileRoleModel() {
        try {
            profileRoleManager = new ProfileRoleManager();
            loadRoles();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    // Singleton
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

    public void createRole(String roleName) {
        try {
            ProfileRole newRole = new ProfileRole(roleName);
            profileRoleManager.createRole(newRole);
            roles.add(newRole);// Add the new role to the observable list
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }
}
