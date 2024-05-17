package GUI.Utility;

import BE.ProjectTeam;

public class DataModelSingleton {

    private static DataModelSingleton instance = new DataModelSingleton();
    private ProjectTeam currentTeam;

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
}
