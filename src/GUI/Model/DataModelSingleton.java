package GUI.Model;

import BE.ProjectTeam;

public class DataModelSingleton {

    private static DataModelSingleton instance = new DataModelSingleton();
    private ProjectTeam currentTeam;

    private DataModelSingleton() {
    }

    public static DataModelSingleton getInstance() {
        return instance;
    }

    public ProjectTeam getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(ProjectTeam currentTeam) {
        this.currentTeam = currentTeam;
    }
}
