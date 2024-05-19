package GUI.Utility;

import BE.Profile;
import BE.ProjectTeam;
import GUI.Model.ProfileModel;

public class DataModelSingleton {

    private static DataModelSingleton instance = new DataModelSingleton();
    private ProjectTeam currentTeam;
    private Profile currentProfile;

    private DataModelSingleton() {
    }

    public static synchronized DataModelSingleton getInstance() {
        if (instance == null){
            instance = new DataModelSingleton();
        }
        return instance;
    }

    public ProjectTeam getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(ProjectTeam currentTeam) {
        this.currentTeam = currentTeam;
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }
}
